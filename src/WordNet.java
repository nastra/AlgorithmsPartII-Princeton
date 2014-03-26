/**
 * 
 * @author nastra - Eduard Tudenhoefner
 * 
 */
public class WordNet {
    /**
     * constructor takes the name of the two input files
     * 
     * @param synsets
     *            the sets of synonyms
     * @param hypernyms
     *            (more general synonym sets)
     */
    public WordNet(String synsets, String hypernyms) {

    }

    /**
     * @return the set of nouns (no duplicates), returned as an Iterable
     */
    public Iterable<String> nouns() {
        return null;
    }

    /**
     * @param word
     * @return is the word a WordNet noun?
     */
    public boolean isNoun(String word) {
        return false;
    }

    /**
     * The distance between nounA and nounB. The distance is defined as following:<br>
     * distance(A, B) = distance is the minimum length of any ancestral path between any synset v of A and any synset w of B.
     * 
     * @param nounA
     * @param nounB
     * @return The distance between nounA and nounB
     */
    public int distance(String nounA, String nounB) {
        return 0;
    }

    /**
     * An ancestral path between two vertices v and w in a digraph is a directed path from v to a common ancestor x, together with a directed path
     * from w to the same ancestor x. A shortest ancestral path is an ancestral path of minimum total length.
     * 
     * @param nounA
     * @param nounB
     * @return a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB in a shortest ancestral path
     */
    public String sap(String nounA, String nounB) {
        return null;
    }

    /**
     * for unit testing of this class
     * 
     * @param args
     */
    public static void main(String[] args) {

    }
}
