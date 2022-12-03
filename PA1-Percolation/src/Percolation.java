import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF uf, uf1;
    private final boolean[][] grid;
    private final int size;
    private int openNum = 0;
    private final int[][] move = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        size = n;
        grid = new boolean[n + 1][n + 1];
        uf = new WeightedQuickUnionUF(n * n + 2);
        uf1 = new WeightedQuickUnionUF(n * n + 1); // no bottom virtual node,
        if (n == 1) return;
        for (int i = 0; i < n; i++) {
            uf.union(0, 1 + i);
            uf.union(n * n + 1, n * n - i);
            uf1.union(0, 1 + i);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!validate(row, col)) throw new IllegalArgumentException();
        if (grid[row][col]) return;
        grid[row][col] = true;
        openNum++;
        for (int[] ints : move) {
            int i = row + ints[0], j = col + ints[1];
            if (validate(i, j) && grid[i][j]) {
                uf.union(index(row, col), index(i, j));
                uf1.union(index(row, col), index(i, j));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!validate(row, col)) throw new IllegalArgumentException();
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!validate(row, col)) throw new IllegalArgumentException();
        if (size == 1) return grid[1][1];
        return grid[row][col] && uf1.find(0) == uf1.find(index(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openNum;
    }

    // does the system percolate?
    public boolean percolates() {
        if (size == 1) return grid[1][1];
        return uf.find(0) == uf.find(size * size + 1);
    }

    private int index(int row, int col) {
        if (row <= 0 || row > size || col <= 0 || col > size) {
            throw new IllegalArgumentException();
        }
        return size * (row - 1) + col;
    }

    private boolean validate(int row, int col) {
        return row > 0 && row <= size && col > 0 && col <= size;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(8);
        int[] open = {3, 4, 5, 9, 12, 13, 14, 15, 16, 17, 18, 19, 22, 23, 27, 28,
                30, 31, 32, 34, 35, 36, 38, 39, 42, 47, 48, 49, 51, 53, 54, 55,
                56, 57, 58, 59, 60, 62};
        for (int index : open) {
            int row = (index % 8 == 0) ? (index / 8) : (index / 8 + 1);
            int col = (index % 8 == 0) ? 8 : (index % 8);
            p.open(row, col);
        }
        System.out.println(p.numberOfOpenSites() + "|expected:" + open.length);
        System.out.println(p.percolates() + "|expected:true");
        System.out.println(p.isFull(6, 8) + "|expected:true");
        System.out.println(p.isFull(8, 1) + "|expected:false");
        System.out.println(p.isOpen(2, 1) + "|expected:true");
        System.out.println(p.isOpen(8, 8) + "|expected:false");
    }
}
