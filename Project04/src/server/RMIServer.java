package server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import server.command.DeleteCommand;
import server.command.GetCommand;
import server.command.PutCommand;
import util.KeyValueDB;
import util.Log;

/**
 * Access: pakage-protected
 * Class for RMI Server
 */
class RMIServer extends UnicastRemoteObject implements KeyValueDB, Server{

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
    public RMIServer(String host, int port, String coordinatorHost) throws RemoteException {
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
        transactionResetManager();
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


    //
    //Start of distributed transaction algorithm and methods
    //

    @Override
    public synchronized boolean canCommit(Transaction transaction) throws RemoteException {
        if(transaction == null) {
            return false;
        }
        while(currentTransaction != null) {
            continue;
        }
        //In memory copy of the database
        copyDB = db.copy();
        currentTransaction = transaction;
        currentTransaction.execute(copyDB);
//        try {
//            int delay = new Random().nextInt(1000);
//            Log.logln(delay);
//            Thread.sleep(delay);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        //Can commit the transaction
        if(currentTransaction.getResult()!=null && currentTransaction.getResult().size()!=0) { //&& new Random().nextInt(10) != 0) {
            Log.logln("canCommit "+transaction.getId()+": true");
            //Waiting for reply from the manager to either commit or abort
            waitForTransactionResponse = new Thread(() -> {
                try {
                    Thread.sleep(TRANSACTION_RESPONSE_WAIT_TIME);
                    Log.logln("Requesting detDecision");
                    Server transactionServer = getServerFromHeader(transaction.getCallerHeader());
                    boolean result = transactionServer.getDecision(transaction);
                    if(result) {
                        doCommit(transaction);
                    } else {
                        doAbort(transaction);
                    }
                } catch (RemoteException | NotBoundException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    //Empty
                }
            });
            waitForTransactionResponse.start();
            return true;
        } else {
            //Cannot commit the transaction. Abort immediately
            Log.logln("canCommit "+transaction.getId()+": false");
            copyDB = null;
            currentTransaction = null;
            result = null;
            return false;
        }
    }

    @Override
    public void doCommit(Transaction transaction) throws RemoteException {
        //Commit the transaction
        Log.logln("doCommit: "+transaction.getId());
        if(committed.contains(transaction.getId())) {
            Log.logln("AlreadyCommitted: "+transaction.getId());
            return;
        }
        committed.add(transaction.getId());
        db = copyDB;

        copyDB = null;
        result = currentTransaction.getResult();
        if(waitForTransactionResponse!=null && waitForTransactionResponse.isAlive()) {
            waitForTransactionResponse.interrupt();
            waitForTransactionResponse = null;
        }
        currentTransaction = null;
        try {
            //Send a haveCommited message to Manager.
            getServerFromHeader(transaction.getCallerHeader()).haveCommitted(transaction, this.getServerHeader());
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doAbort(Transaction transaction) throws RemoteException{
        //Abort the transaction
        Log.logln("doAbort: "+transaction.getId());
        copyDB = null;
        result = null;
        if(waitForTransactionResponse!=null && waitForTransactionResponse.isAlive()) {
            waitForTransactionResponse.interrupt();
            waitForTransactionResponse = null;
        }
        currentTransaction = null;
    }

    @Override
    public void haveCommitted(Transaction transaction, ServerHeader header) throws RemoteException {
        //Received a havec ommited message, increment the counter.
        haveCommittedCount++;
    }

    @Override
    public boolean getDecision(Transaction transaction) throws RemoteException{
        //Server asking for a decision to commit or not to manager.
        Log.logln("getDecision: "+transaction.getId());
        while(canCommitResponseCount != serverCount) {
            continue;
        }
        return startCommit;
    }

    @Override
    public List<Object> performTransaction(Transaction transaction) throws RemoteException {
        synchronized (TRANSACTION_LOCK) {
            Thread transactionThread = new Thread(() -> {
                try {
                    //Getting the list of active servers.
                    List<ServerHeader> headerList = coordinatorServer.getAllServers();
                    List<Server> serverList = new ArrayList<>();
                    List<Server> canCommitList = new ArrayList<>();
                    for(ServerHeader serverHeader: headerList) {
                        serverList.add(getServerFromHeader(serverHeader));
                    }
                    serverCount = serverList.size();

                    //Voting phase
                    for(Server server: serverList) {
                        boolean res = server.canCommit(transaction);
                        if(res) {
                            canCommitList.add(server);
                        }
                        canCommitResponseCount++;
                    }

                    //Completion according to outcome phase
                    if(canCommitList.size() == serverList.size()) {
                        //doCommit
                        startCommit = true;
                        for(Server server: canCommitList) {
                            server.doCommit(transaction);
                        }
                        while(haveCommittedCount!=serverCount) {
                            continue;
                        }
                    } else {
                        //doAbort
                        for(Server server: canCommitList) {
                            server.doAbort(transaction);
                        }
                    }
                    transactionResetManager();
                } catch (RemoteException | NotBoundException e) {
                    e.printStackTrace();
                    copyDB = null;
                    currentTransaction = null;
                    result = null;
                    transactionResetManager();
                }
            });
            //Timeout mechanism
            long timeoutTime = System.currentTimeMillis() + TRANSACTION_TIMEOUT;
            transactionThread.start();
            while (transactionThread.isAlive()) {
                long currentTime = System.currentTimeMillis();
                if (currentTime >= timeoutTime) {
                    copyDB = null;
                    currentTransaction = null;
                    result = null;
                    transactionThread.interrupt();
                    break;
                }
            }
            return result;
        }
    }
    private Server getServerFromHeader(ServerHeader header) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(header.getHost(), header.getPort());
        Server server = (Server)registry.lookup("KeyValueDBService");
        return server;
    }

    private void transactionResetManager() {
        serverCount = 0;
        canCommitResponseCount = 0;
        haveCommittedCount = 0;
        startCommit = false;
    }
}
