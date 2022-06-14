import java.util.NoSuchElementException;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int length;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }
    // construct an empty deque
    public Deque() {
        this.first = null;
        this.last = null;
        this.length = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return length == 0;
    }

    // return the number of items on the deque
    public int size() {
        return length;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("null is not accepted");
        Node oldfirst = first;
        first = new Node();
        length++;
        first.item = item;
        first.next = oldfirst;
        first.prev = null;
        if (oldfirst == null) last = first;
        else oldfirst.prev = first;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("null is not accepted");
        Node oldlast = last;
        last = new Node();
        length++;
        last.item = item;
        last.next = null;
        last.prev = oldlast;    
        if (oldlast == null) first = last;
        else oldlast.next = last;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        Item item = first.item;
        first = first.next;
        length--;
        if (isEmpty()) last = null;
        else  first.prev = null;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        Item item = last.item;
        last = last.prev;
        length--;
        if (last == null) first = last;
        else last.next = null; 
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() { return new ListIterator(); }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() { return current != null; }
        public void remove() { throw new UnsupportedOperationException("remove() is not supported"); }
        public Item next() {
            if (current == null) throw new NoSuchElementException("Deque is empty");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> strs = new Deque<String>();
        System.out.println(strs.isEmpty());
        strs.addFirst("lion");
        // strs.addFirst("dog");
        // strs.addLast("cat");
        // strs.addLast("panda");
        System.out.println(strs.isEmpty());
        System.out.println(strs.removeFirst());
        System.out.println(strs.length);
        for (String s: strs) System.out.println(s);
    }
}
