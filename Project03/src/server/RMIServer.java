package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import util.KeyValueDB;
import util.Log;

/**
 * Access: pakage-protected
 * Class for RMI Server
 */
class RMIServer extends UnicastRemoteObject implements KeyValueDB, Server {

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
            System.out.println("Trouble: " + e);
        }
    }

    @Override
    public String get(String key) throws RemoteException {
        return db.get(key);
    }

    @Override
    public boolean put(String key, String value) throws RemoteException {
        return db.put(key, value);
    }

    @Override
    public boolean delete(String key) throws RemoteException {
        return db.delete(key);
    }

    @Override
    public void populate() throws RemoteException {
        db.populate();
    }

    @Override
    public String getString() throws RemoteException {
        return db.getString();
    }
}
