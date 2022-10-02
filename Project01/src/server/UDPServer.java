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

public class UDPServer extends AbstractServer{

  DatagramSocket datagramSocket;

  UDPServer(int port) throws SocketException {
    super(port);
    datagramSocket = new DatagramSocket(port);
  }

  @Override
  public void execute() throws IOException {
    System.out.println("UDPServer listening at port: " + port);
    System.out.println();
    while (true) {
      byte[] buffer = new byte[1000];
      DatagramPacket request = new DatagramPacket(buffer, buffer.length);
      datagramSocket.receive(request);
      String input = new String(request.getData()).substring(0, request.getLength());

      System.out.println(createInputString(request.getAddress(), request.getPort(), input));
      String output = processRequest(input);
      System.out.println("Response: " + output);
      System.out.println();

      byte[] outputByte = output.getBytes();
      DatagramPacket reply = new DatagramPacket(outputByte, output.length(), request.getAddress(), request.getPort());
      datagramSocket.send(reply);
    }
  }
}
