import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Optimization:
// 1. Avoid enqueuing the same search node on PQ
// 2. Cache priority in Node instance
public class Solver {

    private final List<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        MinPQ<Node> pq = new MinPQ<>(manhattanPriority());
        MinPQ<Node> pq2 = new MinPQ<>(manhattanPriority());
        pq.insert(new Node(initial, 0, null));
        pq2.insert(new Node(initial.twin(), 0, null));
        Node min = pq.delMin(), min2 = pq2.delMin();
        while (!min.board.isGoal() && !min2.board.isGoal()) {
            // initial game tree
            for (Board neighbor : min.board.neighbors()) {
                if (min.pre == null || !neighbor.equals(min.pre.board)) {
                    pq.insert(new Node(neighbor, min));
                }
            }
            min = pq.delMin();
            // twin game tree
            for (Board neighbor : min2.board.neighbors()) {
                if (min2.pre == null || !neighbor.equals(min2.pre.board)) {
                    pq2.insert(new Node(neighbor, min2));
                }
            }
            min2 = pq2.delMin();
        }
        // Unsolvable Puzzle
        if (min2.board.isGoal()) {
            solution = null;
            return;
        }
        // Solvable Puzzle
        List<Board> ans = new ArrayList<>();
        while (min != null) {
            ans.add(min.board);
            min = min.pre;
        }
        solution = new ArrayList<>();
        for (int i = ans.size() - 1; i >= 0; i--) {
            solution.add(ans.get(i));
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return solution.size() - 1;
    }

    // sequence of boards in the shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        return new ArrayList<>(solution);
    }

    private static class Node {
        Board board;
        int move, priority;
        Node pre;

        Node(Board board, int move, Node pre) {
            this.board = board;
            this.move = move;
            this.pre = pre;
            priority = board.manhattan() + move;
        }

        Node(Board board, Node pre) {
            this.board = board;
            this.pre = pre;
            move = pre.move + 1;
            priority = board.manhattan() + move;
        }
    }

    private Comparator<Node> manhattanPriority() {
        return Comparator.comparing(o -> o.priority);
    }

    // test client
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        // solve the puzzle
        Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}