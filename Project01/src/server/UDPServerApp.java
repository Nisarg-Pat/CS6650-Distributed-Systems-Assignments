package server;

import java.io.IOException;

import util.DataUtils;

/**
 * UDP Server Application
 */
public class UDPServerApp{

  public static int port;

  /**
   * Main Method to start UDP Client
   * @param args Required args: <port>
   */
  public static void main(String[] args){
    //Validates the command line arguments
    DataUtils.validateServerArguments(args);

    port = Integer.parseInt(args[0]);

    //Creating a UDP Server and calling start
    try {
      AbstractServer server = new UDPServer(port);
      server.start();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
