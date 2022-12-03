import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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
        int len = points.length;
        for (int a = 0; a < len - 3; a++) {
            for (int b = a + 1; b < len - 2; b++) {
                double s1 = points[a].slopeTo(points[b]);
                for (int c = b + 1; c < len - 1; c++) {
                    double s2 = points[a].slopeTo(points[c]);
                    for (int d = c + 1; d < len; d++) {
                        double s3 = points[a].slopeTo(points[d]);
                        if (Double.compare(s1, s2) == 0 && Double.compare(s2, s3) == 0) {
                            Point[] ps = {points[a], points[b], points[c], points[d]};
                            Arrays.sort(ps);
                            ans.add(new LineSegment(ps[0], ps[3]));
                        }
                    }
                }
            }
        }
        lineSegments = ans.toArray(new LineSegment[0]);
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