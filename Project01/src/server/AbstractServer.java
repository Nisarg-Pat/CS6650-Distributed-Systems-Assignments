package server;

import java.io.IOException;
import java.net.ServerSocket;

public abstract class AbstractServer {

  int port;

  AbstractServer(int port) {
    this.port = port;
  }

  public abstract void execute() throws IOException;


}
