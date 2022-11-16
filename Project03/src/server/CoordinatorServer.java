package server;

import java.rmi.RemoteException;
import java.util.List;

public interface CoordinatorServer extends Server {

  int PORT = 9999;
  String SERVER_LIST_SERVICE = "ServerListService";

  void addServer(ServerHeader server) throws RemoteException;

   List<ServerHeader> getAllServers() throws RemoteException;
}
