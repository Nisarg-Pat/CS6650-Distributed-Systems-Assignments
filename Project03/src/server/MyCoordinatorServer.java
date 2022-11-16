package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class MyCoordinatorServer extends UnicastRemoteObject implements CoordinatorServer{

  protected final int port;
  List<Server> serverList;

  protected MyCoordinatorServer(int port) throws RemoteException {
    super(port);
    this.port = port;
    serverList = new ArrayList<>();
  }

  @Override
  public void addServer(Server server) throws RemoteException {
    serverList.add(server);
  }

  @Override
  public List<Server> getAllServers() throws RemoteException {
    return serverList;
  }

  @Override
  public void start() throws RemoteException {
    try {
      Registry registry = LocateRegistry.createRegistry(port);
      registry.rebind(SERVER_LIST_SERVICE, this);
      System.out.println("CoordinatorServer started");
    } catch (Exception e) {
      System.out.println("Trouble: " + e);
    }
  }

  @Override
  public boolean canCommit(Transaction transaction) throws RemoteException {
    return false;
  }

  @Override
  public List<Object> doCommit(Transaction transaction) throws RemoteException {
    return null;
  }

  @Override
  public List<Object> doAbort(Transaction transaction) throws RemoteException {
    return null;
  }

  @Override
  public boolean haveCommitted(Transaction transaction) throws RemoteException {
    return false;
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
}
