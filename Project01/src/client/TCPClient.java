package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient extends AbstractClient {

  public TCPClient(String host, int port) throws IOException {
    super(host, port);
//    socket = new Socket(host, port);
  }

//  @Override
//  public void execute() throws IOException {
//    Scanner sc = new Scanner(System.in);
//    System.out.print("Enter Text: ");
//    String input = sc.nextLine();
//    sc.close();
//
//    OutputStream socketOut = socket.getOutputStream();
//    DataOutputStream dataOut = new DataOutputStream(socketOut);
//    dataOut.writeUTF(input);
//
//    //Receiving the changed string
//    InputStream socketIn = socket.getInputStream();
//    DataInputStream dataIn = new DataInputStream(socketIn);
//    String changedInput = new String(dataIn.readUTF());
//    System.out.println("Server Response: " + changedInput);
//
//    dataIn.close();
//    socketIn.close();
//    dataOut.close();
//    socketOut.close();
//    socket.close();
//  }
}
