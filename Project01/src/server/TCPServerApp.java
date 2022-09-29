package server;

import java.io.IOException;

public class TCPServerApp {
  public static void main(String[] args) throws IOException {
    int port;

    if (args.length != 1) {
      System.out.println("Improper number of arguments!. Required 1 argument: port");
      return;
    }

    try {
      port = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      System.out.println(args[0] + " is not a valid port number!!");
      return;
    }

    TCPServer server = new TCPServer(port);
    server.execute();
  }
}
