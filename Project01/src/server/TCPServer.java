package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends AbstractServer {
  ServerSocket serverSocket;

  public TCPServer(int port) throws IOException {
    super(port);
    serverSocket = new ServerSocket(port);
  }

  public void execute() throws IOException {
    System.out.println("TCPServer listening at port: " + port);
    System.out.println();
    while (true) {
      Socket socket = serverSocket.accept();
      System.out.println("Client connected. " + getAddress(socket.getInetAddress(), socket.getPort()));
      System.out.println();
      BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      String input = null;
      try {
        while ((input = br.readLine()) != null) {
          System.out.println(createInputString(socket.getInetAddress(), socket.getPort(), input));
          String output = getOutput(input);
          System.out.println(output);
          System.out.println();
          out.println(output);
          out.flush();
        }
      } catch (Exception e) {
        //Empty catch
      }
      System.out.println("Connection closed with " + socket.getInetAddress());
      System.out.println();
    }
  }
}
