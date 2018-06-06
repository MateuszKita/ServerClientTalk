package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import static java.lang.Thread.sleep;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerTalk extends javax.swing.JFrame {

    public ServerTalk() {
        initComponents();
    }

    public int port = 8000;
    public Socket publicSocket = null;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        console = new javax.swing.JTextArea();
        startServerButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Console");

        progressBar.setVisible(false);
        progressBar.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        progressBar.setValue(50);
        progressBar.setMinimumSize(new java.awt.Dimension(20, 22));
        progressBar.setPreferredSize(new java.awt.Dimension(146, 30));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Server");

        console.setColumns(20);
        console.setRows(5);
        jScrollPane1.setViewportView(console);

        startServerButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        startServerButton.setText("Run Server");

        startServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    startServerButtonActionPerformed(evt);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 831, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(startServerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startServerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startServerButtonActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();

        }
        while (true) {
            try {
                publicSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            new MainThread(publicSocket).start();
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new ServerTalk().setVisible(true);
        });
    }

    public class ReceiveFromClient implements Runnable {

        Socket clientSocket = null;
        BufferedReader brBufferedReader = null;

        public ReceiveFromClient(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                brBufferedReader = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
                while (true) {
                    sleep(10);
                    String msgRecieved = brBufferedReader.readLine();
                    if (msgRecieved.equals("+")) {
                        console.append("\nProgress bar value from client: " + msgRecieved);
                        progressBar.setValue(progressBar.getValue() + 1);
                    }
                    if (msgRecieved.equals("-")) {
                        console.append("\nProgress bar value from client: " + msgRecieved);
                        progressBar.setValue(progressBar.getValue() - 1);
                    }
                    if (msgRecieved.equals("end")) {
                        publicSocket.close();
                        System.exit(0);
                    }
                }

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerTalk.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public class SendToClient implements Runnable {

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
                    console.append("\nNew client connected");
                    pwPrintWriter.flush();
                }
                while (true) {
                    sleep(10);
                    if (progressBar.getValue() == 100 || progressBar.getValue() == 0) {
                        pwPrintWriter.println(Integer.toString(progressBar.getValue()));
                    }
                    pwPrintWriter.println(Integer.toString(progressBar.getValue()));
                    pwPrintWriter.flush();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerTalk.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class MainThread extends Thread {

        protected Socket clientSocket;

        public MainThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            console.append("\nReceived connection from " + clientSocket.getInetAddress() + " on port " + clientSocket.getPort());

            ReceiveFromClient receive = new ReceiveFromClient(clientSocket);
            Thread receiveThread = new Thread(receive);
            receiveThread.start();

            SendToClient send = new SendToClient(clientSocket);
            Thread sendThread = new Thread(send);
            sendThread.start();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    static javax.swing.JTextArea console;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    static javax.swing.JProgressBar progressBar;
    private javax.swing.JButton startServerButton;
    // End of variables declaration//GEN-END:variables
}
