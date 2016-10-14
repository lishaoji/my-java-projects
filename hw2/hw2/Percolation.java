package hw2;                       

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF grid;
    private WeightedQuickUnionUF grid2;
    private boolean[][] op;
    private int openCount;
    private int ocean;
    private int inland;
    private int size;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N < 1) {
            throw new java.lang.IllegalArgumentException("Non-positive input");
        } else {
            ocean = 0;
            inland = N * N + 1;
            grid = new WeightedQuickUnionUF(N * N + 2);
            grid2 = new WeightedQuickUnionUF(N * N + 1);
            op = new boolean[N][N];
            openCount = 0;
            size = N;
        }
    }

    // Translate (row, col) into index
    private int getIndex(int row, int col) {
        if (row >= 0 && row < size && col >= 0 && col < size) {
            return (row * size + col + 1);
        } else {
            throw new java.lang.IllegalArgumentException("Invalid position");
        }
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row >= size || row < 0 || col >= size || col < 0) {
            throw new IndexOutOfBoundsException("Invalid position");
        } else if (!isOpen(row, col)) {
            op[row][col] = true;
            openCount += 1;
            int index = getIndex(row, col);
            if (row == 0) {
                grid.union(index, ocean);
                grid2.union(index, ocean);
            }
            if (row == size - 1) {
                grid.union(index, inland);
            }
            if (col > 0 && isOpen(row, col - 1)) {
                grid.union(getIndex(row, col), getIndex(row, col - 1));
                grid2.union(getIndex(row, col), getIndex(row, col - 1));
            }
            if (col < size - 1 && isOpen(row, col + 1)) {
                grid.union(getIndex(row, col), getIndex(row, col + 1));
                grid2.union(getIndex(row, col), getIndex(row, col + 1));
            }
            if (row > 0 && isOpen(row - 1, col)) {
                grid.union(getIndex(row, col), getIndex(row - 1, col));
                grid2.union(getIndex(row, col), getIndex(row - 1, col));
            }
            if (row < size - 1 && isOpen(row + 1, col)) {
                grid.union(getIndex(row, col), getIndex(row + 1, col));
                grid2.union(getIndex(row, col), getIndex(row + 1, col));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return op[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row >= size || row < 0 || col >= size || col < 0) {
            throw new java.lang.IndexOutOfBoundsException("Invalid position");
        } else {
            return ((grid.connected(ocean, getIndex(row, col)) 
                && grid2.connected(ocean, getIndex(row, col))));
        }
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return grid.connected(ocean, inland);
    }

    // unit testing (not required)
    public static void main(String[] args) {
        Percolation p = new Percolation(5);
        System.out.println(p.numberOfOpenSites());
        System.out.println(p.percolates());
        p.open(0, 1);
        System.out.println(p.percolates());
        System.out.println(p.numberOfOpenSites());
    }
}                       
