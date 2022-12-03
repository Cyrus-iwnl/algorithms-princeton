import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FastCollinearPoints {
    private LineSegment[] lineSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        validate(points);
        find(points);
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    private void find(Point[] points) {
        List<LineSegment> ans = new ArrayList<>();
        int len = points.length - 1;
        for (int i = 0; i < points.length; i++) {
            Point origin = points[i];
            Point[] other = getOther(points, i); // points exclude the origin
            Arrays.sort(other, origin.slopeOrder());
            int cnt = 1; // the number of collinear points
            for (int j = 1; j < len; j++) {
                double s1 = origin.slopeTo(other[j - 1]);
                double s2 = origin.slopeTo(other[j]);
                if (Double.compare(s1, s2) == 0) cnt++;
                else {
                    if (cnt >= 3) {
                        List<Point> collinearPoints = new ArrayList<>();
                        for (int k = j - cnt; k < j; k++) collinearPoints.add(other[k]);
                        // ************CRUX*************
                        // How to avoid duplicated line segments?
                        // origin(Pivot) must be the min end of the line
                        Point minEnd = Collections.min(collinearPoints);
                        if (origin.compareTo(minEnd) < 0) {
                            Point maxEnd = Collections.max(collinearPoints);
                            ans.add(new LineSegment(origin, maxEnd));
                        }
                    }
                    cnt = 1;
                }
            }
            // when the traversal ends, it's possible that cnt >= 3
            if (cnt >= 3) {
                List<Point> collinearPoints = new ArrayList<>();
                for (int k = len - cnt; k < len; k++) collinearPoints.add(other[k]);
                Point minEnd = Collections.min(collinearPoints);
                if (origin.compareTo(minEnd) < 0) {
                    Point maxEnd = Collections.max(collinearPoints);
                    ans.add(new LineSegment(origin, maxEnd));
                }
            }
        }
        lineSegments = ans.toArray(new LineSegment[0]);
    }

    private Point[] getOther(Point[] points, int index) {
        Point[] ans = new Point[points.length - 1];
        int k = 0;
        for (int i = 0; i < index; i++) {
            ans[k++] = points[i];
        }
        for (int i = index + 1; i < points.length; i++) {
            ans[k++] = points[i];
        }
        return ans;
    }

    private void validate(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException();
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }
}