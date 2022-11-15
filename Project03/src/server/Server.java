package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.Unreferenced;

/**
 * An instance of a Server.
 */
public interface Server extends Remote { //, Unreferenced {

  /**
   * Starts a server, listening at port based on the protocol it is created.
   *
   */
  void start() throws RemoteException;
}
