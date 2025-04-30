
import java.util.HashMap;
import java.util.Map;

public class QuizBank {
    public static void main(String[] args) {
        Map<String, String[]> quizMap = new HashMap<>();
        Map<String, Integer> correctAnswers = new HashMap<>();

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

        // Example: Print first 5 questions with correct answer index
        int count = 0;
        for (String question : quizMap.keySet()) {
            if (count++ == 5) break;
            System.out.println("Q: " + question);
            String[] options = quizMap.get(question);
            for (int i = 0; i < options.length; i++) {
                System.out.println((char)('A' + i) + ") " + options[i]);
            }
            System.out.println("Correct Answer Index: " + correctAnswers.get(question));
            System.out.println();
        }
    }
}
