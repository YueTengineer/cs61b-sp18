public class LinkedListDeque<T> implements Deque<T>{
    private class IntNode {
        private T item;
        private IntNode prev;
        private IntNode next;

        private IntNode(T i, IntNode n, IntNode p) {
            item = i;
            next = n;
            prev = p;
        }
    }

    private IntNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new IntNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        IntNode nextNode = sentinel.next;
        sentinel.next = new IntNode(item, sentinel.next, sentinel);
        nextNode.prev = sentinel.next;
        size += 1;

    }

    @Override
    public void addLast(T item) {
        IntNode prevNode = sentinel.prev;
        sentinel.prev = new IntNode(item, sentinel, sentinel.prev);
        prevNode.next = sentinel.prev;
        size += 1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void printDeque() {
        IntNode temp = sentinel;
        while (temp.next != sentinel) {
            temp = temp.next;
            System.out.print(temp.item);
            System.out.print(" ");
        }
    }

    @Override
    public T removeFirst() {
        if (sentinel.next == sentinel) {
            return null;
        }
        IntNode firstNode = sentinel.next;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size -= 1;
        return firstNode.item;
    }

    @Override
    public T removeLast() {
        if (sentinel.prev == sentinel) {
            return null;
        }
        IntNode lastNode = sentinel.prev;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size -= 1;
        return lastNode.item;
    }

    @Override
    public T get(int index) {
        if (index > size - 1 || index < 0) {
            return null;
        }
        IntNode temp = sentinel;
        for (int i = 0; i <= index; i++) {
            temp = temp.next;
        }
        return temp.item;
    }

    public T getRecursive(int index) {
        if (index > size - 1 || index < 0) {
            return null;
        }
        return getRecursive(index, sentinel.next);
    }

    private T getRecursive(int index, IntNode n) {
        if (index == 0) {
            return n.item;
        }

        return getRecursive(index - 1, n.next);
    }
}
