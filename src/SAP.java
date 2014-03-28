import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author nastra - Eduard Tudenhoefner
 * 
 */
public class SAP {
    private Digraph graph;

    public SAP(Digraph G) {
        graph = new Digraph(G);
    }

    private boolean validIndex(int i) {
        if (i < 0 || i >= graph.V()) {
            return false;
        }
        return true;
    }

    private boolean validIndex(Iterable<Integer> it) {
        for (Integer i : it) {
            if (!validIndex(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 
     * @param v
     * @param w
     * @return length of shortest ancestral path between v and w; -1 if no such path exists
     */
    public int length(int v, int w) {
        if (!validIndex(v) || !validIndex(w)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return new SAPProcessor(v, w).distance;
    }

    /**
     * 
     * @param v
     * @param w
     * @return a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path exists
     */
    public int ancestor(int v, int w) {
        if (!validIndex(v) || !validIndex(w)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        // TODO: add caching of the results
        return new SAPProcessor(v, w).ancestor;
    }

    /**
     * 
     * @param v
     * @param w
     * @return length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path exists
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (!validIndex(v) || !validIndex(w)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return new SAPProcessor(v, w).distance;
    }

    /**
     * 
     * @param v
     * @param w
     * @return a common ancestor that participates in shortest ancestral path; -1 if no such path exists
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (!validIndex(v) || !validIndex(w)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return new SAPProcessor(v, w).ancestor;
    }

    /**
     * for unit testing of this class (such as the one below)
     * 
     * @param args
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

    private class SAPProcessor {
        int ancestor;
        int distance;

        public SAPProcessor(int v, int w) {
            BreadthFirstDirectedPaths a = new BreadthFirstDirectedPaths(graph, v);
            BreadthFirstDirectedPaths b = new BreadthFirstDirectedPaths(graph, w);

            List<Integer> ancestors = new ArrayList<>();
            for (int i = 0; i < graph.V(); i++) {
                if (a.hasPathTo(i) && b.hasPathTo(i)) {
                    ancestors.add(i);
                }
            }

            int shortestAncestor = -1;
            int minDistance = Integer.MAX_VALUE;
            for (int ancestor : ancestors) {
                int dist = a.distTo(ancestor) + b.distTo(ancestor);
                if (dist < minDistance) {
                    minDistance = dist;
                    shortestAncestor = ancestor;
                }
            }
            if (Integer.MAX_VALUE == minDistance) {
                distance = -1;
            } else {
                distance = minDistance;

            }
            ancestor = shortestAncestor;
        }

        public SAPProcessor(Iterable<Integer> v, Iterable<Integer> w) {
            BreadthFirstDirectedPaths a = new BreadthFirstDirectedPaths(graph, v);
            BreadthFirstDirectedPaths b = new BreadthFirstDirectedPaths(graph, w);

            List<Integer> ancestors = new ArrayList<>();
            for (int i = 0; i < graph.V(); i++) {
                if (a.hasPathTo(i) && b.hasPathTo(i)) {
                    ancestors.add(i);
                }
            }

            int shortestAncestor = -1;
            int minDistance = Integer.MAX_VALUE;
            for (int ancestor : ancestors) {
                int dist = a.distTo(ancestor) + b.distTo(ancestor);
                if (dist < minDistance) {
                    minDistance = dist;
                    shortestAncestor = ancestor;
                }
            }
            if (Integer.MAX_VALUE == minDistance) {
                distance = -1;
            } else {
                distance = minDistance;

            }
            ancestor = shortestAncestor;
        }
    }
}
