package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

import util.DataUtils;

/**
 * Access: pakage-protected
 * Class for UDP Server
 */
class UDPServer extends AbstractServer{

  DatagramSocket datagramSocket;

  /**
   * Constructor for UDP Server
   * @param port Port at which server should listen
   * @throws IOException
   */
  UDPServer(int port) throws IOException {
    super(port);
    datagramSocket = new DatagramSocket(port);

    //Creating a log file
    serverLog.createFile("UCPServerLog.txt");
  }

  @Override
  public void start() throws IOException {
    serverLog.logln("UDPServer listening at port: " + port+ "\n");
    while (true) {

      //Reads request from the client
      byte[] buffer = new byte[1000];
      DatagramPacket request = new DatagramPacket(buffer, buffer.length);
      datagramSocket.receive(request);
      String input = new String(request.getData()).substring(0, request.getLength());
      //input = DataUtils.decode(input);

      //Processes the request
      serverLog.logln(createInputString(request.getAddress(), request.getPort(), input));
      String output = getOutput(input);
      serverLog.logln(output+"\n");

      //Sends response back to client
      //byte[] outputByte = DataUtils.encode(output).getBytes();
      byte[] outputByte = output.getBytes();
      DatagramPacket reply = new DatagramPacket(outputByte, output.length(), request.getAddress(), request.getPort());
      datagramSocket.send(reply);
    }
  }
}
