import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class MindRushClient {
    private static final int SERVER_PORT = 5190;
    private BufferedReader in;
    private PrintWriter out;
    private final JFrame frame = new JFrame("Mind Rush");
    private JTextArea display = new JTextArea(20, 50);
    private JLabel questionLabel = new JLabel("Waiting for question...");
    private JButton choiceA = new JButton("A");
    private JButton choiceB = new JButton("B");
    private JButton choiceC = new JButton("C");
    private JButton choiceD = new JButton("D");
    private JLabel scoreLabel = new JLabel("Score: 0");
    private final JButton startButton = new JButton("Start Quiz");

    public MindRushClient(String serverAddr, String username) throws IOException {
        Socket socket = new Socket(serverAddr, SERVER_PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        out.println(username);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BorderLayout());
        questionPanel.add(questionLabel, BorderLayout.CENTER);
        questionPanel.add(scoreLabel, BorderLayout.EAST);
        frame.add(questionPanel, BorderLayout.NORTH);

        JPanel choicesPanel = new JPanel();
        choicesPanel.setLayout(new GridLayout(2, 2));
        choicesPanel.add(choiceA);
        choicesPanel.add(choiceB);
        choicesPanel.add(choiceC);
        choicesPanel.add(choiceD);
        frame.add(choicesPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setLayout(new FlowLayout());
        bottom.add(new JScrollPane(display));
        bottom.add(startButton);
        frame.add(bottom, BorderLayout.SOUTH);

        startButton.addActionListener(e -> {
            out.println("START_CLICKED");
            display.append("Game start!\n");
        });

        display.setEditable(false);

        frame.pack();
        frame.setVisible(true);

        choiceA.addActionListener(e -> out.println("0"));
        choiceB.addActionListener(e -> out.println("1"));
        choiceC.addActionListener(e -> out.println("2"));
        choiceD.addActionListener(e -> out.println("3"));

        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println("Received message: " + line);
                    if (line.startsWith("QUESTION:")) {
                        // Parse question and choices
                        String[] parts = line.substring(9).split(";");
                        if (parts.length == 5) {
                            final String questionText = parts[0];
                            final String[] choices = new String[] { parts[1], parts[2], parts[3], parts[4] };
                            SwingUtilities.invokeLater(() -> {
                                questionLabel.setText(questionText);
                                choiceA.setText(choices[0]);
                                choiceB.setText(choices[1]);
                                choiceC.setText(choices[2]);
                                choiceD.setText(choices[3]);
                            });
                        } else {
                            System.err.println("Invalid QUESTION format: " + line);
                        }
                    } else if (line.startsWith("SCORE:")) {
                        // Update score
                        final String score = line.substring(6);
                        SwingUtilities.invokeLater(() -> {
                            scoreLabel.setText("Score: " + score);
                        });
                    } else {
                        // Log other messages
                        final String message = line;
                        SwingUtilities.invokeLater(() -> {
                            display.append(message + "\n");
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) throws Exception {
        String serverAddr = JOptionPane.showInputDialog(
                "Enter IP Address of the Server:", "localhost");
        String username = JOptionPane.showInputDialog(
                "Enter your username:");
        new MindRushClient(serverAddr, username);
    }
}