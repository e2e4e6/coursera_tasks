import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;


public class PointSET {
    private SET<Point2D> pointSet = new SET<>();

    public PointSET() {

    }

    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    public int size() {
        return pointSet.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new java.lang.IllegalArgumentException("Argument point is null.");
        }

        pointSet.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new java.lang.IllegalArgumentException("Argument point is null.");
        }

        return pointSet.contains(p);
    }

    public void draw() {
        for (Point2D point : pointSet) {
            StdDraw.point(point.x(), point.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new java.lang.IllegalArgumentException("Argument rect is null.");
        }

        List<Point2D> result = new LinkedList<>();
        for (Point2D point : pointSet) {
            if (rect.contains(point)) {
                result.add(point);
            }
        }

        return result;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new java.lang.IllegalArgumentException("Argument point is null.");
        }

        Point2D result = null;
        double minDist = Double.MAX_VALUE;
        for (Point2D point : pointSet) {
            double curDist = point.distanceTo(p);
            if (minDist > curDist) {
                minDist = curDist;
                result = point;
            }
        }

        return result;
    }

    public static void main(String[] args) {

    }
}
