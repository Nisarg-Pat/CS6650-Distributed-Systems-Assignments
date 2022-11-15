package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * An instance of a Server.
 */
public interface Server extends Remote {

  /**
   * Starts a server, listening at port based on the protocol it is created.
   *
   */
  void start() throws RemoteException;
}
