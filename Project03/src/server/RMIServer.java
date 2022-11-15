package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

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

    protected final int port;
    protected final MyKeyValueDB db;

    protected final Log serverLog;

    protected CoordinatorServer coordinatorServer;

    /**
     * Constructor for RMIServer
     *
     * @param port port at which the server should listen
     * @throws RemoteException If unable to create instance of remote object db.
     */
    public RMIServer(int port) throws RemoteException {
        super(port);
        this.port = port;
        this.db = new MyKeyValueDB();
        this.db.populate();
        this.serverLog = new Log();
    }

    @Override
    public void start() throws RemoteException {
        try {
            db.populate();
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("KeyValueDBService", this);
            System.out.println("RMIServer started");

            Registry coordinatorRegistry = LocateRegistry.getRegistry(CoordinatorServer.PORT);
            coordinatorServer = (CoordinatorServer) coordinatorRegistry.lookup(CoordinatorServer.SERVER_LIST_SERVICE);
            coordinatorServer.addServer(this);
            System.out.println(coordinatorServer.getAllServers());
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
        Transaction transaction = new Transaction(""+this.port+""+System.currentTimeMillis());
        transaction.addCommand(new PutCommand(key, value));
        List<Object> result = performTransaction(transaction);
        return (boolean) result.get(0);
    }

    @Override
    public boolean delete(String key) throws RemoteException {
        return (boolean) new DeleteCommand(key).execute(db);
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
    public boolean canCommit(Transaction transaction) throws RemoteException{
        return false;
    }

    @Override
    public List<Object> doCommit(Transaction transaction) throws RemoteException{
        transaction.execute(db);
        return transaction.getResult();
    }

    @Override
    public List<Object> doAbort(Transaction transaction) throws RemoteException{
        return null;
    }

    @Override
    public boolean haveCommited(Transaction transaction) throws RemoteException{
        return false;
    }

    @Override
    public boolean getDecision(Transaction transaction) throws RemoteException{
        return false;
    }

    @Override
    public List<Object> performTransaction(Transaction transaction) throws RemoteException{
        List<Object> result = null;
        try {
            for(Server server: coordinatorServer.getAllServers()) {
                result = server.doCommit(transaction);
            }
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
}
