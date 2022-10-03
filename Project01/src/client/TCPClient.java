package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient extends AbstractClient {

  Socket socket;

  public TCPClient(InetAddress address, int port) throws IOException {
    super();
    this.socket = new Socket(address, port);
  }

  @Override
  public void send(String input) throws IOException {
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    out.println(input);
    out.flush();

    String temp = br.readLine();
    System.out.println("Server Response: " + temp);
    System.out.println();
  }
}
