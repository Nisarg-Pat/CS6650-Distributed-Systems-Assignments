package Q1.server;

import java.io.IOException;
import java.net.InetAddress;

import Q1.util.DataUtils;

/**
 * Visibility: Package Private
 * Abstract class for Server implementing common methods between both types of servers
 */
abstract class AbstractServer implements Server {

  protected final int port;

  /**
   * Constructor for AbstractServer
   *
   * @param port port at which the server should listen
   * @throws IOException
   */
  AbstractServer(int port) throws IOException {
    this.port = port;
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
    return reverseAndChange(input);
  }

  public static String reverseAndChange(String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) >= 'A' && s.charAt(i) <= 'Z') {
        sb.append((char) (s.charAt(i) - 'A' + 'a'));
      } else if (s.charAt(i) >= 'a' && s.charAt(i) <= 'z') {
        sb.append((char) (s.charAt(i) - 'a' + 'A'));
      } else {
        sb.append(s.charAt(i));
      }
    }
    return sb.reverse().toString();
  }
}