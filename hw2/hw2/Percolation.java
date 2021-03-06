package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF pcl;
    private WeightedQuickUnionUF pcl2;
    // false:blocked true:open
    private boolean [][] state;
    private int open_num = 0;
    private int length;
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N should be greater than zero.");
        }
        /* pcl including the virtual top site and bottom site.
        TOP SITE : N*N  BOTTOM SITE: N*N + 1.
        pcl2 only including the virtual top site N*N*/
        pcl = new WeightedQuickUnionUF(N * N + 2);
        pcl2 = new WeightedQuickUnionUF(N * N + 1);
        state = new boolean[N][N];
        length = N;
    }
    public void open(int row, int col) {
        if (state[row][col] == true) {
            return;
        } else {
            if (length == 1) {
                state[row][col] = true;
                open_num += 1;
                pcl.union(0,1);
                pcl2.union(0,1);
                pcl.union(0,2);
            } else {
                state[row][col] = true;
                open_num += 1;
                connectSurroundings(row,col);
            }

        }
    }
    private void connectSurroundings(int row, int col) {
        if (row == 0) {
            // connect top layer site to the virtual top site.
            pcl.union(length * length,dChange(row,col));
            pcl2.union(length * length,dChange(row,col));
            if (col == 0) {
                if (isOpen(row + 1, col)) {
                    pcl.union(dChange(row, col),dChange(row + 1, col));
                    pcl2.union(dChange(row, col),dChange(row + 1, col));
                }
                if (isOpen(row , col + 1)) {
                    pcl.union(dChange(row, col),dChange(row , col + 1));
                    pcl2.union(dChange(row, col),dChange(row , col + 1));
                }
            } else if (col == length - 1) {
                if (isOpen(row + 1, col)) {
                    pcl.union(dChange(row, col),dChange(row + 1, col));
                    pcl2.union(dChange(row, col),dChange(row + 1, col));
                }
                if (isOpen(row , col - 1)) {
                    pcl.union(dChange(row, col),dChange(row , col - 1));
                    pcl2.union(dChange(row, col),dChange(row , col - 1));
                }
            } else {
                if (isOpen(row + 1, col)) {
                    pcl.union(dChange(row, col),dChange(row + 1, col));
                    pcl2.union(dChange(row, col),dChange(row + 1, col));
                }
                if (isOpen(row , col - 1)) {
                    pcl.union(dChange(row, col),dChange(row , col - 1));
                    pcl2.union(dChange(row, col),dChange(row , col - 1));
                }
                if (isOpen(row , col + 1)) {
                    pcl.union(dChange(row, col),dChange(row , col + 1));
                    pcl2.union(dChange(row, col),dChange(row , col + 1));
                }
            }
        } else if (row == length - 1) {
            // connect bottom layer to the virtual bottom site.
            pcl.union(length * length + 1,dChange(row,col));
            if (col == 0) {
                if (isOpen(row - 1, col)) {
                    pcl.union(dChange(row, col),dChange(row - 1, col));
                    pcl2.union(dChange(row, col),dChange(row - 1, col));
                }
                if (isOpen(row , col + 1)) {
                    pcl.union(dChange(row, col),dChange(row , col + 1));
                    pcl2.union(dChange(row, col),dChange(row , col + 1));
                }
            } else if (col == length - 1) {
                if (isOpen(row - 1, col)) {
                    pcl.union(dChange(row, col),dChange(row - 1, col));
                    pcl2.union(dChange(row, col),dChange(row - 1, col));
                }
                if (isOpen(row , col - 1)) {
                    pcl.union(dChange(row, col),dChange(row , col - 1));
                    pcl2.union(dChange(row, col),dChange(row , col - 1));
                }
            } else {
                if (isOpen(row - 1, col)) {
                    pcl.union(dChange(row, col),dChange(row - 1, col));
                    pcl2.union(dChange(row, col),dChange(row - 1, col));
                }
                if (isOpen(row , col - 1)) {
                    pcl.union(dChange(row, col),dChange(row , col - 1));
                    pcl2.union(dChange(row, col),dChange(row , col - 1));
                }
                if (isOpen(row , col + 1)) {
                    pcl.union(dChange(row, col),dChange(row , col + 1));
                    pcl2.union(dChange(row, col),dChange(row , col + 1));
                }
            }
        } else {
            if (col == 0) {
                if (isOpen(row + 1, col)) {
                    pcl.union(dChange(row, col),dChange(row + 1, col));
                    pcl2.union(dChange(row, col),dChange(row + 1, col));
                }
                if (isOpen(row - 1, col)) {
                    pcl.union(dChange(row, col),dChange(row - 1, col));
                    pcl2.union(dChange(row, col),dChange(row - 1, col));
                }
                if (isOpen(row , col + 1)) {
                    pcl.union(dChange(row, col),dChange(row , col + 1));
                    pcl2.union(dChange(row, col),dChange(row , col + 1));
                }
            } else if (col == length -1) {
                if (isOpen(row + 1, col)) {
                    pcl.union(dChange(row, col),dChange(row + 1, col));
                    pcl2.union(dChange(row, col),dChange(row + 1, col));
                }
                if (isOpen(row - 1, col)) {
                    pcl.union(dChange(row, col),dChange(row - 1, col));
                    pcl2.union(dChange(row, col),dChange(row - 1, col));
                }
                if (isOpen(row , col - 1)) {
                    pcl.union(dChange(row, col),dChange(row , col - 1));
                    pcl2.union(dChange(row, col),dChange(row , col - 1));
                }
            } else {
                if (isOpen(row + 1, col)) {
                    pcl.union(dChange(row, col),dChange(row + 1, col));
                    pcl2.union(dChange(row, col),dChange(row + 1, col));
                }
                if (isOpen(row - 1, col)) {
                    pcl.union(dChange(row, col),dChange(row - 1, col));
                    pcl2.union(dChange(row, col),dChange(row - 1, col));
                }
                if (isOpen(row , col - 1)) {
                    pcl.union(dChange(row, col),dChange(row , col - 1));
                    pcl2.union(dChange(row, col),dChange(row , col - 1));
                }
                if (isOpen(row , col + 1)) {
                    pcl.union(dChange(row, col),dChange(row , col + 1));
                    pcl2.union(dChange(row, col),dChange(row , col + 1));
                }
            }

        }
    }
    public boolean isOpen(int row, int col) {
        return state[row][col] == true;
    }  // is the site (row, col) open?
    public boolean isFull(int row, int col) {
        return pcl2.connected(dChange(row,col),length * length);
    }  // is the site (row, col) full?
    public int numberOfOpenSites() {
        return open_num;
    }          // number of open sites
    public boolean percolates() {
        return pcl.connected(length * length,length * length + 1);
    }              // does the system percolate?
    private int dChange(int row, int col) { // change 2d to 1d dimension
        return row * length + col;
    }
    public static void main(String[] args) {

    }  
}
