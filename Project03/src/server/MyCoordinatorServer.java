package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class MyCoordinatorServer extends UnicastRemoteObject implements CoordinatorServer {

  protected final int port;
  private final ServerHeader header;
  List<ServerHeader> serverList;

  protected MyCoordinatorServer(String host) throws RemoteException {
    super();
    this.port = CoordinatorServer.PORT;
    this.header = new ServerHeader(host, this.port);
    serverList = new ArrayList<>();
  }

  @Override
  public void addServer(ServerHeader server) throws RemoteException {
    serverList.add(server);
  }

  @Override
  public List<ServerHeader> getAllServers() throws RemoteException {
    return serverList;
  }

  @Override
  public void start() throws RemoteException {
    try {
      Registry registry = LocateRegistry.createRegistry(port);
      registry.rebind(SERVER_LIST_SERVICE, this);
      System.out.println("CoordinatorServer started at host: "+header.getHost()+", port: "+header.getPort());
    } catch (Exception e) {
      System.out.println("Trouble: " + e);
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
