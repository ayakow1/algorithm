import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import java.util.Queue;
import java.util.LinkedList;

public class PointSET {
    private final TreeSet<Point2D> points;

    public         PointSET()  {                             // construct an empty set of points 
        points = new TreeSet<Point2D>();
    }
    public           boolean isEmpty() {                     // is the set empty? 
        return points.isEmpty();
    }
    public               int size() {                        // number of points in the set 
        return points.size();
    }
    public              void insert(Point2D p) {             // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException("null not accepted");
        points.add(p);
    }
    public           boolean contains(Point2D p) {           // does the set contain point p?
        if (p == null) throw new IllegalArgumentException("null not accepted");
        return points.contains(p);
    } 
    public              void draw() {                        // draw all points to standard draw 
        for (Point2D p: points) {
            p.draw();
        }
    }
    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException("null not accepted"); 
        double xmin = rect.xmin();
        double xmax = rect.xmax();
        double ymin = rect.ymin();
        double ymax = rect.ymax();
        Queue<Point2D> qu = new LinkedList<Point2D>();
        for (Point2D p: points) {
            if (p.x() >= xmin && p.x() <= xmax) {
                if (p.y() >= ymin && p.y() <= ymax) qu.add(p);
            }
        }
        return qu;
    }
    public           Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty 
        if (p == null) throw new IllegalArgumentException("null not accepted");
        double leastDis = 2.0;
        Point2D bestP = null;
        for (Point2D p0: points) {
            double dis = p0.distanceSquaredTo(p);
            if (dis <= leastDis) {
                leastDis = dis;
                bestP = p0;
            }
        }
        return bestP;
    }
    public static void main(String[] args) {                 // unit testing of the methods (optional) 
        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            System.out.println(p.x());
            System.out.println(brute.contains(p));
            brute.insert(p);
        }
        System.out.println(brute.isEmpty());
        System.out.println(brute.size());
        System.out.println(brute.range(new RectHV(0.5, 0.5, 1.0, 1.0)));
        System.out.println(brute.nearest(new Point2D(0.5, 0.5)));
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        brute.draw();
    }
}
