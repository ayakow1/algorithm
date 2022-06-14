/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private final int size;
    private final WeightedQuickUnionUF qu;
    private final WeightedQuickUnionUF quBack;
    private int openCount;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("index i out of bounds");
        this.qu = new WeightedQuickUnionUF(n * n + 2);
        this.quBack = new WeightedQuickUnionUF(n * n + 2);
        this.grid = new boolean[n + 1][n + 1];
        this.size = n;
        this.openCount = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isValid(row, col)) throw new IllegalArgumentException("index i out of bounds");
        if (!isOpen(row, col)) {
            grid[row][col] = true;
            openCount++;
            int index = xyTo1D(row, col);
            if (row == 1) {
                qu.union(index, 0);
                quBack.union(index, 0);
            }
            if (row == size) {
                qu.union(index, size * size + 1);
            }
            if (isValid(row, col - 1) && isOpen(row, col - 1)) {
                qu.union(index - 1, index);
                quBack.union(index - 1, index);
            }
            if (isValid(row, col + 1) && isOpen(row, col + 1)) {
                qu.union(index + 1, index);
                quBack.union(index + 1, index);
            }
            if (isValid(row - 1, col) && isOpen(row - 1, col)) {
                qu.union(index - size, index);
                quBack.union(index - size, index);
            }
            if (isValid(row + 1, col) && isOpen(row + 1, col)) {
                qu.union(index + size, index);
                quBack.union(index + size, index);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isValid(row, col)) throw new IllegalArgumentException("index i out of bounds"); 
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isValid(row, col)) throw new IllegalArgumentException("index i out of bounds");
        int index = xyTo1D(row, col);
        return quBack.find(0) == quBack.find(index);
        }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    private int xyTo1D(int row, int col) {
        if (!isValid(row, col)) throw new IllegalArgumentException("index i out of bounds");
        return size * (row - 1) + col;
    }

    private boolean isValid(int row, int col) {
        return (row > 0 && row <= size && col > 0 && col <= size);
    }

    // does the system percolate?
    public boolean percolates() {
        return qu.find(0) == qu.find(size * size + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation per = new Percolation(10);
        per.open(1, 1);
        per.open(1, 2);
    
        System.out.println(per.qu.find(1) == per.qu.find(2));
    }

}
