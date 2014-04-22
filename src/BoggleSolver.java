public class BoggleSolver {
    /**
     * Initializes the data structure using the given array of strings as the dictionary. (You can assume each word in the dictionary contains only
     * the uppercase letters A through Z.)
     * 
     * @param dictionary
     */
    public BoggleSolver(String[] dictionary) {

    }

    /**
     * Returns the set of all valid words in the given Boggle board, as an Iterable.
     * 
     * @param board
     * @return The set of all valid words in the given Boggle board, as an Iterable.
     */
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        return null;
    }

    /**
     * Returns the score of the given word if it is in the dictionary, zero otherwise. (You can assume the word contains only the uppercase letters A
     * through Z.)
     * 
     * @param word
     * @return The score of the given word if it is in the dictionary, zero otherwise. (You can assume the word contains only the uppercase letters A
     *         through Z.)
     */
    public int scoreOf(String word) {
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}