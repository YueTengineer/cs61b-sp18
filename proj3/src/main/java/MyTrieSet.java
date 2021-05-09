import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * {@code MyTrieSet} class MyTrieSet implements the TrieSet61B interface using a
 * Trie as its core data structure. Allows finding keys with given prefix application.
 *
 */

public class MyTrieSet  {

    private Node root;

    /**
     * Stores a character, key identification, and a HashMap for the following
     * characters.
     */
    private static class Node {
        private char ch;
        private boolean isKey;
        private HashMap<Character, Node> next;
        private Node(char c, boolean k) {
            ch = c;
            isKey = k;
            next = new HashMap<>();
        }
    }

    /**
     * Initializes the root node with an arbitrary character.
     */
    public MyTrieSet() {
        root = new Node('r', false);
    }

    /**
     * Clear all following nodes besides the root.
     */

    public void clear() {
        root.next.clear();
    }

    /**
     * Return {@code true} if this trie set contains the given {@code key}.
     *
     * @param key the key string
     * @return {@code true} if this trie set contains the given {@code key} and
     *         {@code false} otherwise.
     * @throws IllegalArgumentException if {@code key} is {@code null} or size is 0
     */

    public boolean contains(String key) {
        if (key == null || key.length() < 1) {
            throw new IllegalArgumentException("argument to contains() is null or size is 0");
        }
        Node curr = root;
        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            if (!curr.next.containsKey(ch)) {
                return false;
            }
            curr = curr.next.get(ch);
        }
        return true;
    }

    /**
     * Inserts the key into the trie set.
     *
     * @param key the key
     * @throws IllegalArgumentException if {@code key} is {@code null} or size is 0
     */

    public void add(String key) {
        if (key == null || key.length() < 1) {
            throw new IllegalArgumentException("argument to add() is null or size is 0");
        }
        addHelper(root, key);
    }

    /**
     * Helper function to recursively match and insert characters of the given key.
     *
     * @param n the node
     * @param k the key
     */
    private void addHelper(Node n, String k) {
        char ch = k.charAt(0);
        if (!n.next.containsKey(ch)) {
            n.next.put(ch, new Node(ch, false));
        }
        Node nextNode = n.next.get(ch);
        if (k.length() == 1) {
            nextNode.isKey = true;
        }
        else {
            addHelper(nextNode, k.substring(1));
        }
    }

    /**
     * Returns all keys in the trie set.
     *
     * @param prefix the prefix string
     * @return the list of keys with the given prefix
     * @throws IllegalArgumentException if {@code key} is {@code null} or size is 0
     */

    public List<String> keysWithPrefix(String prefix) {
        if (prefix == null || prefix.length() < 1) {
            throw new IllegalArgumentException("argument to keysWithPrefix() is null or size is 0");
        }
        Node node = getEndNode(prefix);
        if (node == null) {
            return null;
        }
        ArrayList<String> keys = new ArrayList<>();
        for (Character ch : node.next.keySet()) {
            collectHelper(prefix + ch, node.next.get(ch), keys);
        }
        return keys;
    }

    /**
     * Helper function to get the last node of the given {@code key}.
     *
     * @param key the key
     * @return the last node of the given {@code key}.
     */
    private Node getEndNode(String key) {
        Node curr = root;
        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            if (!curr.next.containsKey(ch)) {
                return null;
            }
            curr = curr.next.get(ch);
        }
        return curr;
    }

    /**
     * Helper function to recursively add the keys with the given prefix, and
     * moves on the the following characters in the trie table.
     *
     * @param s the prefix string
     * @param n the node
     * @param keys the list of keys with the given prefix
     */
    private void collectHelper(String s, Node n, List<String> keys) {
        if (n.isKey == true) {
            keys.add(s);
        }
        for (Character ch : n.next.keySet()) {
            collectHelper(s + ch, n.next.get(ch), keys);
        }
    }


    /**
     * To find the longest key that is a prefix of g given string.
     * @param key the prefix string
     * @return  the longest key that is a prefix of g given string
     */


    public String longestPrefixOf(String key) {
        int length = search(root, key, 0, 0);
        return key.substring(0,length);
    }

    public int search(Node x, String s, int d, int length) {
        if (x == null) return length;
        if (x.isKey == true) length = d;
        if (d == s.length()) return length;
        char ch = s.charAt(d);
        return search(x.next.get(ch), s,d + 1, length);
    }

}
