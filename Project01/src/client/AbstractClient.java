package client;

import java.io.IOException;
import java.net.Socket;
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
    while(!input.equals("QUIT")) {
      System.out.print("Command: ");
      input = scanner.nextLine();
      String[] data = input.split(" ");
      String command = data[0];
      switch (command) {
        case "PUT":
          if(checkPut(data)) {
            send(input);
          } else {
            System.out.println("Improper format for PUT. Required: PUT key value");
          }
          break;
        case "GET":
          if(checkGet(data)) {
            send(input);
          } else {
            System.out.println("Improper format for GET. Required: GET key");
          }
          break;
        case "DELETE":
          if(checkDelete(data)) {
            send(input);
          } else {
            System.out.println("Improper format for DELETE. Required: PUT key");
          }
          break;
        case "QUIT":
          break;
        default:
          System.out.println("Invalid command: "+command+"\nPossible commands: PUT/GET/DELETE/QUIT");
      }
    }
    scanner.close();
  }

  private boolean checkDelete(String[] data) {
    return data.length == 2;
  }

  private boolean checkGet(String[] data) {
    return data.length == 2;
  }

  private boolean checkPut(String[] data) {
    return data.length == 3;
  }

  public abstract void send(String input) throws IOException;
}
