package server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    protected MyKeyValueDB db;
    private ServerHeader header;

    private MyKeyValueDB copyDB;
    private List<Object> result;

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
        this.header = new ServerHeader("localhost", this.port);
        this.db = new MyKeyValueDB();
        this.db.populate();
        this.serverLog = new Log();
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
        return (boolean) result.get(0);
    }

    @Override
    public boolean delete(String key) throws RemoteException {
        Transaction transaction = new Transaction(""+this.port+""+System.currentTimeMillis(), this.header);
        transaction.addCommand(new DeleteCommand(key));
        List<Object> result = performTransaction(transaction);
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
    public boolean canCommit(Transaction transaction) throws RemoteException{
        copyDB = db.copy();
        result = transaction.execute(copyDB);
        if(result!=null && result.size()!=0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Object> doCommit(Transaction transaction) throws RemoteException{
        db = copyDB;
        copyDB = null;
        return result;
    }

    @Override
    public List<Object> doAbort(Transaction transaction) throws RemoteException{
        copyDB = null;
        return null;
    }

    @Override
    public boolean haveCommitted(Transaction transaction) throws RemoteException{
        return false;
    }

    @Override
    public boolean getDecision(Transaction transaction) throws RemoteException{
        return false;
    }

    @Override
    public List<Object> performTransaction(Transaction transaction) throws RemoteException{
        try {
            int totalCanCommit = 0;
            List<ServerHeader> headerList = coordinatorServer.getAllServers();
            List<Server> serverList = new ArrayList<>();
            for(ServerHeader serverHeader: headerList) {
                serverList.add(getServerFromHeader(serverHeader));
            }
            for(Server server: serverList) {
                boolean res = server.canCommit(transaction);
                if(res) {
                    totalCanCommit++;
                } else {
                    break;
                }
            }
            if(totalCanCommit == serverList.size()) {
                for(Server server: serverList) {
                    server.doCommit(transaction);
                }
            } else {
                for(Server server: serverList) {
                    server.doAbort(transaction);
                }
            }
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        return result;
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
}
