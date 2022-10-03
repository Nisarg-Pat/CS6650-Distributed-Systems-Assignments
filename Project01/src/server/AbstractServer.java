package server;

import java.io.IOException;
import java.net.InetAddress;

import util.DataUtils;
import util.Log;

/**
 * Visibility: Package Private
 * Abstract class for Server implementing common methods between both types of servers
 */
abstract class AbstractServer implements Server {

  protected final int port;
  protected final KeyValueDB db;

  protected final Log serverLog;

  /**
   * Constructor for AbstractServer
   *
   * @param port port at which the server should listen
   * @throws IOException
   */
  AbstractServer(int port) throws IOException {
    this.port = port;
    this.db = new KeyValueDB();
    this.db.populate();
    this.serverLog = new Log();
  }

  @Override
  public String createInputString(InetAddress addr, int port, String input) {
    StringBuffer sb = new StringBuffer();
    sb.append("Request Received. ").append(getAddress(addr, port)).append("\n")
            .append(DataUtils.getCurrentTime()).append(": Request: ").append(input);
    return sb.toString();
  }

  @Override
  public String getAddress(InetAddress addr, int port) {
    return String.format("InetAddress: %s:%s", addr, port);
  }

  @Override
  public String getOutput(String input) {
    StringBuilder sb = new StringBuilder();
    sb.append("RESPONSE").append("(");
    sb.append(processRequest(input)).append(")");
    return sb.toString();
  }

  @Override
  public String processRequest(String input) {
    if (input.equals("ALL")) {
      //Gives list of all key-value pair in the database
      //Automatically called initially by the client
      return "Database Content(Key, Value):\n" + db.toString();
    }
    String[] data = input.split(" ");
    String command = data[0];
    switch (command) {
      case "PUT":
        if (checkPut(data)) {
          String key = data[1];
          String value = data[2];
          boolean res = db.put(key, value);
          if (res) {
            //PUT successful
            return String.format("(%s, %s) added successfully", key, value);
          } else {
            //PUT failed. It can be because of blank or improper key or value.
            return String.format("Cannot put (%s, %s) in database. Please check the key and value.", key, value);
          }
        } else {
          //Improper arguments after PUT
          return "Invalid format for PUT. Expected: PUT <key> <value>";
        }
      case "GET":
        if (checkGet(data)) {
          String key = data[1];
          String res = db.get(key);
          if (res.equals("")) {
            //GET failed. It can occur if the key is not present in the database.
            return key + " is not present in database.";
          } else {
            //GET successful
            return res;
          }
        } else {
          //Improper arguments after GET
          return "Invalid format for GET. Expected: GET <key>";
        }
      case "DELETE":
        if (checkDelete(data)) {
          String key = data[1];
          boolean res = db.delete(key);
          if (res) {
            //DELETE successful
            return "Successfully deleted " + key;
          } else {
            //DELETE failed. It can occur if the key is not present in the database.
            return key + " is not present in database";
          }
        } else {
          //Improper arguments after DELETE
          return "Invalid format for DELETE. Expected: DELETE <key>";
        }
      default:
        //Improper request from client or command not recognized
        return "Received malformed request from client!!";
    }
  }

  /**
   * Checks the validity of DELETE command. Required: DELETE <key>
   *
   * @param data DELETE command
   * @return true if DELETE command is of valid format
   */
  private boolean checkDelete(String[] data) {
    return data.length == 2;
  }

  /**
   * Checks the validity of GET command. Required: GET <key>
   *
   * @param data GET command
   * @return true if GET command is of valid format
   */
  private boolean checkGet(String[] data) {
    return data.length == 2;
  }

  /**
   * Checks the validity of PUT command. Required: PUT <key> <value>
   *
   * @param data PUT command
   * @return true if PUT command is of valid format
   */
  private boolean checkPut(String[] data) {
    return data.length == 3;
  }
}
