import java.util.ArrayDeque;
import java.util.Queue;
import edu.princeton.cs.algs4.In;

public class Board {

    private int[][] board;
    private final int n;
    private int zeroRow;
    private int zeroColumn;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = tiles[i][j];
                if (board[i][j] == 0) {
                    zeroRow = i;
                    zeroColumn = j;
                }
            }
        }
    }
                                           
    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    private int rightNum(int i, int j) {
        if (i == n - 1 && j == n - 1) return 0;
        return (i * n + (j + 1));
    }

    private int absSub(int a, int b) {
        if (a > b) return a - b;
        return b - a;
    }

    // number of tiles out of place
    public int hamming() {
        int total = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0 && board[i][j] != rightNum(i, j)) total++;
            }
        }
        return total;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int total = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int ans = rightNum(i, j);
                int cell = board[i][j];
                if (cell != 0 && cell != ans) {
                    if (i == n - 1 && j == n - 1) ans = n * n;
                    int trueRow = (cell - 1)/n;
                    int trueCol = cell - 1 - trueRow * n;
                    total += absSub(trueRow, i);
                    total += absSub(trueCol, j);
                }
            }
        }
        return total;
    }

    // is this board the goal board?
    public boolean isGoal() {
        boolean isSame = true;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != rightNum(i, j)) isSame = false;
            }
        }
        return isSame;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.n != that.n) return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.board[i][j] != that.board[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new ArrayDeque<>();
        if (zeroColumn - 1 >= 0) neighbors.add(exch(zeroRow, zeroColumn, zeroRow, zeroColumn - 1, true));
        if (zeroRow - 1 >= 0) neighbors.add(exch(zeroRow, zeroColumn, zeroRow - 1, zeroColumn, true));
        if (zeroColumn + 1 <= n - 1) neighbors.add(exch(zeroRow, zeroColumn, zeroRow, zeroColumn + 1, true));
        if (zeroRow + 1 <= n - 1) neighbors.add(exch(zeroRow, zeroColumn, zeroRow + 1, zeroColumn, true));
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (board[0][0] != 0) {
            if (board[0][1] != 0) return exch(0, 0, 0, 1, false);
            else return exch(0, 0, 1, 0, false);
            }
        else return exch(0, 1, 1, 0, false);
    }

    private Board exch(int i, int j, int k, int l, boolean reset) {
        Board twin = new Board(this.board);
        int swap = twin.board[i][j];
        twin.board[i][j] = board[k][l];
        twin.board[k][l] = swap;
        if (reset) {
            twin.zeroRow = k;
            twin.zeroColumn = l;
        }
        return twin;
    }
    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        System.out.println(initial.toString()); 
        System.out.println(initial.dimension());
        System.out.println(initial.hamming());
        System.out.println(initial.manhattan());
        System.out.print(initial.isGoal());
        // System.out.println(initial.twin().toString());
        System.out.println(initial.zeroRow);
        System.out.println(initial.zeroColumn);
        for (Board next: initial.neighbors()) {
            System.out.println(next.toString());
            for (Board next2: next.neighbors()) {
                System.out.println("child");
                System.out.println(next2.toString());
            }
        }
    }
}