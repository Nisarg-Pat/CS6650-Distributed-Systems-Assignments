package Q1.client;

import java.io.IOException;
import java.util.Scanner;

/**
 * Visibility: Package Private
 * Abstract class for Client implementing common methods between both types of clients
 */
abstract class AbstractClient implements Client {

  protected final Scanner scanner;

  protected static final int TIMEOUT = 10000; //in milliseconds (= 10 sec)

  /**
   * Constructor for AbstractClient
   */
  public AbstractClient() {
    scanner = new Scanner(System.in);
  }

  public void execute() throws IOException {
    System.out.println("Type QUIT to logout\n");
    String input = "";
    while (true) {
      //Taking client request
      System.out.print("Command: ");
      input = scanner.nextLine();
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
