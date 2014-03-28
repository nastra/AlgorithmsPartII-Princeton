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
    private WordNet net;

    public Outcast(WordNet wordnet) {
        net = wordnet;
    }

    /**
     * @param nouns
     * @return given an array of WordNet nouns, return an outcast
     */
    public String outcast(String[] nouns) {
        String outcast = null;
        int max = 0;

        for (String nounA : nouns) {
            int dist = 0;
            for (String nounB : nouns) {
                if (!nounA.equals(nounB)) {
                    dist += net.distance(nounA, nounB);
                }
            }
            if (dist > max) {
                max = dist;
                outcast = nounA;
            }
        }
        return outcast;
    }

    /**
     * for unit testing of this class (such as the one below)
     * 
     * @param args
     */
    public static void main(String[] args) {
//        long start = System.nanoTime();
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);

        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
        // long end = System.nanoTime();
        // System.out.println("Duration: " + (end - start) / 1000000);
    }
}
