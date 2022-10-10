package Q1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleThreadedTCPServer {
  public static void main(String[] args) throws IOException {
    int port;
    if (args.length >= 2) {
      System.out.println("Improper number of arguments!. Required 1 or less");
      return;
    } else if (args.length == 0) {
      port = 1234;
    } else {
      //args.length is 1
      try {
        port = Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {
        System.out.println(args[0] + " is not a valid port number!!");
        return;
      }
    }

    ServerSocket serverSocket = new ServerSocket(port);

    System.out.println("TCPServer started at port: " + port);

    Socket socket = serverSocket.accept();

    //Receiving the client string
    InputStream socketIn = socket.getInputStream();
    DataInputStream dataIn = new DataInputStream(socketIn);
    String input = new String(dataIn.readUTF());
    System.out.println("\nServer Input: " + input);

    //Changing the string
    String changedInput = reverseAndChange(input);
    System.out.println("Server Output: " + changedInput);

    //Sending the changed string
    OutputStream socketOut = socket.getOutputStream();
    DataOutputStream dataOut = new DataOutputStream(socketOut);
    dataOut.writeUTF(changedInput);

    dataIn.close();
    socketIn.close();
    dataOut.close();
    socketOut.close();
    socket.close();
  }

  public static String reverseAndChange(String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) >= 'A' && s.charAt(i) <= 'Z') {
        sb.append((char) (s.charAt(i) - 'A' + 'a'));
      } else if (s.charAt(i) >= 'a' && s.charAt(i) <= 'z') {
        sb.append((char) (s.charAt(i) - 'a' + 'A'));
      } else {
        sb.append(s.charAt(i));
      }
    }
    return sb.reverse().toString();
  }
}
