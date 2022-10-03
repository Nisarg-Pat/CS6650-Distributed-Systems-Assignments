package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import util.DataUtils;

public class UDPClient extends AbstractClient {

  DatagramSocket datagramSocket;
  InetAddress addr;
  int port;

  public UDPClient(InetAddress addr, int port) throws IOException {
    super();
    datagramSocket = new DatagramSocket();
    datagramSocket.setSoTimeout(TIMEOUT);
    this.addr = addr;
    this.port = port;
  }

  @Override
  public void request(String input) throws IOException {
    //byte[] buffer = DataUtils.encode(input).getBytes();
    byte[] buffer = input.getBytes();
    DatagramPacket request = new DatagramPacket(buffer, input.length(), addr, port);
    datagramSocket.send(request);

    buffer = new byte[1000];
    DatagramPacket response = new DatagramPacket(buffer, buffer.length);

    try {
      datagramSocket.receive(response);
    } catch (SocketTimeoutException e) {
      clientLog.logln("TIMEOUT\n");
      return;
    }

    String res = new String(response.getData()).substring(0, response.getLength());
    String output = getOutput(res);
    if (output.isEmpty()) {
      clientLog.logln("Malformed Response!");
      return;
    }
    //output = DataUtils.decode(output);
    clientLog.logln("Response: " + output + "\n");
  }
}
