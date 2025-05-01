import java.io.*;
import java.net.*;
import java.util.*;

public class MindRushServer {
    private static final int PORT = 5190;
    private static Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());
    private static HashMap<Integer, Integer>[] currentQuiz = new HashMap[5];
    private static final HashMap<String, Integer> correctAnswers =  new HashMap<>();
    private static final HashMap<String, String[]> quizMap = new HashMap<>();
    private static List<Integer> questionIndexes;
    private static boolean startPlay = false;
    private static boolean playing = false;
    private static List<String> questionKeys;
    private static String current_question;


    public static void main(String[] args) throws IOException {

        quizMap.put("What is the capital of France?", new String[]{"Rome", "London", "Paris", "Berlin"});
        quizMap.put("What is the capital of Germany?", new String[]{"Frankfurt", "Munich", "Berlin", "Hamburg"});
        quizMap.put("What is the capital of Italy?", new String[]{"Venice", "Milan", "Rome", "Florence"});
        quizMap.put("What is the capital of Spain?", new String[]{"Madrid", "Barcelona", "Seville", "Valencia"});
        quizMap.put("What is the capital of Canada?", new String[]{"Vancouver", "Montreal", "Toronto", "Ottawa"});
        quizMap.put("Which planet is known as the Red planet?", new String[]{"Mars", "Jupiter", "Venus", "Saturn"});
        quizMap.put("Which planet is known as the Blue planet?", new String[]{"Uranus", "Neptune", "Earth", "Saturn"});
        quizMap.put("Which planet is known as the Morning Star planet?", new String[]{"Mars", "Mercury", "Jupiter", "Venus"});
        quizMap.put("Which planet is known as the Ringed planet?", new String[]{"Jupiter", "Neptune", "Saturn", "Uranus"});
        quizMap.put("Which planet is known as the Giant planet?", new String[]{"Earth", "Jupiter", "Saturn", "Neptune"});
        quizMap.put("What is the largest ocean on Earth?", new String[]{"Atlantic Ocean", "Indian Ocean", "Pacific Ocean", "Arctic Ocean"});
        quizMap.put("What is the tallest mountain in the world?", new String[]{"K2", "Mount Everest", "Kangchenjunga", "Lhotse"});
        quizMap.put("What is the longest river in the world?", new String[]{"Amazon River", "Nile River", "Yangtze River", "Mississippi River"});
        quizMap.put("What is the smallest country in the world?", new String[]{"Monaco", "San Marino", "Liechtenstein", "Vatican City"});
        quizMap.put("What is the largest desert in the world?", new String[]{"Sahara Desert", "Gobi Desert", "Arabian Desert", "Antarctic Desert"});
        quizMap.put("Who wrote 'Romeo and Juliet'?", new String[]{"Charles Dickens", "Mark Twain", "Jane Austen", "William Shakespeare"});
        quizMap.put("Who painted the Mona Lisa?", new String[]{"Vincent van Gogh", "Pablo Picasso", "Claude Monet", "Leonardo da Vinci"});
        quizMap.put("What is the chemical symbol for water?", new String[]{"H2O", "O2", "CO2", "NaCl"});
        quizMap.put("What is the speed of light?", new String[]{"150,000 km/s", "450,000 km/s", "600,000 km/s", "300,000 km/s"});
        quizMap.put("What is the capital of Japan?", new String[]{"Osaka", "Kyoto", "Nagoya", "Tokyo"});
        quizMap.put("What is the largest mammal in the world?", new String[]{"Elephant", "Giraffe", "Hippopotamus", "Blue Whale"});
        quizMap.put("What is the smallest planet in the solar system?", new String[]{"Mercury", "Mars", "Venus", "Pluto"});
        quizMap.put("What is the hardest natural substance on Earth?", new String[]{"Gold", "Iron", "Diamond", "Platinum"});
        quizMap.put("What is the most spoken language in the world?", new String[]{"English", "Mandarin Chinese", "Spanish", "Hindi"});
        quizMap.put("What is the currency of the United Kingdom?", new String[]{"Euro", "Dollar", "Pound Sterling", "Yen"});
        quizMap.put("What is the capital of Australia?", new String[]{"Sydney", "Melbourne", "Canberra", "Brisbane"});
        quizMap.put("What is the largest continent?", new String[]{"Africa", "Asia", "Europe", "North America"});
        quizMap.put("What is the boiling point of water?", new String[]{"90°C", "100°C", "110°C", "120°C"});
        quizMap.put("What is the freezing point of water?", new String[]{"0°C", "-10°C", "5°C", "-5°C"});
        quizMap.put("Who discovered gravity?", new String[]{"Albert Einstein", "Isaac Newton", "Galileo Galilei", "Nikola Tesla"});
        quizMap.put("What is the capital of Russia?", new String[]{"Saint Petersburg", "Moscow", "Kazan", "Sochi"});
        quizMap.put("What is the national animal of the United States?", new String[]{"Bald Eagle", "Grizzly Bear", "Buffalo", "Cougar"});
        quizMap.put("What is the largest island in the world?", new String[]{"Greenland", "Australia", "Madagascar", "Borneo"});
        quizMap.put("What is the capital of South Korea?", new String[]{"Busan", "Seoul", "Incheon", "Daegu"});
        quizMap.put("What is the chemical symbol for gold?", new String[]{"Au", "Ag", "Fe", "Pb"});
        quizMap.put("What is the capital of Brazil?", new String[]{"Rio de Janeiro", "São Paulo", "Brasília", "Salvador"});
        quizMap.put("What is the national sport of Canada?", new String[]{"Hockey", "Lacrosse", "Baseball", "Soccer"});
        quizMap.put("What is the capital of Egypt?", new String[]{"Cairo", "Alexandria", "Giza", "Luxor"});
        quizMap.put("What is the largest organ in the human body?", new String[]{"Heart", "Liver", "Skin", "Lungs"});
        quizMap.put("What is the capital of India?", new String[]{"Mumbai", "Delhi", "New Delhi", "Kolkata"});

        correctAnswers.put("What is the capital of France?", 2);
        correctAnswers.put("What is the capital of Germany?", 2);
        correctAnswers.put("What is the capital of Italy?", 2);
        correctAnswers.put("What is the capital of Spain?", 0);
        correctAnswers.put("What is the capital of Canada?", 3);
        correctAnswers.put("Which planet is known as the Red planet?", 0);
        correctAnswers.put("Which planet is known as the Blue planet?", 1);
        correctAnswers.put("Which planet is known as the Morning Star planet?", 3);
        correctAnswers.put("Which planet is known as the Ringed planet?", 2);
        correctAnswers.put("Which planet is known as the Giant planet?", 1);
        correctAnswers.put("What is the largest ocean on Earth?", 2);
        correctAnswers.put("What is the tallest mountain in the world?", 1);
        correctAnswers.put("What is the longest river in the world?", 1);
        correctAnswers.put("What is the smallest country in the world?", 3);
        correctAnswers.put("What is the largest desert in the world?", 3);
        correctAnswers.put("Who wrote 'Romeo and Juliet'?", 3);
        correctAnswers.put("Who painted the Mona Lisa?", 3);
        correctAnswers.put("What is the chemical symbol for water?", 0);
        correctAnswers.put("What is the speed of light?", 3);
        correctAnswers.put("What is the capital of Japan?", 3);
        correctAnswers.put("What is the largest mammal in the world?", 3);
        correctAnswers.put("What is the smallest planet in the solar system?", 0);
        correctAnswers.put("What is the hardest natural substance on Earth?", 2);
        correctAnswers.put("What is the most spoken language in the world?", 1);
        correctAnswers.put("What is the currency of the United Kingdom?", 2);
        correctAnswers.put("What is the capital of Australia?", 2);
        correctAnswers.put("What is the largest continent?", 1);
        correctAnswers.put("What is the boiling point of water?", 1);
        correctAnswers.put("What is the freezing point of water?", 0);
        correctAnswers.put("Who discovered gravity?", 1);
        correctAnswers.put("What is the capital of Russia?", 1);
        correctAnswers.put("What is the national animal of the United States?", 0);
        correctAnswers.put("What is the largest island in the world?", 0);
        correctAnswers.put("What is the capital of South Korea?", 1);
        correctAnswers.put("What is the chemical symbol for gold?", 0);
        correctAnswers.put("What is the capital of Brazil?", 2);
        correctAnswers.put("What is the national sport of Canada?", 1);
        correctAnswers.put("What is the capital of Egypt?", 0);
        correctAnswers.put("What is the largest organ in the human body?", 2);
        correctAnswers.put("What is the capital of India?", 2);
        
        List<String> questionKeys = new ArrayList<>(correctAnswers.keySet());

        for (int i = 0; i < 5; i++) {
            currentQuiz[i] = new HashMap<>();
        }
        ServerSocket serverSocket = new ServerSocket(PORT);      

        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler handler = new ClientHandler(socket); // connection for this new client
            clients.add(handler);
            handler.start();
            if (startPlay) {
                startPlay = false;
                playing = true;
                Thread countdownThread = new Thread(() -> {
                    try {
                        for (int i = 0; i < 5; i++) {
                            current_question = questionKeys.get(questionIndexes.get(i));
                            broadcast("QUESTION:" + current_question + ";" + String.join(";", quizMap.get(current_question)));
                            Thread.sleep(30000);
                        }
                        for (ClientHandler client : clients) {
                            broadcast(client.getScore());
                        }
                        playing = false;
                    }
                    catch (InterruptedException e) {
                        System.out.println("Countdown interrupted");
                    }
                });
                countdownThread.start();
            }
        }

    }

    private static void broadcast(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.out.println(message);
            }
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private Scanner in;
        private PrintStream out;
        private String username;
        private int correct;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() { // runs when the thread starts
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintStream(socket.getOutputStream(), true);

                String message;

                correct = 0;

                username = in.nextLine();

                Random random = new Random();
                
                while (in.hasNextLine()) {
                    if (!playing) {
                        message = in.nextLine();
                        if (message.equals("START_CLICKED")) {
                            // Set questions for this quiz
                            questionIndexes = new ArrayList<>();
                            for (int i = 0; i < 5; i++) {
                                questionIndexes.add(random.nextInt(40));
                            }
                            startPlay = true;
                        }
                    }
                    else { // If the input is an answer being given
                        if (correctAnswers.get(current_question) == Integer.parseInt(in.nextLine())) {
                            correct++;
                        }
                    }
                }
            } catch (IOException e) {
                // failed
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }

        public String getScore() {
            return (username + correct);
        }
    }
}