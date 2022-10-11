package Q4;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class SortingServer {

  public static void main(String[] args) {
    try {
      Sorter sorter = new SorterImpl();
      Sorter stubSorter = (Sorter) UnicastRemoteObject.exportObject(sorter, 0);
      Registry registry = LocateRegistry.getRegistry();
      registry.bind("SortingService", sorter);
    } catch (Exception e) {
      System.out.println("Trouble: " + e);
    }
  }
}
