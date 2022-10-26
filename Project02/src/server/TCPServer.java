package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Access: pakage-protected
 * Class for TCP Server
 */
class TCPServer extends AbstractServer {
  private final ServerSocket serverSocket;

  /**
   * Constructor for TCP Server
   *
   * @param port Port at which server should listen
   * @throws IOException
   */
  public TCPServer(int port) throws IOException {
    super(port);
    serverSocket = new ServerSocket(port);

    //Creating a log file
    serverLog.createFile("TCPServerLog.txt");
  }

  @Override
  public void start() throws IOException {
    serverLog.logln("TCPServer listening at port: " + port + "\n");
    while (true) {
      //Accepts new client
      Socket socket = serverSocket.accept();
      serverLog.logln("Client connected. " + getAddress(socket.getInetAddress(), socket.getPort()) + "\n");
      BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      String input = null;

      //Reads request from the client
      try {
        while ((input = br.readLine()) != null) {
          serverLog.logln(createInputString(socket.getInetAddress(), socket.getPort(), input));

          //Processes the request
          String output = getOutput(input);
          serverLog.logln(output + "\n");

          //Sends response back to client
          out.println(output);
          out.flush();
        }
      } catch (Exception e) {
        //Empty catch
      }

      serverLog.logln("Connection closed with " + getAddress(socket.getInetAddress(), socket.getPort()) + "\n");
      socket.close();
    }
  }
}
