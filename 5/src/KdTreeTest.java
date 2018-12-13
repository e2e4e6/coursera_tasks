import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class KdTreeTest {

    @Test
    public void Should_return_false_when_set_is_empty() {
        KdTree target = new KdTree();

        assertTrue(target.isEmpty());
    }

    @Test
    public void Should_return_true_when_is_not_empty() {
        KdTree target = new KdTree();
        target.insert(new Point2D(0.1, 0.1));

        assertFalse(target.isEmpty());
    }

    @Test
    public void Should_return_set_size() {
        KdTree target = new KdTree();

        assertEquals(0, target.size());

        target.insert(new Point2D(0.1, 0.1));

        assertEquals(1, target.size());

        target.insert(new Point2D(0.2, 0.2));
        target.insert(new Point2D(0.3, 0.3));

        assertEquals(3, target.size());
    }

    @Test
    public void Should_return_true_when_contains() {
        KdTree target = new KdTree();

        target.insert(new Point2D(0.1, 0.1));
        assertTrue(target.contains(new Point2D(0.1, 0.1)));
    }

    @Test
    public void Should_return_false_when_does_not_contain() {
        KdTree target = new KdTree();
        assertFalse(target.contains(new Point2D(0.1, 0.1)));

        target.insert(new Point2D(0.1, 0.1));
        assertFalse(target.contains(new Point2D(0.2, 0.2)));
    }

    @Test
    public void Should_return_nearest_point() {
        KdTree target = new KdTree();
        target.insert(new Point2D(0.1, 0.1));
        target.insert(new Point2D(0.5, 0.1));
        target.insert(new Point2D(0.9, 0.1));

        assertEquals(new Point2D(0.1, 0.1), target.nearest(new Point2D(0.1, 0.1)));
        assertEquals(new Point2D(0.1, 0.1), target.nearest(new Point2D(0.2, 0.1)));
        assertEquals(new Point2D(0.5, 0.1), target.nearest(new Point2D(0.4, 0.1)));
        assertEquals(new Point2D(0.5, 0.1), target.nearest(new Point2D(0.7, 0.1)));
        assertEquals(new Point2D(0.9, 0.1), target.nearest(new Point2D(0.71, 0.1)));
    }

    @Test
    public void Should_return_points_in_rectangle() {
        KdTree target = new KdTree();

        target.insert(new Point2D(0.1, 0.1));
        target.insert(new Point2D(0.5, 0.1));
        target.insert(new Point2D(0.9, 0.1));

        Iterable<Point2D> result = target.range(new RectHV(0.4, 0.0, 0.9, 0.1));
        Iterator<Point2D> it = result.iterator();
        assertEquals(new Point2D(0.5, 0.1), it.next());
        assertEquals(new Point2D(0.9, 0.1), it.next());
        assertFalse(it.hasNext());
    }
}
