package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SendToServer implements Runnable {

    Socket sock = null;
    PrintWriter print = null;
    BufferedReader brinput = null;

    public SendToServer(Socket sock) {
        this.sock = sock;
    }

    @Override
    public void run() {
        try {
            if (sock.isConnected()) {
                System.out.println("Client connected to " + sock.getInetAddress() + " on port " + sock.getPort());
                this.print = new PrintWriter(sock.getOutputStream(), true);
                while (true) {
                    brinput = new BufferedReader(new InputStreamReader(System.in));
                    String msgToServer = null;
                    msgToServer = brinput.readLine();
                    this.print.println(msgToServer);
                    this.print.flush();

                    if (msgToServer.equals("EXIT")) {
                        break;
                    }
                    System.out.print("Chat to Server: ");
                }
                sock.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
