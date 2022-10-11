package Q4;

import java.rmi.Naming;

public class SortingServer {

  public static void main(String[] args) {
    try {
      Sorter c = new SorterImpl();
      Naming.rebind("rmi://localhost:1234/SortingService", c);
    } catch (Exception e) {
      System.out.println("Trouble: " + e);
    }
  }
}
