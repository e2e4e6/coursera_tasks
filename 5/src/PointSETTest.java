import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PointSETTest {

    @Test
    public void Should_return_false_when_set_is_empty() {
        PointSET target = new PointSET();

        assertTrue(target.isEmpty());
    }

    @Test
    public void Should_return_true_when_is_not_empty() {
        PointSET target = new PointSET();
        target.insert(new Point2D(1.0, 1.0));

        assertFalse(target.isEmpty());
    }

    @Test
    public void Should_return_set_size() {
        PointSET target = new PointSET();

        assertEquals(0, target.size());

        target.insert(new Point2D(1.0, 1.0));

        assertEquals(1, target.size());

        target.insert(new Point2D(2.0, 2.0));
        target.insert(new Point2D(3.0, 3.0));

        assertEquals(3, target.size());
    }

    @Test
    public void Should_return_true_when_contains() {
        PointSET target = new PointSET();

        target.insert(new Point2D(1.0, 1.0));
        assertTrue(target.contains(new Point2D(1.0, 1.0)));
    }

    @Test
    public void Should_return_false_when_does_not_contain() {
        PointSET target = new PointSET();
        assertFalse(target.contains(new Point2D(1.0, 1.0)));

        target.insert(new Point2D(1.0, 1.0));
        assertFalse(target.contains(new Point2D(2.0, 2.0)));
    }

    @Test
    public void Should_return_nearest_point() {
        PointSET target = new PointSET();
        target.insert(new Point2D(1.0, 1.0));
        target.insert(new Point2D(5.0, 1.0));
        target.insert(new Point2D(9.0, 1.0));

        assertEquals(new Point2D(1.0, 1.0), target.nearest(new Point2D(1.0, 1.0)));
        assertEquals(new Point2D(1.0, 1.0), target.nearest(new Point2D(2.0, 1.0)));
        assertEquals(new Point2D(5.0, 1.0), target.nearest(new Point2D(4.0, 1.0)));
        assertEquals(new Point2D(5.0, 1.0), target.nearest(new Point2D(7.0, 1.0)));
        assertEquals(new Point2D(9.0, 1.0), target.nearest(new Point2D(7.1, 1.0)));
    }

    @Test
    public void Should_return_points_in_rectangle() {
        PointSET target = new PointSET();

        target.insert(new Point2D(1.0, 1.0));
        target.insert(new Point2D(5.0, 1.0));
        target.insert(new Point2D(9.0, 1.0));

        Iterable<Point2D> result = target.range(new RectHV(4.0, 0.0, 9.0, 1.0));
        Iterator<Point2D> it = result.iterator();
        assertEquals(new Point2D(5.0, 1.0), it.next());
        assertEquals(new Point2D(9.0, 1.0), it.next());
        assertFalse(it.hasNext());
    }
}
