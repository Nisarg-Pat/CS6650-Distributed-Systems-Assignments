package client;

import java.io.IOException;
import java.util.Scanner;

import util.Log;

/**
 * Visibility: Package Private
 * Abstract class for Client implementing common methods between both types of clients
 */
abstract class AbstractClient implements Client{

  protected final Scanner scanner;

  protected static final int TIMEOUT = 10000; //in milliseconds (= 10 sec)

  protected final Log clientLog;

  /**
   * Constructor for AbstractClient
   */
  public AbstractClient() {
    scanner = new Scanner(System.in);
    clientLog = new Log();
  }

  public void execute() throws IOException {
    request("ALL");
    clientLog.logln("Possible commands: PUT/GET/DELETE/QUIT\n");
    String input = "";
    while (true) {
      //Taking client request
      clientLog.log("Command: ");
      input = scanner.nextLine();
      clientLog.logOnly(input);
      if (input.equals("QUIT")) {
        break;
      }
      //Requesting to the server
      request(input);
    }
  }

  public String getOutput(String response) {
    if (response.startsWith("RESPONSE(") && response.endsWith(")")) {
      return response.substring(9, response.length() - 1);
    }
    return "";
  }
}
