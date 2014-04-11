import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author nastra - Eduard Tudenhoefner
 * 
 */
public class BaseballElimination {
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] games;
    private Map<String, Integer> teamToId;
    private int maxWins = Integer.MIN_VALUE;
    private String leaderTeam;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        int teams = in.readInt();
        wins = new int[teams];
        losses = new int[teams];
        remaining = new int[teams];
        games = new int[teams][teams];

        teamToId = new HashMap<String, Integer>();
        for (int id = 0; id < teams; id++) {
            String team = in.readString();
            teamToId.put(team, id);
            wins[id] = in.readInt();
            losses[id] = in.readInt();
            remaining[id] = in.readInt();

            for (int j = 0; j < teams; j++) {
                games[id][j] = in.readInt();
            }
            if (wins[id] > maxWins) {
                maxWins = wins[id];
                leaderTeam = team;
            }
        }
    }

    public int numberOfTeams() {
        return teamToId.size();
    }

    public Iterable<String> teams() {
        return teamToId.keySet();
    }

    public int wins(String team) {
        if (!teamToId.containsKey(team)) {
            throw new IllegalArgumentException("The team is not known! Please specify a valid team name!");
        }
        return wins[teamToId.get(team)];
    }

    public int losses(String team) {
        if (!teamToId.containsKey(team)) {
            throw new IllegalArgumentException("The team is not known! Please specify a valid team name!");
        }
        return losses[teamToId.get(team)];
    }

    /**
     * @param team
     * @return Number of remaining games for given team
     */
    public int remaining(String team) {
        if (!teamToId.containsKey(team)) {
            throw new IllegalArgumentException("The team is not known! Please specify a valid team name!");
        }
        return remaining[teamToId.get(team)];
    }

    /**
     * 
     * @param team1
     * @param team2
     * @return Number of remaining games between team1 and team2
     */
    public int against(String team1, String team2) {
        if (!teamToId.containsKey(team1) || !teamToId.containsKey(team2)) {
            throw new IllegalArgumentException("The team is not known! Please specify a valid team name!");
        }
        return games[teamToId.get(team1)][teamToId.get(team2)];
    }

    public boolean isEliminated(String team) {
        if (!teamToId.containsKey(team)) {
            throw new IllegalArgumentException("The team is not known! Please specify a valid team name!");
        }
        int id = teamToId.get(team);
        if (triviallyEliminated(id)) {
            return true;
        }
        Graph graph = buildGraphFor(id);
        for (FlowEdge edge : graph.network.adj(graph.source)) {
            if (edge.flow() < edge.capacity()) {
                return true;
            }
        }
        return false;
    }

    private boolean triviallyEliminated(int id) {
        for (int i = 0; i < wins.length; i++) {
            if (i != id) {
                if (wins[id] + remaining[id] < wins[i]) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * To verify that you are returning a valid certificate of elimination R, compute a(R) = (w(R) + g(R)) / |R|, where w(R) is the total number of
     * wins of teams in R, g(R) is the total number of remaining games between teams in R, and |R| is the number of teams in R. Check that a(R) is
     * greater than the maximum number of games the eliminated team can win
     * 
     * @param team
     * @return The subset R of teams that eliminates given team; null if not eliminated
     */
    public Iterable<String> certificateOfElimination(String team) {
        if (!teamToId.containsKey(team)) {
            throw new IllegalArgumentException("The team is not known! Please specify a valid team name!");
        }
        Set<String> set = new HashSet<>();
        if (triviallyEliminated(teamToId.get(team))) {
            set.add(leaderTeam);
            return set;
        }
        Graph g = buildGraphFor(teamToId.get(team));
        for (FlowEdge edge : g.network.adj(g.source)) {
            if (edge.flow() < edge.capacity()) {
                for (String t : teams()) {
                    int id = teamToId.get(t);
                    if (g.ff.inCut(id)) {
                        set.add(t);
                    }
                }
            }
        }
        g = null;
        if (set.isEmpty()) {
            return null;
        }
        return set;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                    StdOut.print(t + " ");
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

    private Graph buildGraphFor(int id) {
        int n = numberOfTeams();
        int source = n;
        int sink = n + 1;
        int gameNode = n + 2;
        int currentMaxWins = wins[id] + remaining[id];
        Set<FlowEdge> edges = new HashSet<>();
        for (int i = 0; i < n; i++) {
            if (i == id || wins[i] + remaining[i] < maxWins) {
                continue;
            }

            for (int j = 0; j < i; j++) {
                if (j == id || games[i][j] == 0 || wins[j] + remaining[j] < maxWins) {
                    continue;
                }

                edges.add(new FlowEdge(source, gameNode, games[i][j]));
                edges.add(new FlowEdge(gameNode, i, Double.POSITIVE_INFINITY));
                edges.add(new FlowEdge(gameNode, j, Double.POSITIVE_INFINITY));
                gameNode++;
            }
            edges.add(new FlowEdge(i, sink, currentMaxWins - wins[i]));
        }

        FlowNetwork network = new FlowNetwork(gameNode);
        for (FlowEdge edge : edges) {
            network.addEdge(edge);
        }
        FordFulkerson ff = new FordFulkerson(network, source, sink);
        return new Graph(ff, network, source, sink);
    }

    private class Graph {
        FordFulkerson ff;
        FlowNetwork network;
        int source;
        int sink;

        public Graph(FordFulkerson ff, FlowNetwork network, int source, int sink) {
            super();
            this.ff = ff;
            this.network = network;
            this.source = source;
            this.sink = sink;
        }
    }
}
