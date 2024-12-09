/**
 * Project3 focuses on reaching a victory when all the words are guessed. The updates for this project display a victory message and allow the user to
 * quit or play again, allow the user to upload a .txt file containing new letters to guess along with the solution words, and also has a custom exception
 * implemented to print out any guess that isn’t a word containing lowercase letters in the console.
 *
 * - loadFile(): This function focuses on reading the file uploaded by the user and allowing the program to update and function properly. It checks if there
 *   is at least one word to guess using an if statement. The puzzle letters are updated, score is reset, the unsortedList, sortedList, and set containing guessed
 *   words are all cleared. A for loop is used to add any newly guessed words to the sortedList and unsortedList and continue the guessing game. The program
 *   has the letters to guess and the answers so the game can continue. validAnswers is used to store all the solutions from the given file that are correct.
 *   A for loop is used to iterate through the data and if a word passes the check, it's added. Otherwise, it's printed in the console.
 *
 * - WinnerWinner(): This helper function focuses on displaying the winner message once the size of the users correctly guessed words matches the size of the
 *   answers array. This is done using an if statement and since the user can only guess a word once, it works and allows the game to display a message allowing
 *   the user to play again or quit the game.
 *
 * - guessAgain(): This helper function focuses on resetting everything since the user wants to play the game again. It is called when the user presses ‘yes’
 *   to play again after winning. It clears the GUI fully and sets the score to 0 and then creates the proper GUI components, refreshes it and lets the user continue.
 *
 * - inputListen(): This function focuses on handling all user input. It has an if statement to check for multiple entries ensuring there are no duplicate entries
 *   preventing no extra points and words from being added to the GUI. If a user enters a correct answer, we call two helper functions. HasFirstLetter ensures that
 *   the answer has the first letter of the guessing letters and if it does, one point is granted. If a user enters a guess with all the letters, three points are granted.
 *   These methods are using if statements. This function also uses else statements to display an incorrect guess message to the user upon guessing incorrectly.
 *   The custom exception message is implemented here so that if the user enters anything that isn’t a word with lowercase letters, it prints the word to the console.
 *   If the user wins, the WinnerWinner() helper function is called.
 *
 * - hasFirstLetter(String userAttempt): Focuses on taking the first letter of the userAttempt by doing charAt() and then setting it to a char variable. It then uses
 *   indexOf() to see if a value NOT equal to -1 is returned. If a value not equal to -1 is returned, we are assured that the first letter is in the userAttempt and it's true, otherwise it's false.
 *
 * - isCorrectAnswer(String userAttempt): Focuses on checking for the userAttempt and matching with correct answers. It iterates over each word of the list of correct answers
 *   and compares it. If it finds a match, it returns true.
 *
 * - hasAllLetters(String userAttempt): Focuses on checking if userAttempt has all the letters present in the puzzle letters. It does this by using a for loop with each letter
 *   in the puzzleLetters and checking if it matches with userAttempt with the use of indexOf. Similar but different in the requirement to the last method, it looks for the method
 *   to return -1 and if so, this means that the letter is NOT in the userAttempt resulting in a false return. Otherwise, all letters are in the userAttempt and it's true.
 *
 * - updateSWL(): Focuses on updating the GUI components. It iterates over sortedList and adds the word to sW. sW is then sorted alphabetically with Collections.sort(sW). This
 *   is displayed using JTextArea and each word is shown on a new line. It also focuses on showing the player's score (RHS). The ArrayList sW iterates through the sortedList and
 *   starts from the first node and continues till there is no more (null). The word we get is added to sW using .add().
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors; 

public class GUI extends JFrame {
    private JLabel puzzleLettersDisplay;
    private JTextArea foundWords;
    private JTextField userEntry;
    private JButton submitButton;
    private JPanel RHS;
    private String puzzleLetters; 
    private int score;
    private Set<String> guessedTracker; 
    private UnsortedWordList unsortedList;
    private SortedWordList sortedList; 
    private String[] answers;


    public GUI(String puzzleLetters, UnsortedWordList unsortedList, SortedWordList sortedList, String[] answers) { 
        this.puzzleLetters = puzzleLetters;
        this.answers = answers;
        this.score = 0;
        this.guessedTracker = new HashSet<>();
        this.unsortedList = unsortedList;
        this.sortedList = sortedList; 
        initializeComponents();
        createGUI();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // initializing GUI components
    private void initializeComponents() {
        puzzleLettersDisplay = new JLabel("Puzzle Letters: " + puzzleLetters);
        foundWords = new JTextArea(10, 20);
        userEntry = new JTextField(10);
        submitButton = new JButton("Submit");

        // Button Functionality
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                inputListen();
            }
        });
        
        // Initializing RHS (Right Hand Side of GUI)
        RHS = new JPanel(new BorderLayout());
        RHS.add(new JLabel("Words Found: "), BorderLayout.NORTH);
        RHS.add(new JScrollPane(foundWords), BorderLayout.CENTER);
        RHS.add(new JLabel("Score: " + score), BorderLayout.SOUTH);
    }

    // GUI Layout
    private void createGUI() {
        JPanel center = new JPanel(new BorderLayout());
        JPanel LHS = new JPanel(new GridLayout(3,1));
        LHS.add(puzzleLettersDisplay);
        JPanel P1 = new JPanel();
        P1.add(userEntry);
        P1.add(submitButton);
        LHS.add(P1);
        center.add(LHS, BorderLayout.WEST);
        center.add(RHS, BorderLayout.EAST);

        JMenuBar fileMenu = new JMenuBar();
        JMenu fileMain = new JMenu("File");
        JMenuItem uploadMenu = new JMenuItem("Open");
        JMenuItem quitMenu = new JMenuItem("Quit");

        fileMain.add(uploadMenu);
        fileMain.add(quitMenu);
        fileMenu.add(fileMain);
        setJMenuBar(fileMenu);

        FileMenuHandler handle = new FileMenuHandler(this);
        uploadMenu.addActionListener(handle);
        quitMenu.addActionListener(handle);
        setContentPane(center); 
    }

    // Event handling Class 
    public class FileMenuHandler implements ActionListener {
        private GUI theGUI;
    
        public FileMenuHandler(GUI gui) {
            this.theGUI = gui;
        }
    
        // 'Open' and 'Quit' Options Configuration
        @Override
        public void actionPerformed(ActionEvent v) {
            String cmd = v.getActionCommand();
            if (cmd.equals("Open")) {
                JFileChooser pick = new JFileChooser();
                int returnValue = pick.showOpenDialog(theGUI);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    theGUI.loadFile(pick.getSelectedFile().getAbsolutePath());
                }
            } else if (cmd.equals("Quit")) {
                System.exit(0);
            }
        }
    }

    // Function to handle uploaded file (solutions + letters)
    public void loadFile(String filePath) {
    try {
        // Read lines from the file specified by filePath
        Path txPath = Paths.get(filePath);
        List<String> lines = Files.readAllLines(txPath);

        // Verify there is atleast one minimum word to guess
        if (lines.size() < 2) {
            JOptionPane.showMessageDialog(this, "Invalid File Uploaded!");
            return;
        }

        // List created for proper solution words
        List<String> validAnswers = new ArrayList<>();

        // Method to verify if data can be added to validAnswers or is invalid
        for (String line : lines.stream().skip(1).collect(Collectors.toList())) {
            if (line.matches("[a-z]+")) {
                validAnswers.add(line);
            } else {
                System.out.println("Invalid word in data: " + line);
            }
        }

        // Update puzzle letters and answers with new information
        puzzleLetters = lines.get(0).toLowerCase();
        answers = validAnswers.toArray(new String[0]);

        // Update display for puzzle letters
        puzzleLettersDisplay.setText("Puzzle Letters: " + puzzleLetters);
        
        // Clear previous data
        guessedTracker.clear();
        unsortedList.clear();
        sortedList.clear();
        foundWords.setText("");
        
        // Continue adding words the unsortedList and sortedList AFTER uploading
        for (String answer : answers) {
            Word newWord = new Word(answer);
            unsortedList.add(newWord);
            sortedList.add(newWord);
        }
        // Reset score and update GUI
        score = 0;
        updateSWL();
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage());
    }
}

    // Helper Function to display winner message and allow for quitting or playing again 
    private void WinnerWinner() {
        if (guessedTracker.size() == answers.length) {
            int msg = JOptionPane.showConfirmDialog(this,"Congrats Winner! Play Again?", "CONGRATS!",JOptionPane.YES_NO_OPTION);
            if (msg == JOptionPane.YES_OPTION) {
                guessAgain();
            } else {
                System.exit(0);
            }
        }
    }

    // Helper Function to reset everything if player wants to play again
    private void guessAgain() {
        foundWords.setText("");
        userEntry.setText("");
        guessedTracker.clear();
        score = 0;
        RHS.removeAll();
        RHS.add(new JLabel("Words Found: "), BorderLayout.NORTH);
        RHS.add(new JScrollPane(foundWords), BorderLayout.CENTER);
        RHS.add(new JLabel("Score: " + score), BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    // Function to handle user input
    private void inputListen() {
        String userAttempt = userEntry.getText();
        try {
            // Check if guess is a valid word
            @SuppressWarnings("unused")
            Word wordWord = new Word(userAttempt);
            
            // Method to stop repeating guesses 
            if (guessedTracker.contains(userAttempt)) {
                JOptionPane.showMessageDialog(this, "Already guessed entered word!");
                userEntry.setText("");
                return;
            }
            
            // Method to track scoring and update Lists with words
            if (isCorrectAnswer(userAttempt)) {
                if (hasFirstLetter(userAttempt)) {
                    if (hasAllLetters(userAttempt)) {
                        score += 3;
                    } else {
                        score++;
                    }
                    unsortedList.append(new Word(userAttempt));
                    sortedList.add(new Word(userAttempt));
                    guessedTracker.add(userAttempt); 
                    updateSWL();
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect Guess. Check again!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect Guess. Check again!");
            }
            WinnerWinner();
        } catch (IllegalWordException e) { // Implementing custom exception in the program
            System.err.println("Incorrect! " + e.getMessage());
        }
    }
    
    // Helper Function to check if guessed word has first letter of puzzleLetters 
    private boolean hasFirstLetter(String userAttempt) {
        char firstLetter = puzzleLetters.charAt(0);
        return userAttempt.indexOf(firstLetter) != -1;
    }

    // Helper Function to check if guessed word contains all letters of puzzleLetters
    private boolean hasAllLetters(String userAttempt) {
        for(char letter : puzzleLetters.toCharArray()) {
            if (userAttempt.indexOf(letter) == -1) {
                return false;
            }
        }
        return true;
    }

    // Helper Function to check if the guessed word is correct 
    private boolean isCorrectAnswer(String userAttempt) {
        for (WordNode node = unsortedList.front.next; node != null; node = node.next) {
            if (node.data.getWord().equalsIgnoreCase(userAttempt)) {
                return true;
            }
        }
        return false;
    }

    // Helper Function to update GUI with each bit of information
    private void updateSWL() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RHS.removeAll();
                RHS.add(new JLabel("Words Found: "), BorderLayout.NORTH);
                
                List<String> sW = new ArrayList<>(guessedTracker);
                //sorts elements in order based on sW
                Collections.sort(sW);
                
                // Display the sorted words in the text area
                JTextArea sWTextArea = new JTextArea(10, 20);
                for (String word : sW) {
                    sWTextArea.append(word + "\n");
                }

                RHS.add(new JScrollPane(sWTextArea), BorderLayout.CENTER);
                RHS.add(new JLabel("Score: " + score), BorderLayout.SOUTH);

                // methods found below for visual updates
                revalidate();
                repaint();
            }
        });
    }
}