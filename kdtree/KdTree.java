import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import java.util.Queue;
import java.util.LinkedList;

public class KdTree {
    private Node root;
    private int size;
    private static final boolean HORIZONTAL = false;
    private static final boolean VERTICAL = true;

    public KdTree() {
        size = 0;
    }

    private static class Node {
        private final Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private final boolean direction;
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p0, boolean d0, RectHV rect0) {
            p = p0;
            direction = d0;
            rect = rect0;
        }
    }

    public           boolean isEmpty() {                     // is the set empty? 
        return size == 0;
    }

    public               int size() {                        // number of points in the set 
        return size;
    }

    public              void insert(Point2D p) {             // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException("null not accepted");
        if (!contains(p)) {
            root = insert(root, p, VERTICAL, 0.0, 0.0, 1.0, 1.0);
        }   
    }

    private Node insert(Node node, Point2D p, boolean newDirection, double xmin, double ymin, double xmax, double ymax) {
        if (node == null) {
            size++;
            return new Node(p, newDirection, new RectHV(xmin, ymin, xmax, ymax));
        }
        Point2D pp = node.p;
        if (pp.equals(p)) return null;
        if (node.direction == VERTICAL) {
            if (pp.x() > p.x()) {
                node.lb = insert(node.lb, p, HORIZONTAL, xmin, ymin, pp.x(), ymax);
            }
            else {
                node.rt = insert(node.rt, p, HORIZONTAL, pp.x(), ymin, xmax, ymax);
            }
        }
        else {
            if (pp.y() > p.y()) {
                node.lb = insert(node.lb, p, VERTICAL, xmin, ymin, xmax, pp.y());
            }
            else {
                node.rt = insert(node.rt, p, VERTICAL, xmin, pp.y(), xmax, ymax);
            }
        }
        return node;
    }

    public           boolean contains(Point2D p) {           // does the set contain point p? 
        if (p == null) throw new IllegalArgumentException("null not accepted");
        return contains(root, p);
    }

    private boolean contains(Node node, Point2D p) {
        if (node == null) return false;
        Point2D pp = node.p;
        if (pp.equals(p)) return true;
        if (node.direction == VERTICAL) {
            if (pp.x() > p.x()) return contains(node.lb, p);
            else return contains(node.rt, p);
        }
        else {
            if (pp.y() > p.y()) return contains(node.lb, p);
            else return contains(node.rt, p);
        }
    }

    public              void draw() {                        // draw all points to standard draw 
        draw(root);
    }

    private void draw(Node node) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.p.draw();
        if (node.rect == null) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(node.p.x(), 0.0, node.p.x(), 1.0);
        }
        else {
            if (node.direction == VERTICAL) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius();
                StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius();
                StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());            
            }
        }
        if (node.lb != null) draw(node.lb);
        if (node.rt != null) draw(node.rt);
    }

    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle (or on the boundary) 
        if (rect == null) throw new IllegalArgumentException("null not accepted"); 
        Queue<Point2D> qu = new LinkedList<Point2D>();
        if (root == null) return qu;
        range(rect, qu, root);
        return qu;
    }

    private void range(RectHV rect, Queue<Point2D> qu, Node node) {
        if (node.rect == null || node.rect.intersects(rect)) {
            if (rect.contains(node.p)) qu.add(node.p);
            if (node.lb != null) range(rect, qu, node.lb);
            if (node.rt != null) range(rect, qu, node.rt);
        }
    }

    public           Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new IllegalArgumentException("null not accepted");
        if (root == null) return null;
        return nearest(p, root.p, root);
    }

    private Point2D nearest(Point2D p, Point2D bestP, Node node) {
        Point2D tempP = null;
        double leastDis = p.distanceSquaredTo(bestP);
        if (node.p.distanceSquaredTo(p) <= leastDis) bestP = node.p;
        if ((node.lb != null && node.rt == null) || node.lb != null && (node.direction == VERTICAL && p.x() < node.p.x() ||
            (node.direction == HORIZONTAL && p.y() < node.p.y()))) {
            if (leastDis >= node.lb.rect.distanceSquaredTo(p)) {
                bestP = nearest(p, bestP, node.lb);
                leastDis = p.distanceSquaredTo(bestP);
            }
            if (node.rt != null) {
                if (leastDis >= node.rt.rect.distanceSquaredTo(p)) {
                    bestP = nearest(p, bestP, node.rt);
                }
            }
        }
        else if (node.rt != null) {
            if (leastDis >= node.rt.rect.distanceSquaredTo(p)) {
                bestP = nearest(p, bestP, node.rt);
                leastDis = p.distanceSquaredTo(bestP);
            }
            if (node.lb != null) {
                if (leastDis >= node.lb.rect.distanceSquaredTo(p)) {
                    bestP = nearest(p, bestP, node.lb);
                }                
            }
        }
        return bestP;
    }
 
    public static void main(String[] args) {                 // unit testing of the methods (optional) 
        String filename = args[0];
        In in = new In(filename);
        KdTree kd = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            System.out.println(p.x());
            System.out.println(kd.contains(p));
            kd.insert(p);
            System.out.println(kd.contains(p));
        }
        System.out.println(kd.isEmpty());
        System.out.println(kd.size());
        kd.draw();
        System.out.println(kd.range(new RectHV(0.5, 0.0, 1.0, 1.0)));
        System.out.println(kd.nearest(new Point2D(0.152, 0.362)));
    }
}
