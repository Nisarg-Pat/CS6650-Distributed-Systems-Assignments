package Q7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
  public static void main(String[] args) throws IOException {
    String host;
    int port;

    if (args.length != 0 && args.length != 2) {
      System.out.println("Improper number of arguments!. Required 0 or 2 arguments!");
      return;
    } else if (args.length == 0) {
      host = "localhost";
      port = 1234;
    } else {
      // args.length is 2
      try {
        host = args[0];
        port = Integer.parseInt(args[1]);
      } catch (NumberFormatException e) {
        System.out.println(args[1] + " is not a valid port number!!");
        return;
      }
    }

    //Inputting the string
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter Text: ");
    String input = sc.nextLine();
    sc.close();

    Socket socket = new Socket(host, port);

    //Sending the string to server
    OutputStream socketOut = socket.getOutputStream();
    DataOutputStream dataOut = new DataOutputStream(socketOut);
    dataOut.writeUTF(input);

    //Receiving the changed string
    InputStream socketIn = socket.getInputStream();
    DataInputStream dataIn = new DataInputStream(socketIn);
    String changedInput = new String(dataIn.readUTF());
    System.out.println("Server Response: " + changedInput);

    dataIn.close();
    socketIn.close();
    dataOut.close();
    socketOut.close();
    socket.close();
  }
}
