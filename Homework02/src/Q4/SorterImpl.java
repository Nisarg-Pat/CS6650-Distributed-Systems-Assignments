package Q4;

import java.rmi.RemoteException;
import java.util.Arrays;

public class SorterImpl implements Sorter {
  @Override
  public int[] sort(int[] arr) throws RemoteException {
    Arrays.sort(arr);
    return arr;
  }
}
