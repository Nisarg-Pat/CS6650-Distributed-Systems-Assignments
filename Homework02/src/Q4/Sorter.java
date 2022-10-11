package Q4;

import java.rmi.*;


public interface Sorter extends Remote {
  public int[] sort(int[] arr) throws RemoteException;
}
