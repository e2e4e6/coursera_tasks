import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BaseballElimination {
    private final String[] teamNames;
    private final int[] w;
    private final int[] l;
    private final int[] r;
    private final int[][] g;

    private int getTeamIndexBy(String name) {
        for (int i = 0; i < teamNames.length; i++) {
            if (name.equals(teamNames[i])) {
                return i;
            }
        }

        throw new IllegalArgumentException("Unknown team.");
    }

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);

        int teamCount = in.readInt();

        teamNames = new String[teamCount];
        w = new int[teamCount];
        l = new int[teamCount];
        r = new int[teamCount];
        g = new int[teamCount][teamCount];

        for (int i = 0; i < teamCount; i++) {
            teamNames[i] = in.readString();
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < teamCount; j++) {
                g[i][j] = in.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return teamNames.length;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(teamNames);
    }

    // number of wins for given team
    public int wins(String team) {
        return w[getTeamIndexBy(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        return l[getTeamIndexBy(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        return r[getTeamIndexBy(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        return g[getTeamIndexBy(team1)][getTeamIndexBy(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        return null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
