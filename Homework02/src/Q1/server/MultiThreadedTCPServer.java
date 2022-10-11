package Q1.server;

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
class MultiThreadedTCPServer extends AbstractServer {
  private final ServerSocket serverSocket;

  /**
   * Constructor for TCP Server
   *
   * @param port Port at which server should listen
   * @throws IOException
   */
  public MultiThreadedTCPServer(int port) throws IOException {
    super(port);
    serverSocket = new ServerSocket(port);
  }

  @Override
  public void start() throws IOException {
    System.out.println("TCPServer listening at port: " + port + "\n");
    while (true) {
      //Accepts new client
      Socket socket = serverSocket.accept();
      new Thread(new ClientRunnable(socket)).start();
    }
  }

  class ClientRunnable implements Runnable {

    Socket socket;

    ClientRunnable(Socket socket) {
      this.socket = socket;
    }

    @Override
    public void run() {
      try {
        System.out.println("Client connected. " + getAddress(socket.getInetAddress(), socket.getPort()) + "\n");
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        String input = null;

        //Reads request from the client
        try {
          while ((input = br.readLine()) != null) {
            System.out.println(createInputString(socket.getInetAddress(), socket.getPort(), input));

            //Processes the request
            String output = getOutput(input);
            System.out.println(output + "\n");

            // Sends response back to client
            out.println(output);
            out.flush();
          }
        } catch (Exception e) {
          //Empty catch
        }

        System.out.println("Connection closed with " + getAddress(socket.getInetAddress(), socket.getPort()) + "\n");
        socket.close();
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }
}
