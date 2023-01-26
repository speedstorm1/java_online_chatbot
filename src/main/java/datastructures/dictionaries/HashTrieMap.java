package datastructures.dictionaries;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.trie.TrieMap;
import cse332.types.BString;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * See cse332/interfaces/trie/TrieMap.java
 * and cse332/interfaces/misc/Dictionary.java
 * for method specifications.
 */
public class HashTrieMap<A extends Comparable<A>, K extends BString<A>, V> extends TrieMap<A, K, V> {
    public class HashTrieNode extends TrieNode<Map<A, HashTrieNode>, HashTrieNode> {
        public HashTrieNode() {
            this(null);
        }

        public HashTrieNode(V value) {
            this.pointers = new HashMap<A, HashTrieNode>();
            this.value = value;
        }

        @Override
        public Iterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>> iterator() {
            return pointers.entrySet().iterator();
        }
    }

    public HashTrieMap(Class<K> KClass) {
        super(KClass);
        this.root = new HashTrieNode();
    }

    @Override
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        HashTrieNode current = (HashTrieNode) root;
        V oldVal = find(key);
        for (A letter : key) {
            if (!current.pointers.containsKey(letter)) { // add to trie
                current.pointers.put(letter, new HashTrieNode());
            }
            current = current.pointers.get(letter);
        }
        if (oldVal == null) {
            size++;
        }
        current.value = value;
        return oldVal;
    }

    @Override
    public V find(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }

        HashTrieNode node = (HashTrieNode) find(key, root);
        if (node == null) { // doesn't contain the key
            return null;
        } else {
            return node.value;
        }
    }

    /**
     * Returns the node corresponding to the key from the given root or null if none exists
     * @param key the key whose associated node will be returned
     * @param root the node from which the searching happens
     * @return the node corresponding to the key, or null if there is none
     */
    private TrieNode find(K key, TrieNode root) {
        HashTrieNode current = (HashTrieNode) root;
        for (A letter : key) { // iterates through each letter in our key
            if (current.pointers.containsKey(letter)) {
                current = current.pointers.get(letter);
            } else { // map doesn't contain this key
                return null;
            }
        }
        return current;
    }

    @Override
    public boolean findPrefix(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (size == 0) {
            return false;
        }
        HashTrieNode node = (HashTrieNode) find(key, root);
        return (node != null);
    }

    @Override
    public void delete(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        HashTrieNode node = (HashTrieNode) find(key, root);
        // case: mapping does not exist
        if (node == null) {
            return;
        }
        // case: other nodes are dependent on this
        if (node.pointers.keySet().size() > 0) {
            if (node.value != null) {
                size--;
            }
            node.value = null;
            return;
        }

        HashTrieNode lowestInUse = (HashTrieNode) root;
        HashTrieNode current = (HashTrieNode) root;
        A lowestChar = null;
        for (A letter : key) {
            if (current.value != null || current.pointers.keySet().size() > 1) {
                lowestInUse = current;
                lowestChar = letter;
            }
            if (current.pointers.get(letter) == node) {
                break;
            }
            current = current.pointers.get(letter);
        }
        size--;
        if (lowestChar == null) {
            clear();
        } else {
            lowestInUse.pointers.remove(lowestChar);
        }
    }

    @Override
    public void clear() {
        ((HashTrieNode)root).pointers.clear();
        ((HashTrieNode)root).value = null;
        size = 0;
    }
}
