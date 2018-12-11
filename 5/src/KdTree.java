import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Comparator;

public class KdTree {
    private Node root = null;
    private int size = 0;

    class Node {
        Point2D point;
        Node left;
        Node right;
        boolean color;
        boolean isVertical;

        Node(Point2D point, boolean color, boolean isVertical) {
            this.point = point;
            this.color = color;
            this.isVertical = isVertical;
        }
    }

    private static final boolean RED = true;
    private static final boolean BLACK = false;

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

    private boolean isRed(Node node) {
        if (node == null) {
            return false;
        }

        return node.color == RED;
    }

    private boolean isNecessaryFlipp(Node node) {
        if (node.left == null && node.right == null) {
            return false;
        }

        if (node.left != null && compare(node.point, node.left.point, node.isVertical) >= 0) {
            return true;
        }

        if (node.right != null && compare(node.point, node.right.point, node.isVertical) < 0) {
            return true;
        }

        return false;
    }

    private void flipChild(Node node) {
        Node temp = node.left;
        node.left = node.right;
        node.right = temp;
    }

    private Node rotateLeft(Node node) {
        assert isRed(node.right);

        boolean isVerticalNode = node.isVertical;

        Node temp = node.right;
        node.right = temp.left;
        temp.left = node;
        temp.color = node.color;
        node.color = RED;

        return temp;
    }

    private Node rotateRight(Node node) {
        assert isRed(node.left);

        Node temp = node.left;
        node.left = temp.right;
        temp.right = node;
        temp.color = node.color;
        node.color = RED;

        return temp;
    }

    private void flipColors(Node node) {
        assert !isRed(node);
        assert isRed(node.left);
        assert isRed(node.right);

        node.color = RED;
        node.left.color = BLACK;
        node.right.color = BLACK;
    }

    private Node put(Node node, Point2D point, boolean isVertical) {
        if (node == null) return new Node(point, RED, isVertical);

        int cmp = compare(point, node.point, node.isVertical);
        if (cmp < 0) {
            node.left = put(node.left, point, !node.isVertical);
        }
        else if (cmp > 0) {
            node.right = put(node.right, point, !node.isVertical);
        }
        else {
            node.point = point;
        }

        if (isRed(node.right)   && !isRed(node.left))       node = rotateRight(node);
        if (isRed(node.left)    && isRed(node.left.left))   node = rotateLeft(node);
        if (isRed(node.left)    && isRed(node.right))       flipColors(node);

        return node;
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
        root = put(root, p, true);

        size++;
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new java.lang.IllegalArgumentException("Argument point is null.");
        }

        return false;
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

        return null;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new java.lang.IllegalArgumentException("Argument point is null.");
        }

        return null;
    }

    public static void main(String[] args) {

    }
}
