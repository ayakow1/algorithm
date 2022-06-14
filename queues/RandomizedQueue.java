import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import java.util.NoSuchElementException;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private Item[] s;
    private Item[] s_copy;
    private int N = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        s = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return N;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) copy[i] = s[i];
        s = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("null is not accepted");
        if (N == s.length) resize(2 * s.length);
        s[N++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        int index = StdRandom.uniform(N);
        Item item = s[index];
        if (index != N-1) {
            s[index] = s[N-1];
        }
        s[N-1] = null;
        N--;
        if (N > 0 && N == s.length/4) resize(s.length/2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        int index = StdRandom.uniform(N);
        Item item = s[index];
        return item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator(); }

    private class RandomIterator implements Iterator<Item> {
        private int i = N;
        private Item[] iter = (Item[]) new Object[N]; 
        public RandomIterator() {
            for (int i = 0; i < N; i++) iter[i] = s[i];
            StdRandom.shuffle(iter);
        }
        public boolean hasNext() { return i > 0; }
        public void remove() { throw new UnsupportedOperationException("remove() is not supported"); }
        public Item next() {
            if (i == 0) throw new NoSuchElementException("Deque is empty");
            return iter[--i];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (int a : queue) {
            for (int b : queue)
                StdOut.println(a + "-" + b + " ");
            StdOut.println();
        }
    }

}