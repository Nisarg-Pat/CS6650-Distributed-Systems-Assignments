package server;

public abstract class AbstractServerApp {
  public static int port;

  public static void getPort(String[] args) {
    if (args.length != 1) {
      System.out.println("Improper number of arguments!. Required 1 argument: port");
      return;
    }

    try {
      port = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      System.out.println(args[0] + " is not a valid port number!!");
    }
  }
}
