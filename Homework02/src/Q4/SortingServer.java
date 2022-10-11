package Q4;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class SortingServer {

  public static void main(String[] args) {

    if (args.length != 1) {
      System.out.println("Improper number of arguments! Required 1 argument: port");
      return;
    }

    int port;

    try {
      port = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      System.out.println(args[0] + " is not a valid port number!!");
      return;
    }

    try {
      SorterImpl sorter = new SorterImpl();
      Registry registry = LocateRegistry.getRegistry(port);
      registry.rebind("SortingService", sorter);
    } catch (Exception e) {
      System.out.println("Trouble: " + e);
    }
  }
}
