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

/**
 * Access: pakage-protected
 * Class for TCP Client
 */
class TCPClient extends AbstractClient {

  Socket socket;

  /**
   * Constructor for TCP Client
   * @param address InetAddress of the server
   * @param port server port
   * @throws IOException
   */
  public TCPClient(InetAddress address, int port) throws IOException {
    super();
    this.socket = new Socket(address, port);

    //Creating a log file
    clientLog.createFile("TCPClientLog.txt");
    clientLog.logln(String.format("Connected to server at %s:%s", address, port));
  }

  @Override
  public void request(String input) throws IOException {
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    // Sending Request to the Server using DatagramPacket
    out.println(input);
    out.flush();

    // Checking for connection timeout
    long timeoutTime = System.currentTimeMillis() + TIMEOUT;
    while (!br.ready()) {
      long currentTime = System.currentTimeMillis();
      if (currentTime >= timeoutTime) {
        clientLog.logln("Server Timeout! Please try requesting again\n");
        return;
      }
    }

    // Receiving the response
    StringBuilder response = new StringBuilder();
    response.append(br.readLine());
    while (br.ready()) {
      response.append("\n").append(br.readLine());
    }

    //Getting output from the response
    String output = getOutput(response.toString());
    if (output.isEmpty()) {
      clientLog.logln(String.format("Received malformed response from server %s:%s\n",
              socket.getInetAddress(), socket.getPort()));
      return;
    }
    clientLog.logln("Response: " + output + "\n");
  }
}
