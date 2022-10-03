package client;

import java.io.IOException;
import java.util.Scanner;

public abstract class AbstractClient {

  private final Scanner scanner;

  public static final int TIMEOUT = 10000; //in milliseconds (= 10 sec)

  public AbstractClient() throws IOException {
    scanner = new Scanner(System.in);
  }

  public void execute() throws IOException {
    System.out.println("Possible commands: PUT/GET/DELETE/QUIT");
    String input = "";
    while (true) {
      System.out.print("Command: ");
      input = scanner.nextLine();
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
