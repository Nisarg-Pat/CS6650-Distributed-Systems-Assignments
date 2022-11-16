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

  boolean canCommit(Transaction transaction) throws RemoteException;

  List<Object> doCommit(Transaction transaction) throws RemoteException;

  List<Object> doAbort(Transaction transaction) throws RemoteException;

  boolean haveCommitted(Transaction transaction) throws RemoteException;

  boolean getDecision(Transaction transaction) throws RemoteException;

  List<Object> performTransaction(Transaction transaction) throws RemoteException;

  MyKeyValueDB getDBCopy() throws RemoteException;
}
