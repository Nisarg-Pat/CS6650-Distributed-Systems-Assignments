package server;

import java.io.IOException;

import util.DataUtils;

public class TCPServerApp{

  public static int port;

  public static void main(String[] args) throws IOException {
    DataUtils.validateServerArguments(args);

    port = Integer.parseInt(args[0]);

    AbstractServer server = new TCPServer(port);
    server.execute();
  }
}
