package client;

import java.io.IOException;

public class TCPClientApp {

//  public static final int TCP_PORT = 1254;
//  public static final int UDP_PORT = 5421;

  public static void main(String[] args) throws IOException {
    String host;
    int port;

    if (args.length != 2) {
      System.out.println("Improper number of arguments!. Required 2 arguments: host port");
      return;
    }

    try {
      host = args[0];
      port = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      System.out.println(args[1] + " is not a valid port number!!");
      return;
    }

//    if(port != TCP_PORT && port != UDP_PORT) {
//      System.out.println(port+" is not a valid TCP/IP or UDP port for the server!");
//    }

    TCPClient client = new TCPClient(host, port);
    client.execute();

  }
}
