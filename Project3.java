import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class Project3 {
    public static void main(String[] args) {
        // file path goes to 'p2.txt' within the same folder
        Path txPath = Paths.get("p2.txt");
        try {
            // lines get read and are stored as type string
            List<String> lines = Files.readAllLines(txPath);

            // puzzleLetters varible is set to the first line, and everything else is lowercase
            String puzzleLetters = lines.get(0);

            // Method to verify if data can be added to validAnswers or is invalid 
            List<String> validAnswers = new ArrayList<>();
            for (String line : lines.stream().skip(1).toArray(String[]::new)) {
                if (line.matches("[a-z]+")) {
                    validAnswers.add(line);
                } else {
                    System.out.println("Invalid word found: " + line);
                }
            }

            // unsorted word list stores the answers
            UnsortedWordList unsortedList = new UnsortedWordList();
            for (String answer : validAnswers) {
                // each answer is added to the unsorted list (word obj)
                unsortedList.add(new Word(answer));
            }
            
            // sorted word list stores answers 
            SortedWordList sortedList = new SortedWordList();
            for (String answer : validAnswers) {
                sortedList.add(new Word(answer)); 
            }

            new GUI(puzzleLetters, unsortedList, sortedList, validAnswers.toArray(new String[0])); 

        } catch (IOException e) {
            // IOException handling as we are using 'p2.txt'
            e.printStackTrace();
        } 
    }
}


