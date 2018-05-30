package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveFromClient implements Runnable {

    Socket clientSocket = null;
    BufferedReader brBufferedReader = null;

    public ReceiveFromClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            brBufferedReader = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

            String messageString;
            while (true) {
                OUTER:
                while ((messageString = brBufferedReader.readLine()) != null) {
                    System.out.println("From Client: " + messageString);
                    System.out.print("Chat to Client: ");
                }
                this.clientSocket.close();
                System.exit(0);
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
