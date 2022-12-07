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


  Set<Server> serverSet;
  private Server tempServer;
  boolean foundServer;

  private static final int GET_TIMEOUT = 1000;

  protected MyCoordinatorServer(String host) throws RemoteException {
    super();
    this.port = CoordinatorServer.PORT;
    this.header = new ServerHeader(host, this.port);
    serverSet = new HashSet<>();
  }

  @Override
  public void addServer(Server server) throws RemoteException {
    serverSet.add(server);
  }

  @Override
  public List<Server> getAllServers() throws RemoteException {
    //Temporary set of not accessible servers
    //To get accurate list of running servers
    Set<Server> removedSet = new HashSet<>();
    for(Server server: serverSet) {
        foundServer = false;
        Thread lookupTimeOut = new Thread(() -> {
          try {
            server.getServerHeader();
            foundServer = true;
          } catch (RemoteException e) {
            foundServer = false;
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
        if(!foundServer) {
          //If unable to find the server, remove it
          removedSet.add(server);
        }
    }
    for(Server server: removedSet) {
      //Removing the servers
      serverSet.remove(server);
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
  public ServerHeader getServerHeader() throws RemoteException {
    return header;
  }
}
