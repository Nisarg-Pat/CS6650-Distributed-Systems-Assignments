package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import util.DataUtils;

/**
 * Access: Package-Protected
 * Class for UDP Client
 */
public class UDPClient extends AbstractClient {

  private final DatagramSocket datagramSocket;
  private final InetAddress addr;
  private final int port;

  /**
   * Constructor for UDP Client
   * @param addr InetAddress of the server
   * @param port server port
   * @throws IOException
   */
  public UDPClient(InetAddress addr, int port) throws IOException {
    super();
    datagramSocket = new DatagramSocket();

    //Setting timeout of the connection
    datagramSocket.setSoTimeout(TIMEOUT);

    this.addr = addr;
    this.port = port;

    //Creating a log file
    clientLog.createFile("UDPClientLog.txt");
  }

  @Override
  public void request(String input) throws IOException {
    //byte[] buffer = DataUtils.encode(input).getBytes();

    //Sending Request to the Server using DatagramPacket
    byte[] buffer = input.getBytes();
    DatagramPacket request = new DatagramPacket(buffer, input.length(), addr, port);
    datagramSocket.send(request);

    //Receiving response from the server
    buffer = new byte[1000];
    DatagramPacket response = new DatagramPacket(buffer, buffer.length);

    try {
      datagramSocket.receive(response);
    } catch (SocketTimeoutException e) {
      //Checking for timeout
      clientLog.logln("Server Timeout! Please try requesting again\n");
      return;
    }

    //Getting output from the response
    String res = new String(response.getData()).substring(0, response.getLength());
    String output = getOutput(res);
    if (output.isEmpty()) {
      clientLog.logln(String.format("Received unsolicited response of length %d from server %s:%s\n",
              response.getLength(), response.getAddress(), response.getPort()));
      return;
    }
    //output = DataUtils.decode(output);
    clientLog.logln("Response: " + output + "\n");
  }
}
