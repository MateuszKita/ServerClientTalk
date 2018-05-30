package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SendToClient implements Runnable {

    String infoAboutGame = ("Current Tic Tac Toe board:\n");

    PrintWriter pwPrintWriter;
    Socket clientSock = null;

    public SendToClient(Socket clientSock) {
        this.clientSock = clientSock;
    }

    @Override
    public void run() {
        try {
            pwPrintWriter = new PrintWriter(new OutputStreamWriter(this.clientSock.getOutputStream()));
            if (clientSock.isConnected()) {
                pwPrintWriter.println(infoAboutGame
                        + "\n\nType field name from A1 to C3."
                        + "\n/OR type 'EXIT' to exit");
                pwPrintWriter.flush();
            }
            while (true) {

                String msgToClientString = null;
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

                msgToClientString = input.readLine();
                pwPrintWriter.println("YOU WIN, CONGRATULATIONS!");

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
