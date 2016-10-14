package synthesizer;

//import java.util.Iterator;

public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T> {
    protected int fillCount;
    protected int capacity;
    public int capacity() {
        return capacity;
    }
    public int fillCount() {
        return fillCount;
    }
    // public boolean isEmpty(){
        // return BoundedQueue.super.isEmpty();
    // }
    // public boolean isFull(){
        // return BoundedQueue.super.isFull();
    // }
    // public abstract T peek();
    // public abstract T dequeue();
    // public abstract void enqueue(T x);
    // public abstract Iterator<T> iterator();
}
