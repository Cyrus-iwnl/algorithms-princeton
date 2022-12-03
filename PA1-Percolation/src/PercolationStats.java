import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double[] res;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        res = new double[trials];
        for (int t = 0; t < trials; t++) {
            int ans = doTrial(n);
            res[t] = (double) ans / (n * n);
        }
    }

    private int doTrial(int n) {
        Percolation p = new Percolation(n);
        while (true) {
            int index, row, col;
            do {
                index = StdRandom.uniform(n * n) + 1;
                row = (index % n == 0) ? (index / n) : (index / n + 1);
                col = (index % n == 0) ? n : (index % n);
            } while (p.isOpen(row, col));
            p.open(row, col);
            if (p.percolates()) {
                return p.numberOfOpenSites();
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(res);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(res);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(res.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(res.length);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]), t = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, t);
        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}