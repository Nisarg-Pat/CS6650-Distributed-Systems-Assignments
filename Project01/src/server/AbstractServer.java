package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer {

  int port;
  KeyValueDB db;

  AbstractServer(int port) {
    this.port = port;
    this.db = new KeyValueDB();
  }

  public abstract void execute() throws IOException;

  public String createInputString(Socket socket, String input) {
    StringBuffer sb = new StringBuffer();
    sb.append("Request Received. InetAddress: ").append(socket.getInetAddress())
            .append(" Port: ").append(socket.getPort())
            .append("\nRequest: ").append(input);
    return sb.toString();
  }

  public String processRequest(String input) {
    String[] data = input.split(" ");
    String command = data[0];
    switch (command) {
      case "PUT":
        if (checkPut(data)) {
          String key = data[1];
          String value = data[2];
          boolean res = db.put(key, value);
          if (res) {
            return String.format("(%s, %s) added successfully", key, value);
          } else {
            return String.format("Cannot put (%s, %s) in database.", key, value);
          }
        } else {
          return "Invalid format for PUT. Expected: PUT key value";
        }
      case "GET":
        if (checkGet(data)) {
          String key = data[1];
          String res = db.get(key);
          if (res.equals("")) {
            return key + " is not present in database";
          } else {
            return res;
          }
        } else {
          return "Invalid format for GET. Expected: GET key";
        }
      case "DELETE":
        if (checkDelete(data)) {
          String key = data[1];
          boolean res = db.delete(key);
          if (res) {
            return "Successfully deleted "+key;
          } else {
            return key + " is not present in database";
          }
        } else {
          return "Invalid format for DELETE. Expected: DELETE key";
        }
      default:
        return "Invalid command!";
    }
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

}
