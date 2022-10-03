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
    serverLog.createFile("TCPServerLog.txt");
  }

  public void execute() throws IOException {
    serverLog.logln("TCPServer listening at port: " + port+"\n");
    while (true) {
      Socket socket = serverSocket.accept();
      serverLog.logln("Client connected. " + getAddress(socket.getInetAddress(), socket.getPort()) + "\n");
      BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      String input = null;
      try {
        while ((input = br.readLine()) != null) {
          serverLog.logln(createInputString(socket.getInetAddress(), socket.getPort(), input));
          String output = getOutput(input);
          serverLog.logln(output+"\n");
          out.println(output);
          out.flush();
        }
      } catch (Exception e) {
        //Empty catch
      }
      serverLog.logln("Connection closed with " + socket.getInetAddress() + "\n");
    }
  }
}
