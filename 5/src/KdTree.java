import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;

public class KdTree {
    private Node root = null;
    private double minDst = Double.MAX_VALUE;
    private Point2D nearestPoint = null;
    private int size = 0;

    private class Node {
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
            StdDraw.setPenColor(255, 0, 0);
            StdDraw.line(
                    node.point.x(), node.rect.ymin(),
                    node.point.x(), node.rect.ymax());
        }
        else {
            StdDraw.setPenColor(0, 0, 255);
            StdDraw.line(
                    node.rect.xmin(), node.point.y(),
                    node.rect.xmax(), node.point.y());
        }

        StdDraw.setPenColor(0, 0, 0);
        StdDraw.point(node.point.x(), node.point.y());

        draw(node.left, !isVertical);
        draw(node.right, !isVertical);
    }

    private void range(Node node, RectHV rect, List<Point2D> result) {
        if (node == null) return;
        if (!rect.intersects(node.rect)) return;

        if (rect.contains(node.point)) {
            result.add(node.point);
        }

        range(node.left, rect, result);
        range(node.right, rect, result);
    }

    private RectHV makeLeftSubRect(Node node, boolean isVertical) {
        RectHV rect = node.rect;
        Point2D pivot = node.point;

        if (isVertical) {
            return new RectHV(rect.xmin(), rect.ymin(), pivot.x(), rect.ymax());
        }

        return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), pivot.y());
    }

    private RectHV makeRightSubRect(Node node, boolean isVertical) {
        RectHV rect = node.rect;
        Point2D pivot = node.point;

        if (isVertical) {
            return new RectHV(pivot.x(), rect.ymin(), rect.xmax(), rect.ymax());
        }

        return new RectHV(rect.xmin(), pivot.y(), rect.xmax(), rect.ymax());
    }

    private void nearest(Node node, Point2D point, boolean isVertical) {
        double dst = node.point.distanceSquaredTo(point);
        if (dst < minDst) {
            minDst = dst;
            nearestPoint = node.point;
        }

        if (node.left != null && node.right != null) {
            if (compare(point, node.point, isVertical) < 0) {
                if (node.left.rect.distanceSquaredTo(point) < minDst) {
                    nearest(node.left, point, !isVertical);
                }
                if (node.right.rect.distanceSquaredTo(point) < minDst) {
                    nearest(node.right, point, !isVertical);
                }
            } else {
                if (node.right.rect.distanceSquaredTo(point) < minDst) {
                    nearest(node.right, point, !isVertical);
                }
                if (node.left.rect.distanceSquaredTo(point) < minDst) {
                    nearest(node.left, point, !isVertical);
                }
            }
        } else {
            if (node.left != null && node.left.rect.distanceSquaredTo(point) < minDst) {
                nearest(node.left, point, !isVertical);
            }
            if (node.right != null && node.right.rect.distanceSquaredTo(point) < minDst) {
                nearest(node.right, point, !isVertical);
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
            root = new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0));
            size++;
        } else {
            Node current = root;
            boolean isVertical = true;

            while (true) {
                int cmp = compare(p, current.point, isVertical);
                if (cmp < 0) {
                    if (current.left == null) {
                        current.left = new Node(p, makeLeftSubRect(current, isVertical));
                        size++;
                        break;
                    }

                    current = current.left;
                }
                else {
                    if (current.point.equals(p)) {
                        return;
                    }

                    if (current.right == null) {
                        current.right = new Node(p, makeRightSubRect(current, isVertical));
                        size++;
                        break;
                    }

                    current = current.right;
                }

                isVertical = !isVertical;
            }
        }
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
                if (current.point.equals(p)) {
                    return true;
                }

                current = current.right;
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

        range(root, rect, result);

        return result;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new java.lang.IllegalArgumentException("Argument point is null.");
        }

        if (root == null) {
            return null;
        }

        nearestPoint = root.point;
        minDst = p.distanceSquaredTo(root.point);
        nearest(root, p, true);

        return nearestPoint;
    }

    public static void main(String[] args) {

    }
}
