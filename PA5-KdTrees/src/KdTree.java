import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private Node root;
    private Point2D best;

    // construct an empty set of points
    public KdTree() {
        root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        return (node == null) ? 0 : node.count;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = insert(root, p, true, 0, 0, 1, 1);
    }

    private Node insert(Node node, Point2D p, boolean isVertical, double x0, double y0, double x1, double y1) {
        if (node == null) return new Node(p, isVertical, new RectHV(x0, y0, x1, y1));
        int cmp = node.compares(p);
        if (cmp < 0) {
            double xMax = isVertical ? node.x() : x1;
            double yMax = isVertical ? y1 : node.y();
            node.left = insert(node.left, p, !isVertical, x0, y0, xMax, yMax);
        } else if (cmp > 0) {
            double xMin = isVertical ? node.x() : x0;
            double yMin = isVertical ? y0 : node.y();
            node.right = insert(node.right, p, !isVertical, xMin, yMin, x1, y1);
        } // else { DO NOTHING }
        node.count = 1 + size(node.left) + size(node.right);
        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return contains(root, p);
    }

    private boolean contains(Node node, Point2D p) {
        if (node == null) return false;
        if (p.equals(node.p)) return true;
        double v1 = node.isVertical ? p.x() : p.y();
        double v2 = node.isVertical ? node.x() : node.y();
        if (v1 < v2) return contains(node.left, p);
        else return contains(node.right, p);
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node node) {
        if (node == null) return;
        RectHV rect = node.rect;
        // draw boundary
        if (node.isVertical) { // vertical line
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.x(), rect.ymin(), node.x(), rect.ymax());
        } else { // horizontal line
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rect.xmin(), node.y(), rect.xmax(), node.y());
        }
        draw(node.left); // or below
        draw(node.right); // or above
        // draw point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(node.x(), node.y());
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> ans = new ArrayList<>();
        range(ans, root, rect);
        return ans;
    }

    private void range(List<Point2D> ans, Node node, RectHV rect) {
        if (node == null) return;
        if (rect.contains(node.p)) ans.add(node.p);
        // **************** Pruning Rule *********************
        if (node.left != null && rect.intersects(node.left.rect))
            range(ans, node.left, rect);
        if (node.right != null && rect.intersects(node.right.rect))
            range(ans, node.right, rect);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        best = null;
        nearest(p, root);
        return best;
    }

    private void nearest(Point2D p, Node node) {
        if (node == null) return;
        if (best == null || node.p.distanceSquaredTo(p) < best.distanceSquaredTo(p)) {
            best = node.p;
        }
        // determine which side to travel first
        double v1 = node.isVertical ? p.x() : p.y();
        double v2 = node.isVertical ? node.x() : node.y();
        Node next = v1 < v2 ? node.left : node.right;
        Node other = v1 < v2 ? node.right : node.left;
        nearest(p, next); // one side
        // **************** prune *********************
        if (other == null || other.rect.distanceSquaredTo(p) > best.distanceSquaredTo(p)) {
            return;
        }
        nearest(p, other); // the other side
    }

    private static class Node {
        Point2D p;
        Node left, right; // or below/above
        boolean isVertical;
        int count;
        RectHV rect;

        Node(Point2D p, boolean isVertical, RectHV rect) {
            this.p = p;
            this.isVertical = isVertical;
            this.rect = rect;
            count = 1;
            left = null;
            right = null;
        }

        public double x() {
            return p.x();
        }

        public double y() {
            return p.y();
        }

        // return -1 if that should be placed left/down
        // return  1 if that should be placed right/up
        // return  0 if two points are the same
        public int compares(Point2D that) {
            if (that == null) throw new IllegalArgumentException();
            if (p.equals(that)) return 0;
            if (isVertical) {
                return that.x() < p.x() ? -1 : 1;
            } else {
                return that.y() < p.y() ? -1 : 1;
            }
        }
    }
}