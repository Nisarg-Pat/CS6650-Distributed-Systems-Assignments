package Q4;

import java.rmi.*;


public interface Sorter extends Remote {
  int[] sort(int[] arr) throws RemoteException;
}
