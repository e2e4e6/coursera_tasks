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
        RectHV rect;
        Node left;
        Node right;

        Node(Point2D point, RectHV rect) {
            this.point = point;
            this.rect = rect;
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
            StdDraw.line(
                    node.point.x(), node.rect.ymin(),
                    node.point.x(), node.rect.ymax());
        }
        else {
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.line(
                    node.rect.xmin(), node.point.y(),
                    node.rect.xmax(), node.point.y());
        }

        StdDraw.setPenColor(Color.BLACK);
        StdDraw.point(node.point.x(), node.point.y());

        draw(node.left, !isVertical);
        draw(node.right, !isVertical);
    }

    private void range(Node node, boolean isVertical, RectHV rect, List<Point2D> result) {
        if (node == null) return;
        if (!rect.intersects(node.rect)) return;

        if (rect.contains(node.point)) {
            result.add(node.point);
        }

        if (isVertical) {
            range(node.left, !isVertical, rect, result);
            range(node.right, !isVertical, rect, result);
        } else {
            range(node.left, !isVertical, rect, result);
            range(node.right, !isVertical, rect, result);
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
            root = new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0));
        } else {
            Node current = root;
            boolean isVertical = true;

            while (true) {
                int cmp = compare(p, current.point, isVertical);
                if (cmp < 0) {
                    if (current.left == null) {
                        RectHV rect;
                        if (isVertical) {
                            rect = new RectHV(current.rect.xmin(), current.rect.ymin(), current.point.x(), current.rect.ymax());
                        }
                        else {
                            rect = new RectHV(current.rect.xmin(), current.rect.ymin(), current.rect.xmax(), current.point.y());
                        }
                        current.left = new Node(p, rect);
                        break;
                    }

                    current = current.left;
                }
                else {
                    if (current.right == null) {
                        RectHV rect;
                        if (isVertical) {
                            rect = new RectHV(current.point.x(), current.rect.ymin(), current.rect.xmax(), current.rect.ymax());
                        }
                        else {
                            rect = new RectHV(current.rect.xmin(), current.point.y(), current.rect.xmax(), current.rect.ymax());
                        }

                        current.right = new Node(p, rect);
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

        range(root, true, rect, result);

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
