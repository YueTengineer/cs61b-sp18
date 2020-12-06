package hw2;

import org.junit.Test;

public class PercolationFactory {
    public Percolation make(int N) {
        return new Percolation(N);
    }
}
