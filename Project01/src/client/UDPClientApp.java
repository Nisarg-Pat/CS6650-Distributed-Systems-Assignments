package client;

import java.io.IOException;
import java.net.InetAddress;

import util.DataUtils;

public class UDPClientApp {

  public static void main(String[] args){
    DataUtils.validateClientArguments(args);

    try {
      InetAddress address = InetAddress.getByName(args[0]);
      int port = Integer.parseInt(args[1]);

      AbstractClient client = new UDPClient(address, port);
      client.execute();
    } catch (IOException e) {
      System.out.println(e);
    }

  }
}
