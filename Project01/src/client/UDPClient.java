package client;

import java.io.IOException;

public class UDPClient extends AbstractClient {
  public UDPClient(String host, int port) throws IOException {
    super(host, port);
  }

  @Override
  public void send(String input) throws IOException {

  }
}
