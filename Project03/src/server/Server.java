package server;

/**
 * An instance of a Server.
 */
public interface Server {

  /**
   * Starts a server, listening at port based on the protocol it is created.
   *
   */
  void start();
}
