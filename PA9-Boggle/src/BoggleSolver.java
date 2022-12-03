import edu.princeton.cs.algs4.SET;

public class BoggleSolver {
    private static final int[][] MOVE = {{-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}};
    private BoggleBoard myBoard;
    private int rows, cols;
    private Node root;
    private SET<String> validWords;
    private StringBuilder path;
    private boolean[] marked;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        root = new Node();
        for (String s : dictionary) put(s);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        validWords = new SET<>();
        myBoard = board;
        rows = board.rows();
        cols = board.cols();
        for (int v = 0; v < rows * cols; v++) {
            path = new StringBuilder();
            marked = new boolean[rows * cols];
            marked[v] = true;
            char c = myBoard.getLetter(v / cols, v % cols);
            if (c == 'Q') {
                path.append("QU");
                dfs(v, root.next[16].next[20]);
            } else {
                path.append(c);
                dfs(v, root.next[c - 'A']);
            }
        }
        return validWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (contains(word) && word.length() >= 3) {
            int len = word.length();
            if (len == 3 || len == 4) return 1;
            else if (len == 5) return 2;
            else if (len == 6) return 3;
            else if (len == 7) return 5;
            else return 11;
        }
        return 0;
    }

    private void dfs(int v, Node x) {
        if (path.length() >= 3 && x != null && x.isEnd) {
            validWords.add(path.toString());
        }
        int vr = v / cols, vc = v % cols;
        for (int i = 0; i < 8; i++) {
            int row = vr + MOVE[i][0], col = vc + MOVE[i][1];
            if (!isValid(row, col)) continue;
            int w = row * cols + col;
            char c = myBoard.getLetter(row, col);
            if (!marked[w] && x != null && x.next[c - 'A'] != null) {
                marked[w] = true;
                if (c == 'Q') {
                    path.append("QU");
                    dfs(w, x.next[16].next[20]);
                } else {
                    path.append(c);
                    dfs(w, x.next[c - 'A']);
                }
                marked[w] = false;
                path.deleteCharAt(path.length() - 1);
                if (c == 'Q') path.deleteCharAt(path.length() - 1);
            }
        }
    }

    private boolean isValid(int i, int j) {
        return i >= 0 && i < rows && j >= 0 && j < cols;
    }

    private static class Node {
        private boolean isEnd;
        private final Node[] next = new Node[26];
    }

    private boolean contains(String key) {
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.isEnd;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        int c = key.charAt(d) - 'A';
        return get(x.next[c], key, d + 1);
    }

    private void put(String key) {
        root = put(root, key, 0);
    }

    private Node put(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.isEnd = true;
            return x;
        }
        int c = key.charAt(d) - 'A';
        x.next[c] = put(x.next[c], key, d + 1);
        return x;
    }
}