public class Word {
    private String word;

    // Constructor to initialize the word
    public Word(String word) throws IllegalWordException {
        if (!word.matches("[a-z]+")) {  // regex check for special characters guess
            throw new IllegalWordException("Illegal Word: " + word);
        }
        // regex check for capital letter guess
        if (word.matches(".*[A-Z].*")) {
            throw new IllegalWordException("Illegal Word: " + word);
        }
        this.word = word;
    }    
    
    // Getter method
    public String getWord() {
        return word;
    }
}

class WordNode {
    protected Word data; // Node containing the word
    protected WordNode next; // Reference to the NEXT WordNode

    // Constructor to initialize WordNode with a word
    public WordNode(Word w) {
        data = w;
        next = null; 
    }
}

// Parent class to be used by WordList classes
abstract class WordList {
    protected WordNode front; // refers to first node
    protected WordNode back; // refers to last node
    protected int length;

    // No-argument Constructor
    public WordList() {
        front = new WordNode(null); // Initialize empty list with extra/temp node
        back = front;
        length = 0;
    }

    public void append(Word word) {
        // Method to append a word to the end of list
        WordNode newNode = new WordNode(word); // create new node with entry word
        back.next = newNode; 
        back = newNode; // updates and points to last node
        length++;
    }

// Helper Function to clear lists
    public void clear() {
        front.next = null; 
        back = front;
        length = 0;
    }
}

class UnsortedWordList extends WordList {
    public UnsortedWordList() {
        super();
    }

    public void add(Word word) {
        append(word); // add a word to unsorted list
    }
}

class SortedWordList extends WordList {
    public SortedWordList() {
        super();
    }

    public void add(Word word) {
        // Method to add a word to sorted list in *alphabetical* order
        WordNode newNode = new WordNode(word); // create new node with entry word
        WordNode current = front.next; // starts from first node in list
        WordNode prev = front; // backtrack node 

    // Sift through list for correct position for new word 
    // Continues to "sift" as long as nodes exist 
    // .compareTo() used to sort as list is sifted
        while (current != null && current.data.getWord().compareTo(word.getWord()) < 0) {
            prev = current; // updates variable to current node
            current = current.next; // updates to next node to allow for comparison
        }

        // Inserts the new word into list
        newNode.next = current; // connect new node to current node
        prev.next = newNode; // connect previous node to new node
        if (current == null) {
            back = newNode; // updates to new node at end IF null 
        }
        length++;
    }
}
