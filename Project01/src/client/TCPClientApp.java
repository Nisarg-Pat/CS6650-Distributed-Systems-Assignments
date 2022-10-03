package client;

import java.io.IOException;

import util.DataUtils;

public class TCPClientApp{

  public static String host;
  public static int port;
  public static AbstractClient client;

  public static void main(String[] args) throws IOException {
    DataUtils.validateClientArguments(args);
    host = args[0];
    port = Integer.parseInt(args[1]);
    client = new TCPClient(host, port);
    client.execute();
  }
}
