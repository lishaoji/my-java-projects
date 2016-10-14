

/* Node class and some other code from lec 5 code
 */
public class LinkedListDeque<Item>{
    private class Node {
        public Item item;
        public Node prev;
        public Node next;

        public Node(Item i){
            item = i;
            prev = null;
            next = null;
        }
    }

    private Node sentinel;
    private Node current;
    private int size;

    public LinkedListDeque(){
        size = 0;
        sentinel = new Node(null);
        sentinel.next = sentinel.prev = sentinel;
        current = sentinel;
    }

    /*Inspired from http://www.cs.dartmouth.edu/~cs5/lectures/0525/SentinelDLL.java */
    public void addFirst(Item x){
        size += 1;
        Node newFirst = new Node(x);
        newFirst.next = sentinel.next;
        newFirst.prev = sentinel;
        sentinel.next.prev = newFirst;
        sentinel.next = newFirst;
    }

    public void addLast(Item x) {
        size += 1;
        Node newBack = new Node(x);
        newBack.prev = sentinel.prev;
        newBack.next = sentinel;
        sentinel.prev.next = newBack;
        sentinel.prev = newBack;
    }

    public boolean isEmpty(){

        return (size == 0);
    }

    public int size(){

        return size;
    }

    public void printDeque(){
        if (size == 0){
            System.out.println("Empty linked list deque\n");
            return;
        }
        Node p = sentinel;
        int index = size;
        while (index > 1){
            System.out.print(p.next.item + " ");
            p = p.next;
            index -= 1;
        }
        System.out.println(p.next.item);

    }

    public Item removeFirst(){
        if (sentinel.next != null){
            Node temp = sentinel.next;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size -= 1;
            return temp.item;
        }
        return null;
    }

    public Item removeLast(){
        if (sentinel.prev != null){
            Node temp = sentinel.prev;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size -= 1;
            return temp.item;
        }
        return null;
    }

    public Item get(int index){
        Node p = sentinel;
        if (size < index) {
            return null;
        }

        while (index > 0 && p.next != null){
            p = p.next;
            index -= 1;
        }
        return p.next.item;
    }


    public Item getRecursive(int index){
        if (index > size){
            return null;
        }
        else if(index == 0){
            return current.next.item;
        }
        else{
            current = current.next;
            return getRecursive(index-1);
        }
    }
    /* Testing
    public static void main(String[] args) {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        lld1.addFirst(10);
        lld1.addLast(9);
        lld1.addFirst(11);
        lld1.printDeque(); 
        lld1.removeFirst();
        lld1.printDeque();
        lld1.removeLast();
        lld1.printDeque();
    }
    */
}