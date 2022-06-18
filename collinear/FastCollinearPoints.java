import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class FastCollinearPoints {
    private final LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 or more points
        if (points == null) throw new IllegalArgumentException("null is not accepted");
        checkValid(points);
        Point[] mutatePoints = points.clone();
        Arrays.sort(mutatePoints);
        ArrayList<LineSegment> segmentsList = new ArrayList<LineSegment>();
        if (points.length > 3) {
            for (int i = 0; i < mutatePoints.length; i++) {
                Point p = mutatePoints[i];
                Point[] copyPoints = mutatePoints.clone();
                Arrays.sort(copyPoints, p.slopeOrder());
                double baseSlope = p.slopeTo(copyPoints[1]);
                int count = 1;
                int start = 1;
                for (int j = 2; j < copyPoints.length; j++) {
                    Point q = copyPoints[j];
                    double currentSlope = p.slopeTo(q);
                    if (baseSlope == currentSlope) {
                        count++;
                    }
                    if (count >= 3 && (baseSlope != currentSlope || (baseSlope == currentSlope && j == copyPoints.length - 1))) {
                        ArrayList<Point> temp = new ArrayList<>();
                        temp.add(p);
                        for (int k = start; k < start + count; k++) {
                            temp.add(copyPoints[k]);
                        }
                        temp.sort(null);
                        if (temp.get(0).compareTo(p) == 0) {
                            LineSegment newLine = new LineSegment(p, temp.get(temp.size()-1));
                            segmentsList.add(newLine);
                        }
                    }
                    if (baseSlope != currentSlope) {
                        count = 1;
                        start = j;
                    }
                    baseSlope = currentSlope;
                }
            }
        }
        segments = segmentsList.toArray(new LineSegment[segmentsList.size()]);
    }

    private void checkValid(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException("null is not accepted");
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null) throw new IllegalArgumentException("null is not accepted");
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException("repeated value not accepted");
            }
        }

    }
        
    public           int numberOfSegments() {       // the number of line segments
        return segments.length;
    }

    public LineSegment[] segments() {               // the line segments
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
