import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseballElimination {

    private final HashMap<Integer, List<Integer>> certificate;
    private final HashMap<String, Integer> teams;
    private final boolean[] isChecked;
    private final boolean[] isEliminated;
    private final int[] win;
    private final int[] loss;
    private final int[] remain;
    private final int[][] games;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        certificate = new HashMap<>();
        teams = new HashMap<>();
        In in = new In(filename);
        int n = in.readInt();
        win = new int[n];
        loss = new int[n];
        remain = new int[n];
        games = new int[n][n];
        isEliminated = new boolean[n];
        isChecked = new boolean[n];
        for (int i = 0; i < n; i++) {
            teams.put(in.readString(), i);
            win[i] = in.readInt();
            loss[i] = in.readInt();
            remain[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                games[i][j] = in.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return teams.size();
    }

    // all teams
    public Iterable<String> teams() {
        return teams.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        validate(team);
        return win[teams.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        validate(team);
        return loss[teams.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        validate(team);
        return remain[teams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        validate(team1);
        validate(team2);
        return games[teams.get(team1)][teams.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        validate(team);
        int x = teams.get(team);
        // team has been checked
        if (isChecked[x]) return isEliminated[x];
        else isChecked[x] = true;
        // Trivial elimination
        int maxWin = win[x] + remain[x];
        for (int i = 0; i < teams.size(); i++) {
            if (maxWin < win[i]) {
                isEliminated[x] = true;
                certificate.put(x, List.of(i));
                return true;
            }
        }
        // Nontrivial elimination
        List<Integer> others = new ArrayList<>(teams.values());
        others.remove((Integer) x);
        int size = others.size();
        int v = (int) (2 + size / 2.0 + size * size / 2.0);
        // construct the flow network
        // v-2 -> S, v-1 -> T, [0,size-1] -> others.index
        FlowNetwork fn = new FlowNetwork(v);
        // for each game vertex
        for (int i = size; i <= v - 3; i++) {
            int[] teamVertex = teamVertexOf(i, size);
            int t1 = teamVertex[0], t2 = teamVertex[1];
            fn.addEdge(new FlowEdge(i, t1, Double.POSITIVE_INFINITY));
            fn.addEdge(new FlowEdge(i, t2, Double.POSITIVE_INFINITY));
            fn.addEdge(new FlowEdge(v - 2, i, games[others.get(t1)][others.get(t2)]));
        }
        // for each team vertex
        for (int i = 0; i < size; i++) {
            int t = others.get(i);
            fn.addEdge(new FlowEdge(i, v - 1, win[x] + remain[x] - win[t]));
        }
        // maximum flow / minimum cut
        FordFulkerson ff = new FordFulkerson(fn, v - 2, v - 1);
        List<Integer> subset = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (ff.inCut(i)) subset.add(others.get(i));
        }
        certificate.put(x, subset);
        for (FlowEdge e : fn.adj(v - 2)) {
            if (Double.compare(e.flow(), e.capacity()) != 0) {
                isEliminated[x] = true;
                return true;
            }
        }
        isEliminated[x] = false;
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validate(team);
        int x = teams.get(team);
        if (isChecked[x]) {
            if (isEliminated[x]) return certificateOf(x);
            else return null;
        } else {
            if (!isEliminated(team)) return null;
            return certificateOf(x);
        }
    }

    private List<String> certificateOf(int index) {
        List<String> ans = new ArrayList<>();
        List<Integer> list = certificate.get(index);
        for (String team : teams.keySet()) {
            if (list.contains(teams.get(team))) ans.add(team);
        }
        return ans;
    }

    private int[] teamVertexOf(int v, int size) {
        int a = v - size + 1;
        int[] ans = new int[2];
        int left, right = 0;
        for (int i = 1; i <= size; i++) {
            left = right + 1;
            right += size - i;
            if (left <= a && a <= right) {
                ans[0] = i - 1;
                ans[1] = ans[0] + a - left + 1;
                break;
            }
        }
        return ans;
    }

    private void validate(String team) {
        if (team == null) throw new IllegalArgumentException("team is null");
        if (!teams.containsKey(team)) throw new IllegalArgumentException("team not known");
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
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
        char c = '1';
        Integer.valueOf(c);
    }
}