import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private final Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        graph = new Digraph(G);
    }

    // length of the shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= graph.V() || w < 0 || w >= graph.V()) {
            throw new IllegalArgumentException();
        }
        return bfs(v, w)[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= graph.V() || w < 0 || w >= graph.V()) {
            throw new IllegalArgumentException();
        }
        return bfs(v, w)[1];
    }

    // length of the shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (!validate(v, w)) throw new IllegalArgumentException();
        return bfs(v, w)[0];
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (!validate(v, w)) throw new IllegalArgumentException();
        return bfs(v, w)[1];
    }

    private boolean validate(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) return false;
        for (Integer i : v) {
            if (i == null || i < 0 || i >= graph.V()) return false;
        }
        for (Integer i : w) {
            if (i == null || i < 0 || i >= graph.V()) return false;
        }
        return true;
    }

    private int[] bfs(int v, int w) {
        if (v == w) return new int[]{0, v};
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);
        int[] ans = {Integer.MAX_VALUE, -1};
        for (int i = 0; i < graph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int dist = bfsV.distTo(i) + bfsW.distTo(i);
                if (dist < ans[0]) {
                    ans[0] = dist;
                    ans[1] = i;
                }
            }
        }
        if (ans[0] == Integer.MAX_VALUE) ans[0] = -1;
        return ans;
    }

    private int[] bfs(Iterable<Integer> v, Iterable<Integer> w) {
        if (isEmpty(v) || isEmpty(w)) return new int[]{-1, -1};
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);
        int[] ans = {Integer.MAX_VALUE, -1};
        for (int i = 0; i < graph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int dist = bfsV.distTo(i) + bfsW.distTo(i);
                if (dist < ans[0]) {
                    ans[0] = dist;
                    ans[1] = i;
                }
            }
        }
        if (ans[0] == Integer.MAX_VALUE) ans[0] = -1;
        return ans;
    }

    private boolean isEmpty(Iterable<Integer> it) {
        for (Integer ignored : it) return false;
        return true;
    }

    // test client
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
}
