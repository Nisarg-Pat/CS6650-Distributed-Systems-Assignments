package client;

import java.io.IOException;

public class TCPClientApp extends AbstractClientApp {

  public static void main(String[] args) throws IOException {
    getHostAndPort(args);
    client = new TCPClient(host, port);
    client.execute();
  }


}
