import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private int size;
    private Node first, last;

    private class Node {
        Item item;
        Node next;
        Node previous;
    }

    // construct an empty deque
    public Deque() {
        size = 0;
        first = null;
        last = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        size++;
        Node old = first;
        first = new Node();
        if (size > 1) old.previous = first;
        first.item = item;
        first.next = old;
        first.previous = null;
        if (size == 1) last = first;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        size++;
        Node old = last;
        last = new Node();
        if (size > 1) old.next = last;
        last.item = item;
        last.next = null;
        last.previous = old;
        if (size == 1) first = last;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        size--;
        Item item = first.item;
        if (isEmpty()) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.previous = null;
        }
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        size--;
        Item item = last.item;
        if (isEmpty()) {
            first = null;
            last = null;
        } else {
            last = last.previous;
            last.next = null;
        }
        return item;
    }

    // return an iterator over items in order from front to back
    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (current == null) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();

        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> d = new Deque<>();
        d.addFirst("Hello");
        d.addLast("World");
        d.addFirst("Cyrus:");
        StdOut.println(d.size());
        for (String s : d) {
            StdOut.println(s);
        }
        d.removeLast();
        d.removeFirst();
        if (d.isEmpty()) StdOut.println("d is empty");
        else StdOut.println("d is not empty");

    }

}