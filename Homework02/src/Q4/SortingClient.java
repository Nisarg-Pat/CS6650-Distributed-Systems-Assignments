package Q4;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SortingClient {
  public static void main(String[] args) {
    try {
      Sorter sorter = (Sorter) Naming.lookup("rmi://localhost:1099/SortingService");
      int[] arr = new int[]{3, 5, 8, 6, 7, 9, 4, 2, 1};
      printArray(sorter.sort(arr));
    } catch (RemoteException re) {
      System.out.println(
              "RemoteException");
      System.out.println(re.getMessage());
    } catch (NotBoundException nbe) {
      System.out.println(
              "NotBoundException");
      System.out.println(nbe.getMessage());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  private static void printArray(int[] arr) {
    for (int i : arr) {
      System.out.print(i + " ");
    }
    System.out.println();
  }
}
