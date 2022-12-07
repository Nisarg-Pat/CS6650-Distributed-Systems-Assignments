package server;

import server.paxos.PaxosServer;

import java.rmi.RemoteException;
import java.util.List;

/**
 * A coordinator server that has the details of all the servers running in the application.
 */
public interface CoordinatorServer extends Server {

  //Coordinator server runs in specific port: 9999
  int PORT = 9999;
  String SERVER_LIST_SERVICE = "ServerListService";

  /**
   * Adds a server to the coordinator
   * @param server The details of the server
   * @throws RemoteException
   */
  void addServer(Server server) throws RemoteException;

  /**
   * Return the list of all the servers for the application.
   * @return The list of servers.
   * @throws RemoteException
   */
   List<Server> getAllServers() throws RemoteException;
}
