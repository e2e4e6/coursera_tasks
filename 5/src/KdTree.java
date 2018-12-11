import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

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

    private boolean isRed(Node node) {
        if (node == null) {
            return false;
        }

        return node.color == RED;
    }

    private Node rotateLeft(Node node) {
        assert isRed(node.right);

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

        int cmp = 0; // TODO:
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
