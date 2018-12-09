import java.util.Arrays;

public class BruteCollinearPoints {
    private final Point[] points;
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

    private boolean find4thCollinearPoint(Point basePoint, int offset, int sequenceSize, Double slope) {
        for (int i = offset + 1; i < points.length; ++i) {
            double curSlope = basePoint.slopeTo(points[i]);
            if (slope == null || slope == curSlope) {
                if (!find4thCollinearPoint(basePoint, i, sequenceSize + 1, curSlope) && sequenceSize >= 3) {
                    LineSegmentEx segment = new LineSegmentEx(basePoint, points[i]);
                    addSegment(segment);
                }
            }
        }

        return false;
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

    public BruteCollinearPoints(Point[] points) {
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
            if (i != 0 && this.points[i].equals(this.points[i - 1])) {
                throw new IllegalArgumentException("Points array contains equal points.");
            }
        }

        for (int i = 0; i < this.points.length; ++i) {
            find4thCollinearPoint(this.points[i], i, 1, null);
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