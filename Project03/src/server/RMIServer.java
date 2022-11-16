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

    private static final int TRANSACTION_TIMEOUT = 12000; //5000 ms = 5 sec
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

    protected final Log serverLog;

    protected CoordinatorServer coordinatorServer;

    private static final Object TRANSACTION_LOCK = new Object();

    /**
     * Constructor for RMIServer
     *
     * @param port port at which the server should listen
     * @throws RemoteException If unable to create instance of remote object db.
     */
    public RMIServer(int port) throws RemoteException {
        super(port);
        this.port = port;
        this.header = new ServerHeader("localhost", this.port);
        this.db = new MyKeyValueDB();
        this.db.populate();
        this.serverLog = new Log();

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
            System.out.println("RMIServer started at port: "+port);

            Registry coordinatorRegistry = LocateRegistry.getRegistry(CoordinatorServer.PORT);
            coordinatorServer = (CoordinatorServer) coordinatorRegistry.lookup(CoordinatorServer.SERVER_LIST_SERVICE);
            if(coordinatorServer.getAllServers().size() == 0) {
                db.populate();
            } else {
                ServerHeader otherHeader = coordinatorServer.getAllServers().get(0);
                Registry otherServerRegistry = LocateRegistry.getRegistry(otherHeader.getHost(), otherHeader.getPort());
                Server otherServer = (Server) otherServerRegistry.lookup("KeyValueDBService");
                db = otherServer.getDBCopy();
            }
            coordinatorServer.addServer(getServerHeader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String get(String key) throws RemoteException {
        return (String) new GetCommand(key).execute(db);
    }

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
    public synchronized boolean canCommit(Transaction transaction) throws RemoteException {
        if(transaction == null) {
            return false;
        }
        while(currentTransaction != null) {
            continue;
        }
        copyDB = db.copy();
        currentTransaction = transaction;
        currentTransaction.execute(copyDB);
        if(currentTransaction.getResult()!=null && currentTransaction.getResult().size()!=0) {   //&& new Random().nextInt(5) != 0) {
            System.out.println("canCommit "+transaction.getId()+": true");
            waitForTransactionResponse = new Thread(() -> {
                try {
                    Thread.sleep(TRANSACTION_RESPONSE_WAIT_TIME);
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
            System.out.println("canCommit "+transaction.getId()+": false");
            copyDB = null;
            currentTransaction = null;
            result = null;
            return false;
        }
    }

    @Override
    public void doCommit(Transaction transaction) throws RemoteException {
        System.out.println("doCommit: "+transaction.getId());
        if(committed.contains(transaction.getId())) {
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
            getServerFromHeader(transaction.getCallerHeader()).haveCommitted(transaction, this.getServerHeader());
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doAbort(Transaction transaction) throws RemoteException{
        System.out.println("doAbort: "+transaction.getId());
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
        haveCommittedCount++;
    }

    @Override
    public boolean getDecision(Transaction transaction) throws RemoteException{
        System.out.println("getDecision: "+transaction.getId());
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
                    List<ServerHeader> headerList = coordinatorServer.getAllServers();
                    List<Server> serverList = new ArrayList<>();
                    List<Server> canCommitList = new ArrayList<>();
                    for(ServerHeader serverHeader: headerList) {
                        serverList.add(getServerFromHeader(serverHeader));
                    }
                    serverCount = serverList.size();
                    for(Server server: serverList) {
                        boolean res = server.canCommit(transaction);
                        if(res) {
                            canCommitList.add(server);
                        }
                        canCommitResponseCount++;
                    }
                    Thread.sleep(5000);
                    if(canCommitList.size() == serverList.size()) {
                        startCommit = true;
                        for(Server server: canCommitList) {
                            server.doCommit(transaction);
                        }
                        while(haveCommittedCount!=serverCount) {
                            continue;
                        }
                    } else {
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
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
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

    private void transactionResetManager() {
        serverCount = 0;
        canCommitResponseCount = 0;
        haveCommittedCount = 0;
        startCommit = false;
    }

    private void transactionResetServer() {

    }
}
