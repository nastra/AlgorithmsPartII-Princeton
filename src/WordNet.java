import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author nastra - Eduard Tudenhoefner
 * 
 */
public class WordNet {
    private final SAP sap;
    private final Map<Integer, String> idToSynset;
    private final Map<String, Set<Integer>> nounToIds;

    /**
     * constructor takes the name of the two input files
     * 
     * @param synsets
     *            the sets of synonyms
     * @param hypernyms
     *            (more general synonym sets)
     */
    public WordNet(String synsets, String hypernyms) {
        idToSynset = new HashMap<>();
        nounToIds = new HashMap<>();
        initSynsets(synsets);
        Digraph graph = initHypernyms(hypernyms);

        DirectedCycle cycle = new DirectedCycle(graph);
        if (cycle.hasCycle() || !rootedDAG(graph)) {
            throw new IllegalArgumentException("The input does not correspond to a rooted DAG!");
        }

        sap = new SAP(graph);
    }

    private boolean rootedDAG(Digraph g) {
        int roots = 0;
        for (int i = 0; i < g.V(); i++) {
            if (!g.adj(i).iterator().hasNext()) {
                roots++;
            }
        }

        return roots == 1;
    }

    private void initSynsets(String synset) {
        In file = new In(synset);
        while (file.hasNextLine()) {
            String[] line = file.readLine().split(",");
            Integer id = Integer.valueOf(line[0]);
            String n = line[1];
            idToSynset.put(id, n);

            String[] nouns = n.split(" ");
            // String definition = line[2];
            for (String noun : nouns) {
                Set<Integer> ids = nounToIds.get(noun);
                if (null == ids) {
                    ids = new HashSet<>();
                }
                ids.add(id);
                nounToIds.put(noun, ids);
            }
        }
    }

    private Digraph initHypernyms(String hypernyms) {
        Digraph graph = new Digraph(idToSynset.size());

        In file = new In(hypernyms);
        while (file.hasNextLine()) {
            String[] line = file.readLine().split(",");
            Integer synsetId = Integer.valueOf(line[0]);
            for (int i = 1; i < line.length; i++) {
                Integer id = Integer.valueOf(line[i]);
                graph.addEdge(synsetId, id);
            }
        }

        return graph;
    }

    /**
     * @return the set of nouns (no duplicates), returned as an Iterable
     */
    public Iterable<String> nouns() {
        return nounToIds.keySet();
    }

    /**
     * @param word
     * @return is the word a WordNet noun?
     */
    public boolean isNoun(String word) {
        if (null == word || "".equals(word)) {
            return false;
        }
        return nounToIds.containsKey(word);
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
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Both words must be nouns!");
        }
        Set<Integer> idsOfNounA = nounToIds.get(nounA);
        Set<Integer> idsOfNounB = nounToIds.get(nounB);
        return sap.length(idsOfNounA, idsOfNounB);
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
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Both words must be nouns!");
        }
        Set<Integer> idsOfNounA = nounToIds.get(nounA);
        Set<Integer> idsOfNounB = nounToIds.get(nounB);
        int ancestor = sap.ancestor(idsOfNounA, idsOfNounB);
        return idToSynset.get(ancestor);
    }

    /**
     * for unit testing of this class
     * 
     * @param args
     */
    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        while (!StdIn.isEmpty()) {
            String v = StdIn.readString();
            String w = StdIn.readString();
            if (!wordNet.isNoun(v)) {
                StdOut.println(v + " not in the word net");
                continue;
            }
            if (!wordNet.isNoun(w)) {
                StdOut.println(w + " not in the word net");
                continue;
            }
            int distance = wordNet.distance(v, w);
            String ancestor = wordNet.sap(v, w);
            StdOut.printf("distance = %d, ancestor = %s\n", distance, ancestor);
        }
    }
}
