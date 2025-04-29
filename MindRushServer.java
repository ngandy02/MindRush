import java.io.*;
import java.net.*;
import java.util.*;

public class MindRushServer {
    private static final int PORT = 5190;
    private static Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler handler = new ClientHandler(socket); // connection for this new client
            clients.add(handler);
            handler.start();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private String username;
        private Scanner in;
        private PrintStream out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() { // runs when the thread starts
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintStream(socket.getOutputStream(), true);
        
                username = in.nextLine(); // the first message is their username
        
                String message;
                while (in.hasNextLine()) {
                    message = in.nextLine();
                    broadcast(username + ": " + message);
                }
            } catch (IOException e) {
                // failed
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }

        private void broadcast(String message) {
            synchronized (clients) {
                for (ClientHandler client : clients) {
                    client.out.println(message);
                }
            }
        }
    }
}