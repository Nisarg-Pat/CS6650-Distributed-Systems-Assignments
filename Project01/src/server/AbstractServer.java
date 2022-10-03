package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import util.DataUtils;
import util.Log;

public abstract class AbstractServer {

  int port;
  KeyValueDB db;

  public Log serverLog;

  AbstractServer(int port) throws IOException {
    this.port = port;
    this.db = new KeyValueDB();
    this.db.populate();
    this.serverLog = new Log();
  }

  public abstract void execute() throws IOException;

  public String createInputString(InetAddress addr, int port, String input) {
    StringBuffer sb = new StringBuffer();
    sb.append("Request Received. ").append(getAddress(addr, port)).append("\n")
            .append(DataUtils.getCurrentTime()).append(": Request: ").append(input);
    return sb.toString();
  }

  public String getAddress(InetAddress addr, int port) {
    return String.format("InetAddress: %s:%s", addr, port);
  }

  public String getOutput(String input) {
    StringBuilder sb = new StringBuilder();
    sb.append("RESPONSE").append("(");
    sb.append(processRequest(input)).append(")");
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
            return String.format("Cannot put (%s, %s) in database. Please check the key and value.", key, value);
          }
        } else {
          return "Invalid format for PUT. Expected: PUT <key> <value>";
        }
      case "GET":
        if (checkGet(data)) {
          String key = data[1];
          String res = db.get(key);
          if (res.equals("")) {
            return key + " is not present in database.";
          } else {
            return res;
          }
        } else {
          return "Invalid format for GET. Expected: GET <key>";
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
          return "Invalid format for DELETE. Expected: DELETE <key>";
        }
      default:
        return "Received malformed request from client!!";
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
