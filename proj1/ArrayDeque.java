public class ArrayDeque<Item> {
    private int size;
    private Item[] items;
    // front is the index of the next front item to be inserted
    private int front;
    // rear is the index of the next last item to be inserted
    private int rear;
    private double usageRatio;

    private int RFACTOR = 2;

    public ArrayDeque() {
        size = 0;
        items = ((Item[]) new Object[8]);
        front = items.length / 2;
        rear = items.length / 2;
        usageRatio = 0;
    }

    private void scale(){
        usageRatio = ((double)size/items.length);
        if (items.length >= 16 && usageRatio <= 0.25){
        Item[] a = (Item[]) new Object[items.length/2];
            System.arraycopy(items, front+1, a, a.length/4, size);
            front = a.length/4 -1;
            rear = a.length/4 + size;
            items = a;
        }
    }


    public void addFirst(Item x) {
        if ( front <= 0 || size == items.length) {
            resize(items.length * RFACTOR);
        }
        if (size == 0) {
            items[front] = x;
        }
        size += 1;
        if (rear == front){
            rear += 1;
        }

        items[front] = x;
//        System.out.println(front+"#"+items[front]);
        front -= 1;
        scale();
//        System.out.println(front+"&");
//        size += 1;
    }

    public void addLast(Item x) {
//        System.out.println(rear+"*"+items.length);
        if (size == items.length || rear >= items.length) {
            resize(items.length * RFACTOR);
        }
        size += 1;
//        System.out.println(rear+"*");

        if (rear == front){
//            items[rear] = x;
            front -= 1;
//            rear += 1;

//            return;
        }
        items[rear] = x;
//        System.out.println(rear+"&"+items[rear]);
        rear += 1;
        scale();
//        front -= 1;
//        size += 1;
    }

    private void resize(int capacity) {
        Item[] a = (Item[]) new Object[capacity];
        System.arraycopy(items, front+1, a, capacity/3, size);

        front = capacity/3 -1;
//        System.out.println(front+"*");
//        if (size/2 + size>= a.length){
//            rear = a.length - 2;
//        }
        rear = capacity/3 + size;

//        System.out.println(rear + "#"+capacity);
        items = a;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public Item removeFirst() {
        if (size == 0) {
//            System.out.println("hi");
            return null;
        }
        Item temp = items[front + 1];
//        if (temp == items[front+1]){
//            rear -= 1;
//        }

        items[front + 1] = null;
        front += 1;
        size -= 1;
        scale();
        return temp;
    }

    public Item removeLast() {
        if (size == 0) {
            return null;
        }
        Item last = items[rear-1];
//        if (last == items[front+1]){
//            front += 1;
//        }
//        System.out.println(rear+"*");
        items[rear-1] = null;
        rear -= 1;
        size -= 1;
        scale();
        return last;
    }

    public Item get(int index) {
        if (index > size) {
            return null;
        }
            return items[front + index + 1];
    }

    public void printDeque() {
        if (size == 0) {
            System.out.println("Empty Array Deque");
            return;
        }
        for (int i = 0; i < items.length; i += 1) {
//                System.out.println(rear);
            if (items[i] != null){
                System.out.println(items[i] + " ");
            }
        }
    }

//            System.out.println("bye");


//    public static void main(String[] args) {
//        System.out.println("Running tests.\n");
//        ArrayDeque<Integer> a = new ArrayDeque<Integer>();
//        a.addLast(0);
//        a.addFirst(1);
//        a.addFirst(2);
//        a.get(0);
//        a.removeFirst();
//        a.removeFirst();
////        System.out.println(a.removeLast());
//
//        a.addLast(0);
//        a.removeLast();
//        a.addLast(2);
//        a.removeLast();
//        a.addFirst(4);
//        a.removeLast();
//        a.addFirst(6);
//        a.removeLast();
//        a.addFirst(8);
//        a.removeLast();
//        a.addFirst(10);
//
//
//
//        a.addLast(4);
//
//        a.addLast(5);
//        a.addLast(5);
//        a.addLast(5);
//        a.addLast(5);
//        a.addLast(5);
//        a.addLast(5);
//        a.addLast(5);
//        a.addLast(5);
//        System.out.println(a.removeLast());
//        a.printDeque();
//        System.out.println(a.get(0));
//        System.out.println(a.removeLast()+"*");
//        a.addLast(6);
//        System.out.println(a.removeFirst()+"*");
//        System.out.println(a.removeFirst()+"*");
//        System.out.println(a.get(0)+"*");
//        System.out.println(a.get(0)+"*");
//        a.addLast(3);
//        System.out.println(a.get(0)+"*");
//        a.addFirst(1);
//        System.out.println(a.get(0)+"*");
//        a.addFirst(0);
//        System.out.println(a.get(0)+"*");
//        a.addLast(8);
//        System.out.println(a.removeFirst());
//        a.printDeque();
//        a.addFirst(1);
//        a.addLast(10);
//
//        a.addFirst(12);
//        a.addLast(13);
////        System.out.println(a.get(12)+"&");
//        a.addLast(14);
//        a.addLast(15);
//        a.addFirst(-1);
//        a.addLast(22);
////        a.printDeque();
////        System.out.println("Getting 32nd element: " + a.get(32));
////        System.out.println("First element: " + a.removeFirst()+ "*");
////        System.out.println("Array with first removed: ");
////        a.printDeque();
////        System.out.println("Array with last removed: "+ a.removeLast() + "*");
////        a.printDeque();
//        a.addLast(0);
//        a.removeLast();
//        a.addLast(2);
//
//        a.addFirst(6);
//        a.removeLast();
//        a.addFirst(8);
//        a.removeLast();
//        a.addFirst(4);
//        a.removeLast();
//        a.removeLast();
//        a.addFirst(10);
//        a.addFirst(4);
//        a.removeLast();
//        a.removeLast();
//        a.addFirst(10);
//    }
}


