package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveFromServer implements Runnable {

    Socket sock = null;
    BufferedReader recieve = null;

    public ReceiveFromServer(Socket sock) {
        this.sock = sock;
    }

    @Override
    public void run() {
        try {
            recieve = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
            String msgRecieved = null;
            while ((msgRecieved = recieve.readLine()) != null) {
                System.out.println("Message from Server: " + msgRecieved);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
