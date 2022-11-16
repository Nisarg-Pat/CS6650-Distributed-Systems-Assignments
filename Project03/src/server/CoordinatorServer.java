package server;

import java.rmi.RemoteException;
import java.util.List;

public interface CoordinatorServer extends Server {

  public static final int PORT = 9999;
  public static final String SERVER_LIST_SERVICE = "ServerListService";

  void addServer(ServerHeader server) throws RemoteException;

   List<ServerHeader> getAllServers() throws RemoteException;
}
