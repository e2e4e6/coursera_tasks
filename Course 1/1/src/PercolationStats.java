import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("n <= 0");
        }

        if (trials <= 0) {
            throw new IllegalArgumentException("trials <= 0");
        }

        double[] sampleMeanList = new double[trials];
        _trials = trials;

        for (int i = 0; i < trials; ++i) {
            Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                }
            }

            sampleMeanList[i] = ((double)percolation.numberOfOpenSites())/(double) (n * n);
        }

        _mean = StdStats.mean(sampleMeanList);
        _stddev = StdStats.stddev(sampleMeanList);
    }

    // sample mean of percolation threshold
    public double mean() {
        return _mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return _stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * _stddev / Math.sqrt(_trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * _stddev / Math.sqrt(_trials);
    }

    // test client (described below)
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Wrong arguments count.");
        }

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);
        StdOut.printf("mean                    = %.16f", stats.mean());
        StdOut.println();
        StdOut.printf("stddev                  = %.16f", stats.stddev());
        StdOut.println();
        StdOut.printf("95%% confidence interval = [%.16f, %.16f]", stats.confidenceLo(), stats.confidenceHi());
    }

    private static final double CONFIDENCE_95 = 1.96;

    private final double _trials;
    private final double _mean;
    private final double _stddev;
}