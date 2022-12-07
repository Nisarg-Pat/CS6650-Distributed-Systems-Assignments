package server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import server.command.Command;
import server.command.DeleteCommand;
import server.command.GetCommand;
import server.command.PutCommand;
import server.paxos.*;
import util.KeyValueDB;
import util.Log;

/**
 * Access: pakage-protected
 * Class for RMI Server
 */
public class PaxosServerImpl extends UnicastRemoteObject implements KeyValueDB, PaxosServer {

    private static final int TRANSACTION_TIMEOUT = 10000; //10000 ms = 10 sec
    private static final int TRANSACTION_RESPONSE_WAIT_TIME = 2000;
    protected final int port;
    protected KeyValueStore store;
    private final ServerHeader header;


    //For Paxos
    private int minProposal;
    private Proposal acceptedProposal;

//    //For All Servers
//    private Transaction currentTransaction;
//    private KeyValueStore copyDB;
//    private List<Object> result;
//    private Thread waitForTransactionResponse;
//    private final Set<String> committed;
//
//    //For Transaction Manager
//    private int serverCount;
//    private int canCommitResponseCount;
//    private int haveCommittedCount;
//    private boolean startCommit;

    //For Paxos
//    Proposer proposer;
//    Acceptor acceptor;
//    Learner learner;

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
        this.store = new KeyValueStore();
        this.store.populate();

        resetPaxos();
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
            List<Server> allServers = coordinatorServer.getAllServers();

            //Replicating db as one of the servers else initializing
            if (allServers.size() == 0) {
                store.populate();
            } else {
//                ServerHeader otherHeader = allServers.get(0);
//                Registry otherServerRegistry = LocateRegistry.getRegistry(otherHeader.getHost(), otherHeader.getPort());
                PaxosServer otherServer = (PaxosServer) allServers.get(0);
                store = otherServer.replicate();
            }
            coordinatorServer.addServer(this);
            Log.logln("RMIServer started at host: " + header.getHost() + ", port: " + header.getPort());
        } catch (Exception e) {
            Log.logln(e.getMessage());
        }
    }

    //Get command not requiring distributed transaction
    @Override
    public String get(String key) throws RemoteException {
        return (String) new GetCommand(key).execute(store);
    }

    //Put command as a distributed transaction
    @Override
    public boolean put(String key, String value) throws RemoteException {
        Command command = new PutCommand(key, value);
        return (boolean) coordinatorServer.propose(command);
    }

    //Delete command as a distributed transaction
    @Override
    public boolean delete(String key) throws RemoteException {
        Command command = new DeleteCommand(key);
        return (boolean) coordinatorServer.propose(command);
    }

    @Override
    public void populate() throws RemoteException {
        store.populate();
    }

    @Override
    public String getString() throws RemoteException {
        return store.getString();
    }

    @Override
    public KeyValueStore replicate() {
        return store.copy();
    }

    @Override
    public CoordinatorServer getCoordinator() {
        return coordinatorServer;
    }

    @Override
    public ServerHeader getServerHeader() throws RemoteException {
        return header;
    }



    @Override
    public Promise prepare(Proposal proposal) throws RemoteException {
        if(proposal.getProposalNumber() > minProposal) {
            minProposal = proposal.getProposalNumber();
            Log.logln("Got Preparation request for "+proposal+": Promised");
            return new Promise(true, acceptedProposal);
        } else {
            Log.logln("Got Preparation request for "+proposal+": Denied");
            return new Promise(false, null);
        }
    }

    @Override
    public int accept(Proposal proposal) throws RemoteException {
        if(proposal.getProposalNumber() >= minProposal) {
            minProposal = proposal.getProposalNumber();
            acceptedProposal = new Proposal(proposal);
            Log.logln("Got Accept request for "+proposal+": Accepted");
//            acceptedProposal.getCommand().execute(store);
        } else {
            Log.logln("Got Accept request for "+proposal+": Rejected");
        }
        return minProposal;
    }

    @Override
    public Object learn(Proposal proposal) throws RemoteException {
        Command command = proposal.getCommand();
        Log.logln("Got Learn request for "+proposal+": Learned");
        resetPaxos();
        return (Object)command.execute(store);
    }

    private void resetPaxos() {
        minProposal = 0;
        acceptedProposal = null;
    }
}
