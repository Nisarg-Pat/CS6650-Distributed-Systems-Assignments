package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.Unreferenced;
import java.util.List;
import java.util.Map;

/**
 * An instance of a Server.
 */
public interface Server extends Remote { //, Unreferenced {

  /**
   * Starts a server, listening at port based on the protocol it is created.
   *
   */
  void start() throws RemoteException;

//  /**
//   * Gives a deep copy of the database object
//   * @return the deep copy of the database object
//   */
//  MyKeyValueDB getDBCopy() throws RemoteException;

  /**
   * Gives the server header including the host and port of the server
   * @return the server header
   */
  ServerHeader getServerHeader() throws RemoteException;
}
