// Make sure to make this class a part of the synthesizer package
package synthesizer;

import java.util.Iterator;


//Make sure to make this class and all of its methods public
//Make sure to make this class extend AbstractBoundedQueue<t>
public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> implements 
    BoundedQueue<T>, Iterable<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        // Create new array with capacity elements.
        // first, last, and fillCount should all be set to 0.
        // this.capacity should be set appropriately. Note that the local variable
        // here shadows the field we inherit from AbstractBoundedQueue, so
        // you'll need to use this.capacity to set the capacity.
        this.first = 0;
        this.last = 0;
        this.fillCount = 0;
        this.capacity = capacity;
        this.rb = (T[]) new Object[capacity];
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    @Override
    public void enqueue(T x) {
        //Enqueue the item. Don't forget to increase fillCount and update last.
        if (fillCount == capacity) {
            throw new RuntimeException("Ring buffer overflow");
        }
        rb[last] = x;
        fillCount += 1;
        last = (last + 1) % capacity;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    @Override
    public T dequeue() {
        //Dequeue the first item. Don't forget to decrease fillCount and update
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        T toReturn = rb[first];
        rb[first] = null;
        fillCount -= 1;
        first = (first + 1) % capacity;
        return toReturn;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    @Override
    public T peek() {
        //Return the first item. None of your instance variables should change.
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        T toReturn = rb[first];
        return toReturn;
    }

    //When you get to part 5, implement the needed code to support iteration.
    @Override
    public boolean hasNext() {
        return iterator().hasNext();
    }

    @Override
    public T next() {
        return iterator().next();
    }

    private class HelperClass implements Iterator<T> {
        private int pointer;

        public HelperClass() {
            pointer = first;
        }
        @Override
        public boolean hasNext() {
            return (pointer < last);
        }
        @Override
        public T next() {
            T current = rb[pointer];
            pointer = (pointer + 1) % capacity;
            return current;
        }
    }
    @Override
    public Iterator<T> iterator() {
        return new HelperClass();
    }

//    public static void main(String[] args) {
//        ArrayRingBuffer<Double> x = new ArrayRingBuffer(10);
//        for (int i = 0; i < 10; i += 1) {
//            System.out.println(x.rb[i]);
//        }
//    }
}
