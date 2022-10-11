package Q4;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class SortingClient {

  public static Sorter sorter;

  public static void main(String[] args) {
    try {
      sorter = (Sorter) Naming.lookup("rmi://localhost:1099/SortingService");
      Scanner sc = new Scanner(System.in);
      System.out.print("Enter the number of integers: ");
      int n = sc.nextInt();
      int[] arr = new int[n];
      System.out.println("Enter "+n+" Integers(Format: a0 a1 a2 .. an): ");
      for(int i = 0;i<n;i++) {
        arr[i] = sc.nextInt();
      }
      System.out.println("Sorted Order: ");
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
