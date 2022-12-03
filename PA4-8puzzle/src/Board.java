import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int[][] grid;
    private final int N;

    // create a board from an N-by-N array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        N = tiles.length;
        grid = new int[N][N];
        for (int i = 0; i < N; i++) {
            grid[i] = tiles[i].clone();
        }
    }

    // string representation of this board
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(N).append(System.lineSeparator());
        for (int[] line : grid) {
            for (int col = 0; col < N; col++) {
                sb.append(" ").append(line[col]);
            }
            sb.append(System.lineSeparator());
        }
        return sb.substring(0, sb.length() - System.lineSeparator().length());
    }

    // board dimension N
    public int dimension() {
        return N;
    }

    // number of tiles out of place
    public int hamming() {
        int ans = 0, check = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                check++;
                if (grid[i][j] == 0) continue;
                if (grid[i][j] != check) ans++;
            }
        }
        return ans;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int ans = 0, check = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                check++;
                if (grid[i][j] == 0) continue;
                if (grid[i][j] != check) ans += distance(i, j);
            }
        }
        return ans;
    }

    // distance from tile (i,j) to the goal position
    private int distance(int i, int j) {
        int num = grid[i][j];
        int x = (num % N == 0) ? (num / N - 1) : (num / N);
        int y = (num % N == 0) ? (N - 1) : (num % N - 1);
        return Math.abs(i - x) + Math.abs(j - y);
    }

    // is this board the goal board?
    public boolean isGoal() {
        int check = 0;
        if (grid[N - 1][N - 1] != 0) return false;
        for (int i = 0; i < N - 1; i++) {
            for (int j = 0; j < N; j++) {
                check++;
                if (grid[i][j] != check) return false;
            }
        }
        for (int i = 0; i < N - 1; i++) {
            check++;
            if (grid[N - 1][i] != check) return false;
        }
        return true;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (this == y) return true; // the same object
        if (y == null || getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (N != that.dimension()) return false;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] != that.grid[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int row = -1, col = -1;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == 0) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }
        List<Board> ans = new ArrayList<>();
        int[][] move = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int i = 0; i < 4; i++) {
            int row1 = row + move[i][0], col1 = col + move[i][1];
            if (validate(row1, col1)) {
                swap(row, col, row1, col1);
                ans.add(new Board(grid));
                swap(row, col, row1, col1);
            }
        }
        return ans;
    }

    private boolean validate(int row, int col) {
        return (row >= 0 && row < grid.length && col >= 0 && col < grid.length);
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int p = -1, q = -1, x = -1, y = -1;
        // find the first tile
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] != 0) {
                    p = i;
                    q = j;
                    break;
                }
            }
            if (p != -1) break;
        }
        // find the second tile
        for (int i = p; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if ((i != p || j != q) && grid[i][j] != 0) {
                    x = i;
                    y = j;
                    break;
                }
            }
            if (x != -1) break;
        }
        swap(p, q, x, y);
        Board ans = new Board(grid);
        swap(p, q, x, y);
        return ans;
    }

    private void swap(int i, int j, int a, int b) {
        int t = grid[i][j];
        grid[i][j] = grid[a][b];
        grid[a][b] = t;
    }

}