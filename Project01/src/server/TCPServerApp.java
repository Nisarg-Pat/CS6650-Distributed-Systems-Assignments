package server;

import java.io.IOException;

import util.DataUtils;

/**
 * TCP Server Application
 */
public class TCPServerApp{

  /**
   * Main Method to start UDP Client
   * @param args Required args: <port>
   */
  public static void main(String[] args){
    //Validates the command line arguments
    DataUtils.validateServerArguments(args);

    int port = Integer.parseInt(args[0]);

    //Creating a TCP server and calling start
    try {
      AbstractServer server = new TCPServer(port);
      server.start();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }

  }
}
