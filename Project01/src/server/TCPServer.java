package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

  int port;
  ServerSocket serverSocket;

  public TCPServer(int port) throws IOException {
    this.port = port;
    serverSocket = new ServerSocket(port);

  }

  public void execute() throws IOException {
    System.out.println("TCPServer started at port: " + port);
    while(true) {
      Socket socket = serverSocket.accept();
      System.out.println(socket.getInetAddress());
      InputStream socketIn = socket.getInputStream();
      DataInputStream dataIn = new DataInputStream(socketIn);
      String input = new String(dataIn.readUTF());
      System.out.println("\nServer Input: " + input);

      //Sending the changed string
      OutputStream socketOut = socket.getOutputStream();
      DataOutputStream dataOut = new DataOutputStream(socketOut);
      dataOut.writeUTF(input);

//      dataIn.close();
//      socketIn.close();
//      dataOut.close();
//      socketOut.close();
    }
    //socket.close();
  }
}
