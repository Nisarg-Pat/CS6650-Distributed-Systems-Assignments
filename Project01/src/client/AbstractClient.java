package client;

import java.io.IOException;
import java.util.Scanner;

import util.Log;

/**
 * Abstract class for Client implementing common methods between both types of clients
 */
public abstract class AbstractClient {

  private final Scanner scanner;

  public static final int TIMEOUT = 10000; //in milliseconds (= 10 sec)

  public Log clientLog;

  /**
   * Constructor for AbstractClient
   */
  public AbstractClient() {
    scanner = new Scanner(System.in);
    clientLog = new Log();
  }

  /**
   * Starts the interactive client. Requests are typed in Command Line. Following are the types of requests:
   * PUT <key> <value> - Puts a key-value pair
   * GET <key> - Gets the value for the key if present
   * DELETE <key> - Deletes the key
   * QUIT - Closes the client
   *
   * @throws IOException
   */
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

  /**
   * Requests a response from the server for an input
   *
   * @param input Request input string
   * @throws IOException
   */
  public abstract void request(String input) throws IOException;

  /**
   * Gets the output from the Response.
   * Response is of the form: RESPONSE(<output>)
   *
   * @param response The response string from the server
   * @return The <output> from the response
   */
  public String getOutput(String response) {
    if (response.startsWith("RESPONSE(") && response.endsWith(")")) {
      return response.substring(9, response.length() - 1);
    }
    return "";
  }
}
