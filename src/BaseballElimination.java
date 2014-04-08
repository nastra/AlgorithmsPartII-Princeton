import java.util.HashMap;
import java.util.Map;

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

    public BaseballElimination(String filename) {
        In in = new In(filename);
        int teams = in.readInt();
        wins = new int[teams];
        losses = new int[teams];
        remaining = new int[teams];

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
        return false;
    }

    /**
     * @param team
     * @return The subset R of teams that eliminates given team; null if not eliminated
     */
    public Iterable<String> certificateOfElimination(String team) {
        if (!teamToId.containsKey(team)) {
            throw new IllegalArgumentException("The team is not known! Please specify a valid team name!");
        }
        return null;
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

}
