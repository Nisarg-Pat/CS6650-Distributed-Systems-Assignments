package server;

import java.io.Serializable;

/**
 * A header for Server that includes the details of it.
 */
public class ServerHeader implements Serializable {
  private final String host;
  private final int port;

  /**
   * Constructor for header
   * @param host host name of the server
   * @param port port of the server
   */
  public ServerHeader(String host, int port) {
    this.host = host;
    this.port = port;
  }

  /**
   * Returns the host of the server
   * @return the host
   */
  public String getHost() {
    return host;
  }

  /**
   * Returns the port of the server
   * @return the port
   */
  public int getPort() {
    return port;
  }
}
