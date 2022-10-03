package client;

import java.io.IOException;
import java.net.InetAddress;

import util.DataUtils;

/**
 * UDP Client Application
 */
public class UDPClientApp {

  /**
   * Main Method to start UDP Client
   *
   * @param args Required args: <hostname or IP> <port>
   */
  public static void main(String[] args) {

    //Validates the command line arguments
    DataUtils.validateClientArguments(args);

    try {
      InetAddress address = InetAddress.getByName(args[0]);
      int port = Integer.parseInt(args[1]);

      //Creating a UDP Client and calling execute
      Client client = new UDPClient(address, port);
      client.execute();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }

  }
}
