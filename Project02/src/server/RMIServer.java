package server;

import util.KeyValueDB;
import util.Log;
import util.MyKeyValueDB;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer implements Server {

    protected final int port;
    protected final KeyValueDB db;

    protected final Log serverLog;

    /**
     * Constructor for RMIServer
     *
     * @param port port at which the server should listen
     * @throws IOException
     */
    public RMIServer(int port) throws RemoteException {
        this.port = port;
        this.db = new MyKeyValueDB();
        this.db.populate();
        this.serverLog = new Log();
    }

    @Override
    public void start() {
        try {
            KeyValueDB db = new MyKeyValueDB();
            db.populate();
            Registry registry = LocateRegistry.getRegistry(port);
            registry.rebind("KeyValueDBService", db);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }
}
