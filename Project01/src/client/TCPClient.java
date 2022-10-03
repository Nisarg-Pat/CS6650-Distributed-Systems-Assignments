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
    clientLog.createFile("TCPClientLog.txt");

    clientLog.logln(String.format("Connected to server at %s:%s", address, port));
  }

  @Override
  public void request(String input) throws IOException {
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    out.println(input);
    out.flush();

    long timeoutTime = System.currentTimeMillis() + TIMEOUT;
    while(!br.ready()) {
      long currentTime = System.currentTimeMillis();
      if(currentTime >= timeoutTime) {
        clientLog.logln("TIMEOUT!");
        return;
      }
    }

    String response = br.readLine();
    String output = getOutput(response);
    if(output.isEmpty()) {
      clientLog.logln("Malformed Response!");
      return;
    }
    clientLog.logln("Response: " + output+"\n");
  }
}
