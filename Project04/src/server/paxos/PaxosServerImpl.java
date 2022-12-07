package server.paxos;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import server.*;
import server.command.DeleteCommand;
import server.command.GetCommand;
import server.command.PutCommand;
import util.KeyValueDB;
import util.Log;

/**
 * Access: pakage-protected
 * Class for RMI Server
 */
class PaxosServerImpl extends UnicastRemoteObject implements KeyValueDB, PaxosServer {

    private static final int TRANSACTION_TIMEOUT = 10000; //10000 ms = 10 sec
    private static final int TRANSACTION_RESPONSE_WAIT_TIME = 2000;
    protected final int port;
    protected MyKeyValueDB db;
    private final ServerHeader header;

    //For All Servers
    private Transaction currentTransaction;
    private MyKeyValueDB copyDB;
    private List<Object> result;
    private Thread waitForTransactionResponse;
    private final Set<String> committed;

    //For Transaction Manager
    private int serverCount;
    private int canCommitResponseCount;
    private int haveCommittedCount;
    private boolean startCommit;

    //For Coordinator Server
    private final String coordinatorHost;
    protected CoordinatorServer coordinatorServer;

    //Lock for transaction
    private static final Object TRANSACTION_LOCK = new Object();

    /**
     * Constructor for RMIServer
     *
     * @param port port at which the server should listen
     * @throws RemoteException If unable to create instance of remote object db.
     */
    public PaxosServerImpl(String host, int port, String coordinatorHost) throws RemoteException {
        super(port);
        this.port = port;
        this.header = new ServerHeader(host, this.port);
        this.coordinatorHost = coordinatorHost;
        this.db = new MyKeyValueDB();
        this.db.populate();

        this.copyDB = null;
        this.currentTransaction = null;
        this.result = null;
        this.committed = new HashSet<>();
        waitForTransactionResponse = null;
    }

    @Override
    public void start() throws RemoteException {
        try {
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("KeyValueDBService", this);

            //Locating the coordinator
            Registry coordinatorRegistry = LocateRegistry.getRegistry(coordinatorHost, CoordinatorServer.PORT);
            coordinatorServer = (CoordinatorServer) coordinatorRegistry.lookup(CoordinatorServer.SERVER_LIST_SERVICE);

            //Getting currentList of servers
            List<ServerHeader> allServers = coordinatorServer.getAllServers();

            //Replicating db as one of the servers else initializing
            if(allServers.size() == 0) {
                db.populate();
            } else {
                ServerHeader otherHeader = allServers.get(0);
                Registry otherServerRegistry = LocateRegistry.getRegistry(otherHeader.getHost(), otherHeader.getPort());
                Server otherServer = (Server) otherServerRegistry.lookup("KeyValueDBService");
                db = otherServer.getDBCopy();
            }
            coordinatorServer.addServer(getServerHeader());
            Log.logln("RMIServer started at host: "+header.getHost()+", port: "+header.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Get command not requiring distributed transaction
    @Override
    public String get(String key) throws RemoteException {
        return (String) new GetCommand(key).execute(db);
    }

    //Put command as a distributed transaction
    @Override
    public boolean put(String key, String value) throws RemoteException {
        Transaction transaction = new Transaction(""+this.port+""+System.currentTimeMillis(), this.header);
        transaction.addCommand(new PutCommand(key, value));
        List<Object> result = performTransaction(transaction);
        if(result == null || result.size() == 0) {
            return false;
        }
        return (boolean) result.get(0);
    }

    //Delete command as a distributed transaction
    @Override
    public boolean delete(String key) throws RemoteException {
        Transaction transaction = new Transaction(""+this.port+""+System.currentTimeMillis(), this.header);
        transaction.addCommand(new DeleteCommand(key));
        List<Object> result = performTransaction(transaction);
        if(result == null || result.size() == 0) {
            return false;
        }
        return (boolean) result.get(0);
    }

    @Override
    public void populate() throws RemoteException {
        db.populate();
    }

    @Override
    public String getString() throws RemoteException {
        return db.getString();
    }

    @Override
    public MyKeyValueDB getDBCopy() throws RemoteException {
        return db.copy();
    }

    @Override
    public ServerHeader getServerHeader() throws RemoteException {
        return header;
    }


    private Server getServerFromHeader(ServerHeader header) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(header.getHost(), header.getPort());
        Server server = (Server)registry.lookup("KeyValueDBService");
        return server;
    }

    @Override
    public Proposer getProposer() {
        return null;
    }

    @Override
    public Acceptor getAcceptor() {
        return null;
    }

    @Override
    public Learner getLearner() {
        return null;
    }
}
