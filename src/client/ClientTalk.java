package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientTalk extends javax.swing.JFrame {

    public String plusMinusSign = randomPlusMinus();
    public String valueToSend = "";
    public Socket sock = null;

    synchronized String getValueToSend() {
        String i = this.valueToSend;
        this.valueToSend = "";
        return i;
    }

    synchronized void setValueToSend(String value) {
        this.valueToSend = value;
    }

    public ClientTalk() {
        initComponents();
    }

    String randomPlusMinus() {

        Random rand = new Random();
        int n = rand.nextInt(2) + 1;
        if (n == 1) {
            return "+";
        } else {
            return "-";
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        connectButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        console = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Client");

        jButton1.setFont(new java.awt.Font("Dialog", 1, 60)); // NOI18N
        jButton1.setEnabled(false);
        jButton1.setText(plusMinusSign
        );
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    jButton1ActionPerformed(evt);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        jProgressBar1.setValue(50);

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        console.setColumns(20);
        console.setRows(5);
        jScrollPane1.setViewportView(console);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addComponent(connectButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(123, 123, 123)
                        .addComponent(jLabel1)))
                .addContainerGap(7, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(connectButton)
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        try {
            jButton1.setEnabled(false);
            Socket sock = new Socket("localhost", 8000);
            SendToServer sendToServer = new SendToServer(sock);
            Thread sendThread = new Thread(sendToServer);
            sendThread.start();
            ReceiveFromServer recieveThread = new ReceiveFromServer(sock);
            Thread receiveThread = new Thread(recieveThread);
            receiveThread.start();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            console.append(exception.getMessage());
        }
    }//GEN-LAST:event_connectButtonActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        try {
            sock.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientTalk.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
        setValueToSend(this.plusMinusSign);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new ClientTalk().setVisible(true);
        });
    }

    public class SendToServer implements Runnable {

        PrintWriter print = null;
        BufferedReader brinput = null;

        public SendToServer(Socket sock2) {
            sock = sock2;
        }

        @Override
        public void run() {
            try {
                if (sock.isConnected()) {
                    console.append("\nClient connected to " + sock.getInetAddress() + " on port " + sock.getPort());
                    this.print = new PrintWriter(sock.getOutputStream(), true);
                    while (true) {
                        sleep(10);
                        brinput = new BufferedReader(new InputStreamReader(System.in));
                        this.print.println(getValueToSend());
                        this.print.flush();
                        if (jProgressBar1.getValue() == 100 || jProgressBar1.getValue() == 0) {
                            this.print.println("end");
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientTalk.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

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
                console.append(recieve.readLine());
                while ((msgRecieved = recieve.readLine()) != null) {
                    if (jProgressBar1.getValue() != Integer.parseInt(msgRecieved)) {
                        jProgressBar1.setValue(Integer.parseInt(msgRecieved));
                    }
                    if (jProgressBar1.getValue() != 100 || jProgressBar1.getValue() != 0) {
                        jButton1.setEnabled(true);
                    }
                    if (Integer.parseInt(msgRecieved) == 0 || Integer.parseInt(msgRecieved) == 100) {
                        console.append("\nTHE END!!");
                        jButton1.setEnabled(false);
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connectButton;
    public static javax.swing.JTextArea console;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    static javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
