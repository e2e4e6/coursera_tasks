import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class KdTree {
    private Node root = null;
    private int size = 0;

    class Node {
        Point2D point;
        Node left;
        Node right;

        Node(Point2D point) {
            this.point = point;
        }
    }

    private int compare(Point2D o1, Point2D o2, boolean isVertical) {
        if (o1 == null || o2 == null) {
            throw new IllegalArgumentException("Point is null.");
        }

        if (o1 == o2) {
            return 0;
        }

        if (isVertical) {
            return Double.compare(o1.x(), o2.x());
        }

        return Double.compare(o1.y(), o2.y());
    }

    private void draw(Node node, boolean isVertical) {
        if (node == null) {
            return;
        }

        if (isVertical) {
            StdDraw.setPenColor(Color.RED);
            StdDraw.line(node.point.x(), 0.0f, node.point.x(), 1.0f);
        }
        else {
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.line(0.0f, node.point.y(), 1.0f, node.point.y());
        }

        StdDraw.setPenColor(Color.BLACK);
        StdDraw.point(node.point.x(), node.point.y());

        draw(node.left, !isVertical);
        draw(node.right, !isVertical);
    }

    private void range(Node node, boolean isVertical, RectHV rect, RectHV nodeRect, List<Point2D> result) {
        if (node == null) return;

        if (rect.contains(node.point)) {
            result.add(node.point);
        }

        if (isVertical) {
            RectHV lRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), node.point.x(), nodeRect.ymax());
            RectHV rRect = new RectHV(node.point.x(), nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax());

            if (rect.intersects(lRect)) {
                range(node.left, !isVertical, rect, lRect, result);
            }

            if (rect.intersects(rRect)) {
                range(node.right, !isVertical, rect, rRect, result);
            }
        } else {
            RectHV bRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), node.point.y());
            RectHV tRect = new RectHV(nodeRect.xmin(), node.point.y(), nodeRect.xmax(), nodeRect.ymax());

            if (rect.intersects(bRect)) {
                range(node.left, !isVertical, rect, bRect, result);
            }

            if (rect.intersects(tRect)) {
                range(node.right, !isVertical, rect, tRect, result);
            }
        }
    }

    public KdTree() {

    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (root == null) {
            root = new Node(p);
        } else {
            Node current = root;
            boolean isVertical = true;

            while (true) {
                int cmp = compare(p, current.point, isVertical);
                if (cmp < 0) {
                    if (current.left == null) {
                        current.left = new Node(p);
                        break;
                    }

                    current = current.left;
                }
                else {
                    if (current.right == null) {
                        current.right = new Node(p);
                        break;
                    }

                    current = current.right;
                }

                isVertical = !isVertical;
            }
        }

        size++;
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new java.lang.IllegalArgumentException("Argument point is null.");
        }

        Node current = root;
        boolean isVertical = true;

        while (current != null) {
            int cmp = compare(p, current.point, isVertical);
            if (cmp < 0) {
                current = current.left;
            }
            else if (cmp > 0) {
                current = current.right;
            } else {
                return true;
            }

            isVertical = !isVertical;
        }

        return false;
    }

    public void draw() {
        draw(root, true);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new java.lang.IllegalArgumentException("Argument rect is null.");
        }

        List<Point2D> result = new LinkedList<>();

        range(root, true, rect, new RectHV(0.0, 0.0, 1.0, 1.0), result);

        return result;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new java.lang.IllegalArgumentException("Argument point is null.");
        }

        if (root == null) {
            throw new java.lang.IllegalArgumentException("Root is null.");
        }

        Node current = root;
        Point2D result = root.point;
        double minDst = Double.MAX_VALUE;
        boolean isVertical = true;

        while (current != null) {
            double dst = current.point.distanceSquaredTo(p);
            if (dst < minDst) {
                minDst = dst;
                result = current.point;
            }

            int cmp = compare(p, current.point, isVertical);
            if (cmp < 0) {
                current = current.left;
            }
            else {
                current = current.right;
            }

            isVertical = !isVertical;
        }

        return result;
    }

    public static void main(String[] args) {

    }
}
