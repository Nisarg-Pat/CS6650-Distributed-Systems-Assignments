package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import util.DataUtils;

public class UDPClient extends AbstractClient {

  DatagramSocket datagramSocket;
  InetAddress addr;
  int port;

  public UDPClient(InetAddress addr, int port) throws IOException {
    super();
    datagramSocket = new DatagramSocket();
    this.addr = addr;
    this.port = port;
  }

  @Override
  public void send(String input) throws IOException {
    //byte[] buffer = DataUtils.encode(input).getBytes();
    byte[] buffer = input.getBytes();
    DatagramPacket request = new DatagramPacket(buffer, input.length(), addr, port);
    datagramSocket.send(request);

    buffer = new byte[1000];
    DatagramPacket response = new DatagramPacket(buffer, buffer.length);
    datagramSocket.receive(response);
    String output = new String(response.getData()).substring(0, response.getLength());
    //output = DataUtils.decode(output);
    System.out.println("Server Response: " + output);
    System.out.println();
  }
}
