import java.util.Arrays;
import java.util.LinkedList;

public class FastCollinearPoints {
    private Point[] points;
    private SegmentNode segmentRoot = null;
    private int segmentCount = 0;
    private LineSegment[] segments;

    private class LineSegmentEx {
        LineSegmentEx(Point p, Point q) {
            this.p = p;
            this.q = q;
        }

        Point p;
        Point q;
    }

    private class SegmentNode {
        LineSegmentEx segment;
        SegmentNode next;

        SegmentNode(LineSegmentEx segment) {
            this.segment = segment;
        }
    }

    private void addSegment(LineSegmentEx segment) {
        SegmentNode node = segmentRoot;

        while (node != null) {
            if (segment.q.compareTo(node.segment.q) == 0 || segment.p.compareTo(node.segment.p) == 0) {
                double slope1 = segment.q.slopeTo(segment.p);
                double slope2 = node.segment.q.slopeTo(node.segment.p);

                if (slope1 == slope2) {
                    if (segment.p.compareTo(node.segment.p) < 0) {
                        node.segment.p = segment.p;
                    }

                    if (segment.q.compareTo(node.segment.q) > 0) {
                        node.segment.q = segment.q;
                    }

                    return;
                }
            }

            node = node.next;
        }

        segmentCount++;
        SegmentNode segmentNode = new SegmentNode(segment);
        segmentNode.next = segmentRoot;
        segmentRoot = segmentNode;
    }

    private void findCollinearSegment(Point basePoint, Point[] points) {
        Arrays.sort(points, basePoint.slopeOrder());
        Double slope = null;
        int sameSlopeCount = 0;
        Point q = null;
        for (Point point : points) {
            if (point.compareTo(basePoint) <= 0) {
                continue;
            }

            double curSlope = basePoint.slopeTo(point);
            if (slope == null || curSlope == slope) {
                sameSlopeCount++;
                q = point;
            } else {
                if (sameSlopeCount >= 3) {
                    addSegment(new LineSegmentEx(basePoint, q));
                }

                q = null;
                sameSlopeCount = 1;
            }

            slope = curSlope;
        }

        if (sameSlopeCount >= 3) {
            addSegment(new LineSegmentEx(basePoint, q));
        }
    }

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points array is null.");
        }

        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException("Points array contains null.");
            }
        }

        this.points = Arrays.copyOf(points, points.length);
        Arrays.sort(this.points, Point::compareTo);

        for (int i = 0; i < this.points.length; ++i) {
            findCollinearSegment(this.points[i], Arrays.copyOf(this.points, this.points.length));
        }

        segments =  new LineSegment[segmentCount];
        if (segmentCount != 0) {
            SegmentNode node = segmentRoot;
            int pos = segmentCount - 1;
            while (node != null) {
                segments[pos] = new LineSegment(node.segment.p, node.segment.q);
                node = node.next;
                pos--;
            }
        }
    }

    public int numberOfSegments() {
        return segmentCount;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    }
}