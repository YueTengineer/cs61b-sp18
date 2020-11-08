package  synthesizer;
import synthesizer.AbstractBoundedQueue;

import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer (int capacity) {
        // TODO: Create new array with capacity elements.
        //       first, last, and fillCount should all be set to 0.
        //       this.capacity should be set appropriately. Note that the local variable
        //       here shadows the field we inherit from AbstractBoundedQueue, so
        //       you'll need to use this.capacity to set the capacity.
        rb = (T [])new Object[capacity];
        first = 0 ;
        last = 0;
        this.fillCount = 0;
        this.capacity = capacity;

    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    @Override
    public void enqueue(T x) throws RuntimeException {
        // TODO: Enqueue the item. Don't forget to increase fillCount and update last.
        if (this.isFull()) {
            throw new RuntimeException("Ring Buffer Overflow") ;
        }
            rb[last] = x;
            fillCount += 1;
            if (last == capacity - 1) {
                last = 0;
            } else {
                last += 1;
            }
    }


    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    @Override
    public T dequeue() throws RuntimeException {
        // TODO: Dequeue the first item. Don't forget to decrease fillCount and update
        if (this.isEmpty()) {
            throw new RuntimeException("Ring Buffer Underflow") ;
        }
        T value = rb[first];
        fillCount -= 1;
        if (first == capacity - 1) {
                first = 0;
        } else {
            first += 1;
        }
        return value;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    @Override
    public T peek() throws RuntimeException {
        // TODO: Return the first item. None of your instance variables should change.
        if (this.isEmpty()) {
            throw new RuntimeException("Ring Buffer Underflow") ;
        }
        return rb[first];
    }

    @Override
    public Iterator<T> iterator() {
        return new BqIterator();
    }

    private class BqIterator implements Iterator<T>{
        private int pos;
        private int cnt;
        public BqIterator () {
            pos = first;
            cnt = 0;
        }
        public boolean hasNext() {
            return cnt == fillCount;
        }
        public T next() {
            T value = rb[pos];
            if (pos == capacity) {
                pos = 0;
            } else {
                pos += 1;
            }
            cnt += 1;
            return value;
        }
    }

}
