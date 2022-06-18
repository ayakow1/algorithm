import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;


public class BruteCollinearPoints {
    private final LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points
        if (points == null) throw new IllegalArgumentException("null is not accepted");
        ArrayList<LineSegment> segmentsList = new ArrayList<LineSegment>();
        for (int p = 0; p < points.length; p++) {
            Point P = points[p];
            if (P == null) throw new IllegalArgumentException("null is not accepted");
            for (int q = p + 1; q < points.length; q++) {
                Point Q = points[q];
                if (Q == null) throw new IllegalArgumentException("null is not accepted");
                if (P.compareTo(Q) == 0) throw new IllegalArgumentException("repeated value is not accepted");
                for (int r = q + 1; r < points.length; r++) {
                    Point R = points[r];
                    if (R == null) throw new IllegalArgumentException("null is not accepted");
                    if (P.compareTo(R) == 0 || Q.compareTo(R) == 0) throw new IllegalArgumentException("repeated value is not accepted");
                    if (P.slopeTo(Q) != Q.slopeTo(R)) continue;
                    for (int s = r + 1; s < points.length; s++) {
                        Point S = points[s];
                        if (S == null) throw new IllegalArgumentException("null is not accepted");
                        if (P.compareTo(S) == 0 || Q.compareTo(S) == 0 || R.compareTo(S) == 0) throw new IllegalArgumentException("repeated value is not accepted");
                        if (P.slopeTo(Q) == P.slopeTo(R) && P.slopeTo(Q) == P.slopeTo(S)) {
                            Point[] toSort = new Point[]{P, Q, R, S};
                            Arrays.sort(toSort);
                            LineSegment newLine = new LineSegment(toSort[0], toSort[3]);
                            segmentsList.add(newLine);
                        }
                    }
                }
            }
        }
        segments = segmentsList.toArray(new LineSegment[segmentsList.size()]);
    }
    public int numberOfSegments() {       // the number of line segments
        return segments.length;
    }
    public LineSegment[] segments() {             // the line segments
        return segments.clone();

    }
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
    
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
    
        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
