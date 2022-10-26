package server;

import java.io.IOException;
import java.net.InetAddress;

public interface Server {

  /**
   * Starts a server, listening at port based on the protocol it is created.
   *
   * @throws IOException
   */
  void start() throws IOException;
}
