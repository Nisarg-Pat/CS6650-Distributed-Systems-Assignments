package client;

import java.io.IOException;
import java.net.InetAddress;

import util.DataUtils;

/**
 * TCP Client Application
 */
public class TCPClientApp {

  /**
   * Main Method to start TCP Client
   * @param args Required args: <hostname or IP> <port>
   */
  public static void main(String[] args) {

    //Validates the command line arguments
    DataUtils.validateClientArguments(args);

    try {
      InetAddress address = InetAddress.getByName(args[0]);
      int port = Integer.parseInt(args[1]);

      //Creating a TCP Client and calling execute
      AbstractClient client = new TCPClient(address, port);
      client.execute();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
