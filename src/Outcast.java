/**
 * Outcast detection. Given a list of wordnet nouns A1, A2, ..., An, which noun is the least related to the others? To identify an outcast, compute
 * the sum of the distances between each noun and every other one:
 * 
 * di = dist(Ai, A1) + dist(Ai, A2) + ... + dist(Ai, An) and return a noun At for which dt is maximum.
 * 
 * @author nastra - Eduard Tudenhoefner
 * 
 */
public class Outcast {
    public Outcast(WordNet wordnet) {

    }

    /**
     * given an array of WordNet nouns, return an outcast
     * 
     * @param nouns
     * @return
     */
    public String outcast(String[] nouns) {
        return null;
    }

    /**
     * for unit testing of this class (such as the one below)
     * 
     * @param args
     */
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
