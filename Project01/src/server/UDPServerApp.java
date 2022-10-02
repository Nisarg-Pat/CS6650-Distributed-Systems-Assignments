package server;

import java.io.IOException;

public class UDPServerApp extends AbstractServerApp {
  public static void main(String[] args) throws IOException {
    getPort(args);

    AbstractServer server = new UDPServer(port);
    server.execute();
  }
}
