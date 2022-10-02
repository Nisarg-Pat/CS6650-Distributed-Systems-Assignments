package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class TCPClient extends AbstractClient {

  public TCPClient(String host, int port) throws IOException {
    super(host, port);
  }

  @Override
  public void send(String input) throws IOException {
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    out.println(input);
    out.flush();
  }
}
