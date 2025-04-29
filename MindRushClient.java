/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package hw3client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

/**
 *
 * @author andysstuff
 */
public class HW3Client {

    private static final int SERVER_PORT = 5190;
    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Chat World");
    private JTextArea display = new JTextArea(30, 50);
    private JTextField inputText = new JTextField(50);
    
    private void sendMessage() {
        //send messages to the server
        out.println(inputText.getText());
        inputText.setText("");
    }
    
    public HW3Client(String serverAddr, String username) throws IOException {
        // Network setup to server
        Socket socket = new Socket(serverAddr, SERVER_PORT);
        //Sets up client socket to commuincate with server
        
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        // Send the username to the server
        out.println(username);
        //make sure different line as message
        
        // Setup GUI components
        JButton sendButton = new JButton("Send");
        inputText.setEditable(true);
        display.setEditable(false);
        frame.getContentPane().add(inputText, BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(display), BorderLayout.CENTER);
        frame.getContentPane().add(sendButton, BorderLayout.EAST);
        frame.pack();

        // Add action listeners
       
         sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        inputText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Add background thread to listen for messages from the server
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    while (true) {
                        String line = in.readLine();
                        if (line == null) {
                            break;
                        }
                        display.append(line + "\n");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
       Thread thread = new Thread(runnable);
       thread.start();
    }

    public static void main(String[] args) throws Exception {
        String serverAddr = JOptionPane.showInputDialog(
                "Enter IP Address of the Server:", "localhost");
        String username = JOptionPane.showInputDialog(
                "Enter your username:");

        HW3Client client = new HW3Client(serverAddr, username);
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
    }
}
