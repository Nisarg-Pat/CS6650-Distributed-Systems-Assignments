package client;

import java.io.IOException;
import java.util.Scanner;

public abstract class AbstractClient {

  private final Scanner scanner;

  public static final int TIMEOUT = 10000; //in milliseconds (= 10 sec)

  public ClientLog clientLog;

  public AbstractClient() throws IOException {
    scanner = new Scanner(System.in);
    clientLog = new ClientLog();
  }

  public void execute() throws IOException {
    clientLog.logln("Possible commands: PUT/GET/DELETE/QUIT");
    String input = "";
    while (true) {
      clientLog.log("Command: ");
      input = scanner.nextLine();
      clientLog.logOnly(input);
      if (input.equals("QUIT")) {
        break;
      }
      request(input);
    }
  }

  public abstract void request(String input) throws IOException;

  public String getOutput(String response) {
    if(response.startsWith("RESPONSE(") && response.endsWith(")")) {
      return response.substring(9, response.length()-1);
    }
    return "";
  }
}
