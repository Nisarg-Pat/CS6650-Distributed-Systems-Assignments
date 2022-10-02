package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends AbstractServer{
  ServerSocket serverSocket;

  public TCPServer(int port) throws IOException {
    super(port);
    serverSocket = new ServerSocket(port);
  }

  public void execute() throws IOException {
    System.out.println("TCPServer listening at port: " + port);
    System.out.println();
    while(true) {
      Socket socket = serverSocket.accept();
      System.out.println("Client connected. Address: "+socket.getInetAddress());
      System.out.println();
      BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      String input = null;
      try {
        while((input = br.readLine()) !=null) {
          System.out.println(createInputString(socket, input));
          String output = processRequest(input);
          out.println(output);
          out.flush();
        }
      } catch (Exception e) {
        System.out.println(e.toString());
      }
      System.out.println("Connection closed with"+socket.getInetAddress());
    }
  }
}
