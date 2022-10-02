package server;

import java.io.IOException;

public class TCPServerApp extends AbstractServerApp {
  public static void main(String[] args) throws IOException {
    getPort(args);

    AbstractServer server = new TCPServer(port);
    server.execute();
  }
}
