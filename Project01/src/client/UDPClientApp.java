package client;

import java.io.IOException;

import util.DataUtils;

public class UDPClientApp{

  public static String host;
  public static int port;
  public static AbstractClient client;

  public static void main(String[] args) throws IOException {
    DataUtils.validateClientArguments(args);
    host = args[0];
    port = Integer.parseInt(args[1]);
    client = new UDPClient(host, port);
    client.execute();
  }
}
