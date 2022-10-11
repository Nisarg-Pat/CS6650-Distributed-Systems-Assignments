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

    if (args.length != 2) {
      System.out.println("Improper number of arguments! Required 2 arguments: host port");
      return;
    }

    String host = args[0];
    int port;

    try {
      port = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      System.out.println(args[1] + " is not a valid port number!!");
      return;
    }

    try {
      Registry registry = LocateRegistry.getRegistry(host, port);
      sorter = (Sorter) registry.lookup("SortingService");
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
    }
  }

  private static void printArray(int[] arr) {
    for (int i : arr) {
      System.out.print(i + " ");
    }
    System.out.println();
  }
}
