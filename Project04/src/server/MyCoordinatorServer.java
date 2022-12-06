package server;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.Log;

/**
 * Implementation of Coordinator Server. It's function is to store the details of all the servers in the application.
 * Should run before any other server starts
 */
public class MyCoordinatorServer extends UnicastRemoteObject implements CoordinatorServer {

  private final int port;
  private final ServerHeader header;
  Set<ServerHeader> serverSet;
  private Server tempServer;

  private static final int GET_TIMEOUT = 1000;

  protected MyCoordinatorServer(String host) throws RemoteException {
    super();
    this.port = CoordinatorServer.PORT;
    this.header = new ServerHeader(host, this.port);
    serverSet = new HashSet<>();
  }

  @Override
  public void addServer(ServerHeader server) throws RemoteException {
    serverSet.add(server);
  }

  @Override
  public List<ServerHeader> getAllServers() throws RemoteException {
    //Temporary set of not accessible servers
    //To get accurate list of running servers
    Set<ServerHeader> removedSet = new HashSet<>();
    for(ServerHeader serverHeader: serverSet) {
        tempServer = null;
        Thread lookupTimeOut = new Thread(() -> {
          try {
            Registry registry = LocateRegistry.getRegistry(serverHeader.getHost(), serverHeader.getPort());
            tempServer = (Server) registry.lookup("KeyValueDBService");
          } catch (NotBoundException | RemoteException e) {
            tempServer = null;
          }
        });
        long timeoutTime = System.currentTimeMillis() + GET_TIMEOUT;
        lookupTimeOut.start();
        while (lookupTimeOut.isAlive()) {
          long currentTime = System.currentTimeMillis();
          if (currentTime >= timeoutTime) {
            lookupTimeOut.interrupt();
            break;
          }
        }
        if(tempServer == null) {
          //If unable to find the server, remove it
          removedSet.add(serverHeader);
        }
    }
    for(ServerHeader serverHeader: removedSet) {
      //Removing the servers
      serverSet.remove(serverHeader);
    }
    //Printing the current server size.
    Log.logln("Current server size: "+serverSet.size());
    return new ArrayList<>(serverSet);
  }

  @Override
  public void start() throws RemoteException {
    try {
      Registry registry = LocateRegistry.createRegistry(port);
      registry.rebind(SERVER_LIST_SERVICE, this);
      Log.logln("CoordinatorServer started at host: "+header.getHost()+", port: "+header.getPort());
    } catch (Exception e) {
      Log.logln("Error while starting: " + e);
    }
  }

  @Override
  public boolean canCommit(Transaction transaction) throws RemoteException {
    return false;
  }

  @Override
  public void doCommit(Transaction transaction) throws RemoteException {
  }

  @Override
  public void doAbort(Transaction transaction) throws RemoteException {
  }

  @Override
  public void haveCommitted(Transaction transaction, ServerHeader header) throws RemoteException {
  }

  @Override
  public boolean getDecision(Transaction transaction) throws RemoteException {
    return false;
  }

  @Override
  public List<Object> performTransaction(Transaction transaction) throws RemoteException {
    return null;
  }

  @Override
  public MyKeyValueDB getDBCopy() throws RemoteException {
    return null;
  }

  @Override
  public ServerHeader getServerHeader() throws RemoteException {
    return header;
  }
}
