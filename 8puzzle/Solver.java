import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Collections;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Solver {
    private int total;
    private SearchNode last;
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("null not accepted");
        Board twin = initial.twin();
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        MinPQ<SearchNode> pqAlt = new MinPQ<SearchNode>();
        pq.insert(new SearchNode(initial, null, 0, initial.manhattan()));
        pqAlt.insert(new SearchNode(twin, null, 0, twin.manhattan()));

        while (!pq.isEmpty()) {
            SearchNode node = pq.delMin();
            last = node;
            SearchNode nodeAlt = pqAlt.delMin();
            if (node.current.isGoal()) {
                total = node.move;
                break;
            }
            else if (nodeAlt.current.isGoal()) {
                total = -1;
                break;
            }
            for (Board next: node.current.neighbors()) {
                if (node.previous == null || !next.equals(node.previous.current)) pq.insert(new SearchNode(next, node, node.move + 1, next.manhattan()));
            }
            for (Board next: nodeAlt.current.neighbors()) {
                if (nodeAlt.previous == null || !next.equals(nodeAlt.previous.current)) pqAlt.insert(new SearchNode(next, nodeAlt, nodeAlt.move + 1, next.manhattan()));
            }            
        }
    }

    private class SearchNode implements Comparable<SearchNode>  {
        public final Board current;
        public final SearchNode previous;
        public final int move;
        private final int priority;
        // public static final Comparator<SearchNode> BY_HAMMING = new ByHamming();

        private SearchNode(Board board, SearchNode prev, int m, int d) {
            current = board;
            previous = prev;
            move = m;
            priority = m + d;
        }

        @Override
        public int compareTo(SearchNode that) {
            return this.priority - that.priority;
        }

        // private class ByHamming implements Comparator<SearchNode> {
        //     public int compare(SearchNode m, SearchNode n) {
        //         return m.current.hamming() - n.current.hamming();
        //     }
        // }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return total != -1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return total;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (total == -1) return null;
        Queue<Board> solutions = Collections.asLifoQueue(new ArrayDeque<>());
        SearchNode search = last;
        while (search.previous != null) {
            solutions.add(search.current);
            search = search.previous;
        }
        solutions.add(search.current);
        return solutions;
    }

    // test client (see below) 
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
