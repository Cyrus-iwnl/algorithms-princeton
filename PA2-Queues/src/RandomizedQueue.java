import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] arr;
    private int size = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        arr = (Item[]) new Object[4];
    }

    // grow or shrink the array
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        if (size >= 0) System.arraycopy(arr, 0, copy, 0, size);
        arr = copy;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (size == arr.length) resize(2 * arr.length);
        arr[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        // ************CRUX************
        // How to avoid null element in the array?
        Item item = swap(StdRandom.uniform(size));
        size--;
        if (size > 0 && size == arr.length / 4) resize(arr.length / 2);
        return item;
    }

    private Item swap(int n) {
        Item ans = arr[n];
        arr[n] = arr[size - 1];
        arr[size - 1] = null;
        return ans;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        return arr[StdRandom.uniform(size)];
    }

    // return an independent iterator over items in random order
    @Override
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item> {
        private final int[] nums;
        private int cnt;

        public RandomIterator() {
            cnt = 0;
            nums = new int[RandomizedQueue.this.size];
            for (int i = 0; i < size; i++) nums[i] = i;
            StdRandom.shuffle(nums);
        }

        @Override
        public boolean hasNext() {
            return cnt < size;
        }

        @Override
        public Item next() {
            if (cnt >= size) throw new NoSuchElementException();
            return arr[nums[cnt++]];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        rq.enqueue("Hello");
        rq.enqueue("World");
        rq.enqueue("Dear");
        rq.enqueue("My lord");
        System.out.println(rq.size());
        System.out.println(rq.sample());
        rq.dequeue();
        rq.dequeue();
        for (String s : rq) System.out.println(s);
        if (rq.isEmpty()) System.out.println("empty");
        else System.out.println("not empty");
    }
}