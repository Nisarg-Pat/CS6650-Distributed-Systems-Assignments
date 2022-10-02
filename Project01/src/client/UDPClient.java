package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient extends AbstractClient {

  DatagramSocket datagramSocket;
  InetAddress addr;
  int port;

  public UDPClient(String host, int port) throws IOException {
    super();
    datagramSocket = new DatagramSocket();
    this.addr = InetAddress.getByName(host);
    this.port = port;
  }

  @Override
  public void send(String input) throws IOException {
    byte[] buffer = input.getBytes();
    DatagramPacket request = new DatagramPacket(buffer, input.length(), addr, port);
    datagramSocket.send(request);
  }
}
