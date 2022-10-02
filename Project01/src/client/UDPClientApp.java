package client;

import java.io.IOException;

public class UDPClientApp extends AbstractClientApp{

  public static void main(String[] args) throws IOException {
    getHostAndPort(args);
    client = new UDPClient(host, port);
    client.execute();
  }
}
