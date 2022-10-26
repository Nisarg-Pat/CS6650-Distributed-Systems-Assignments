package server;

import util.DataUtils;

import java.io.IOException;

/**
 * UDP Server Application
 */
public class UDPServerApp {

  /**
   * Main Method to start UDP Client
   *
   * @param args Required args: <port>
   */
  public static void main(String[] args) {
    //Validates the command line arguments
    DataUtils.validateServerArguments(args);

    int port = Integer.parseInt(args[0]);

    //Creating a UDP Server and calling start
    try {
      AbstractServer server = new UDPServer(port);
      server.start();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
