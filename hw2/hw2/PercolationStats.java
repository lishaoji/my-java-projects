package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] probs;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        if (N < 1 || T < 1) {
            throw new IllegalArgumentException("Non-positive input");
        } else {
            probs = new double[T];
            for (int t = 0; t < T; t += 1) {
                Percolation p = new Percolation(N);
                int opCount = 0;
                while (!p.percolates()) {
                    int randomRow = StdRandom.uniform(N);
                    int randomCol = StdRandom.uniform(N);
                    if (!p.isOpen(randomRow, randomCol)) {
                        p.open(randomRow, randomCol);
                        opCount += 1;
                    }
                }
                probs[t] = (double) opCount / (double) (N * N);
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(probs);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(probs);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLow() {
        return (mean() - 1.96 * stddev() / Math.sqrt(probs.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return (mean() + 1.96 * stddev() / Math.sqrt(probs.length));
    }

    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(20, 10);
        System.out.println(ps.mean());
        System.out.println(ps.stddev());
        System.out.println(ps.confidenceLow());
        System.out.println(ps.confidenceHigh());
    }
}                       
