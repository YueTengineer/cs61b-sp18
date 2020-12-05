package hw2;

import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private double mean;
    private double stddev;
    private double confidencelow;
    private double confidencehigh;
    private int [] openls;
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N and T should be greater than zero.");
        }
        // calculate the mean.
        openls = new int[T];
        double mean_sum = 0;
        for (int i = 0; i < T; i += 1) {
            Percolation temp = pf.make(N);
            while (!temp.percolates()) {
                temp.open((int) StdRandom.uniform(N), (int) StdRandom.uniform(N));
            }
            openls[i] = temp.numberOfOpenSites() / (N * N);
            mean_sum += temp.numberOfOpenSites() / (N * N);
        }
        mean = mean_sum / T;
        // calculate the stddev.
        double std_sum = 0;
        for (int i = 0; i < T; i += 1) {
            std_sum += (openls[i] - mean) * (openls[i] - mean);
        }
        stddev = std_sum / (T - 1);
        confidencelow = mean - 1.96 * Math.sqrt(stddev) / Math.sqrt(T);
        confidencehigh = mean + 1.96 * Math.sqrt(stddev) / Math.sqrt(T);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLow() {
        return confidencelow;
    }

    public double confidenceHigh() {
        return confidencehigh;
    }

}

