package client;

import java.io.IOException;

public abstract class AbstractClientApp {

  public static String host;
  public static int port;
  public static AbstractClient client;

  public static void getHostAndPort(String[] args) {
    if (args.length != 2) {
      System.out.println("Improper number of arguments!. Required 2 arguments: host port");
      return;
    }

    try {
      host = args[0];
      port = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      System.out.println(args[1] + " is not a valid port number!!");
    }
  }
}
