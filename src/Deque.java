import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {
    private int size;
    private ListNode first;
    private ListNode last;

    public Deque() {
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null.");
        }

        ListNode newNode = new ListNode();
        newNode.item = item;
        if (first != null) {
            newNode.next = first;
            first.prev = newNode;
        }

        first = newNode;

        if (last == null) {
            last = newNode;
        }

        size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null.");
        }

        ListNode newNode = new ListNode();
        newNode.item = item;

        if (last != null) {
            newNode.prev = last;
            last.next = newNode;
        }

        last = newNode;

        if (first == null) {
            first = newNode;
        }

        ++size;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }

        ListNode node = first;
        if (size == 1) {
            first = null;
            last = null;
        } else {
            first = first.next;
        }

        node.next = null;
        node.prev = null;
        size--;

        return node.item;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }

        ListNode node = last;
        if (size == 1) {
            first = null;
            last = null;
        } else {
            last = last.prev;
        }

        node.next = null;
        node.prev = null;
        size--;

        return node.item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator(first);
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();

        checkTrue(deque.isEmpty());
        checkEq(deque.size(), 0);

        deque.addFirst(1);
        checkTrue(!deque.isEmpty());
        checkEq(deque.size(), 1);

        deque.addFirst(2);
        checkTrue(!deque.isEmpty());
        checkEq(deque.size(), 2);

        deque.addFirst(3);
        checkTrue(!deque.isEmpty());
        checkEq(deque.size(), 3);

        int checkValue = 3;
        for (Integer i : deque) {
            checkEq(checkValue, i);
            checkValue--;
        }

        checkEq(3, deque.removeFirst());
        checkTrue(!deque.isEmpty());
        checkEq(deque.size(), 2);

        checkEq(2, deque.removeFirst());
        checkTrue(!deque.isEmpty());
        checkEq(deque.size(), 1);

        checkEq(1, deque.removeFirst());
        checkTrue(deque.isEmpty());
        checkEq(deque.size(), 0);

        deque.addLast(1);
        checkTrue(!deque.isEmpty());
        checkEq(deque.size(), 1);

        deque.addLast(2);
        checkTrue(!deque.isEmpty());
        checkEq(deque.size(), 2);

        deque.addLast(3);
        checkTrue(!deque.isEmpty());
        checkEq(deque.size(), 3);

        checkValue = 1;
        for (Integer i : deque) {
            checkEq(checkValue, i);
            checkValue++;
        }

        checkEq(3, deque.removeLast());
        checkTrue(!deque.isEmpty());
        checkEq(deque.size(), 2);

        checkEq(2, deque.removeLast());
        checkTrue(!deque.isEmpty());
        checkEq(deque.size(), 1);

        checkEq(1, deque.removeLast());
        checkTrue(deque.isEmpty());
        checkEq(deque.size(), 0);
    }

    private class ListNode {
        ListNode next;
        ListNode prev;
        Item item;
    }

    private static void checkEq(Integer src, Integer dst) {
        if (!src.equals(dst)) {
            throw new RuntimeException("Not equal.");
        }
    }

    private static void checkTrue(boolean value) {
        if (!value) {
            throw new RuntimeException("Not true");
        }
    }

    private class DequeIterator implements Iterator<Item> {
        private ListNode node;

        DequeIterator(ListNode node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There are no more elements.");
            }

            Item result = node.item;

            node = node.next;
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}