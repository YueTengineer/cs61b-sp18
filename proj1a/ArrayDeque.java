public class ArrayDeque<T> {
    private static int refactor = 2;
    private T []items;
    private int size;
    private int first;
    private int last;

    public ArrayDeque() {
        items = (T []) new Object[8];
        size = 0;
    }

    public void addFirst(T item) {
        if (size == 0) {
            items[0] = item;
            first = 0;
            last = 0;
        } else {
            if (size != items.length) {
                if (first == 0) {
                    first = items.length - 1;
                } else {
                    first -= 1;
                }
                
            } else {
                resize(size * refactor);

                first = items.length - 1;
            }
            items[first] = item;
        }
        size += 1;
    }

    public void addLast(T item) {
        if (size == 0) {
            items[0] = item;
            last = 0;
            first = 0;
        } else {
            if(size != items.length) {
                if(last == items.length-1) {
                    last = 0;
                } else {
                    last = size+first-items.length;
                    last = convert(last);
                }
            } else {
                resize(size * refactor);
                last += 1;
            }
            items[last] = item;
        }
        size += 1;
    }

    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        T temp = items[first];
        items[first] = null;
        if(first == items.length-1) {
            first = 0;
        } else {
            first += 1;
        }
        size -= 1;

        if(items.length >= 16 && getUsageRatio()<0.25) {
            resize((int)(0.5 * items.length));
        }
        return temp;
    }

    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        T temp = items[last];
        items[last] = null;
        if (last==0) {
            last = items.length-1;
        } else {
            last -= 1;
        }
        size -= 1;
        if (items.length >= 16 && getUsageRatio()<0.25) {
            resize((int) (0.5 * items.length));
        }
        return temp;
    }

    public boolean isEmpty() {
        return size == 0 ;
    }

    public int size() {
        return size;
    }


    public void printDeque() {
        if(first <= last) {
            for(int i = first; i <= last; i++) {
                System.out.print(items[i]);
                System.out.print(" ");
            }
        }else{
            for(int j = first; j < items.length; j++) {
                System.out.print(items[j]);
                System.out.print(" ");
            }
            for(int j = 0; j <= last; j++) {
                System.out.print(items[j]);
                System.out.print(" ");
            }
        }
    }

    private void resize(int cap) {
        T[] a = (T []) new Object[cap];

        if(first > last) {
            System.arraycopy(items,first,a,0,items.length-first);
            System.arraycopy(items,0,a,items.length-first,last+1);
        } else {
            System.arraycopy(items,first,a,0,size);
        }
        items = a;
        first = 0;
        last = size-1;
    }

    /* when first/last pointer becomes negative, convert the index to the corresponding positive */
    private int convert(int i) {
        if (i >= 0) {
            return i;
        } else {
            return items.length+i;
        }
    }

    public T get(int i) {
        if (first <= last) {
            return items[first+i];
        } else {
            if (first + i < items.length) {
                return items[first+i];
            } else {
                return items[i+first-items.length];
            }
        }

    }

    private float getUsageRatio() {
        return (float) size/items.length;
    }
}
