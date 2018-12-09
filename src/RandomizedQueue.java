import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Object[] data = null;
    private int size = 0;

    public RandomizedQueue() {
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null.");
        }

        if (data == null) {
            resizeData(2);
        } else if (size == data.length) {
            resizeData(data.length * 2);
        }

        data[size] = item;
        size++;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }

        int index = StdRandom.uniform(size);
        Item result = getElement(index);
        data[index] = data[size - 1];
        data[size - 1] = null;
        size--;

        if (size * 4 == data.length) {
            resizeData(data.length / 2);
        }

        return result;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }

        return getElement(StdRandom.uniform(size));
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        int processedCount;
        int[] indexList;

        public RandomizedQueueIterator() {
            this.processedCount = 0;
            indexList = new int[size];
            for (int i = 0; i < size; ++i) {
                indexList[i] = i;
            }

            StdRandom.shuffle(indexList);
        }

        @Override
        public boolean hasNext() {
            return processedCount != size;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Item result = getElement(indexList[processedCount]);
            processedCount++;
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();

        checkTrue(queue.isEmpty());
        checkEq(queue.size(), 0);

        queue.enqueue(1);
        checkTrue(!queue.isEmpty());
        checkEq(queue.size(), 1);

        checkEq(queue.sample(), 1);
        checkEq(queue.sample(), 1);
        checkEq(queue.sample(), 1);

        queue.enqueue(2);
        checkTrue(!queue.isEmpty());
        checkEq(queue.size(), 2);

        Integer result = queue.dequeue();
        checkTrue(!queue.isEmpty());
        checkTrue(result == 1 || result == 2);

        result = queue.dequeue();
        checkTrue(result == 1 || result == 2);
        checkTrue(queue.isEmpty());
        checkEq(queue.size(), 0);

        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);

        for (Integer i : queue) {
            StdOut.print(i);
            StdOut.println();
        }
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

    private Item getElement(int index) {
        return (Item) data[index];
    }

    private void resizeData(int newSize) {
        Object[] newData = new Object[newSize];
        for (int i = 0; i < size; ++i) {
            newData[i] = data[i];
        }

        data = newData;
    }
}