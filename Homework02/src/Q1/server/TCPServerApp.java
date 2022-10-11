package Q1.server;

import java.io.IOException;

import Q1.util.DataUtils;

/**
 * TCP Server Application
 */
public class TCPServerApp {

  /**
   * Main Method to start UDP Client
   *
   * @param args Required args: <port>
   */
  public static void main(String[] args) {
    //Validates the command line arguments
    DataUtils.validateServerArguments(args);

    String type = args[0];
    int port = Integer.parseInt(args[1]);

    //Creating a TCP server and calling start
    try {
      if(type.equals("single")) {
        Server server = new SingleThreadedTCPServer(port);
        server.start();
      } else if(type.equals("multi")) {
        Server server = new MultiThreadedTCPServer(port);
        server.start();
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }

  }
}
