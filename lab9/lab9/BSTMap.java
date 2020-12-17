package lab9;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */
    Set<K> keyset = new HashSet<>();

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (key == null) throw new IllegalArgumentException("calls get() with a null key");
        if (p == null) return null;
        int cmp = key.compareTo(p.key);
        if      (cmp < 0) return getHelper(key , p.left);
        else if (cmp > 0) return getHelper(key , p.right);
        else              return p.value;
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size += 1;
            return new Node(key, value);
        }
        int cmp = key.compareTo(p.key);
        if      (cmp < 0) p.left  = putHelper(key, value, p.left);
        else if (cmp > 0) p.right = putHelper(key, value, p.right);
        else              p.value   = value;
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("calls put() with a null key");
        if (value == null) {
            delete(key);
            return;
        }
        root = putHelper(key, value, root);
        keyset.add(key);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */

    @Override
    public V remove(K key) {
        V temp_value = get(key);
        delete(key);
        if (temp_value != null) {
            keyset.remove(key);
            size -= 1;
        }
        return temp_value;

    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/

    @Override
    public V remove(K key, V value) {
        V temp_value = get(key);
        delete(key);
        if (temp_value != null) {
            keyset.remove(key);
            size -= 1;
        }
        return temp_value;

    }

    @Override
    public Iterator<K> iterator() {
        return keyset.iterator();
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        return keyset;
    }

    public void delete(K key) {
        if (key == null) throw new IllegalArgumentException("calls delete() with a null key");
        root = delete(root, key);
    }

    private Node delete(Node p, K key) {
        if (p == null) return null;
        int cmp = key.compareTo(p.key);
        if      (cmp < 0) p.left  = delete(p.left,  key);
        else if (cmp > 0) p.right = delete(p.right, key);
        else {
            if (p.right == null) return p.left;
            if (p.left  == null) return p.right;
            Node t = p;
            p = min(t.right);
            p.right = deleteMin(t.right);
            p.left = t.left;
        }
        return p;
    }

    public void deleteMin() {
        if (isEmpty()) throw new IllegalArgumentException("Symbol table underflow");
        root = deleteMin(root);
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        return x;
    }

    public void deleteMax() {
        if (isEmpty()) throw new IllegalArgumentException("Symbol table underflow");
        root = deleteMax(root);
    }
    public Node deleteMax(Node x) {
        if (x.right == null) return x.left;
        x.right = deleteMax(x.right);
        return x;
    }

    public K min() {
        if (isEmpty()) throw new IllegalArgumentException("calls min() with empty symbol table");
        return min(root).key;
    }

    private Node min(Node p) {
        if (p.left == null) return p;
        else                return min(p.left);
    }
    public K max() {
        if (isEmpty()) throw new IllegalArgumentException("calls max() with empty symbol table");
        return max(root).key;
    }

    private Node max(Node x) {
        if (x.right == null) return x;
        else                 return max(x.right);
    }

}
