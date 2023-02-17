package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.SimpleIterator;
import cse332.interfaces.trie.TrieMap;
import cse332.types.AlphabeticString;
import cse332.types.BString;

import java.util.AbstractMap;
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
    public class HashTrieNode extends TrieNode<ChainingHashTable<A, HashTrieNode>, HashTrieNode> {
        public HashTrieNode() {
            this(null);
        }

        public HashTrieNode(V value) {
            this.pointers = new ChainingHashTable<>(AVLTree<A, HashTrieNode>::new);
            this.value = value;
        }

        @Override
        public Iterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>> iterator() {
            return new HTNIterator();
        }

        private class HTNIterator extends SimpleIterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>> {
            Iterator<Item<A, HashTrieNode>> CHTIterator;

            public HTNIterator() {
                CHTIterator = pointers.iterator();
            }

            public Entry<A, HashTrieNode> next() {
                Item<A, HashTrieNode> nextItem = CHTIterator.next();
                return new AbstractMap.SimpleEntry<>(nextItem.key, nextItem.value);
            }

            public boolean hasNext() {
                return CHTIterator.hasNext();
            }

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
            if (current.pointers.find(letter) == null) { // add to trie
                current.pointers.insert(letter, new HashTrieNode()); // may cause an issue cause find inserts?
            }
            current = current.pointers.find(letter); // update this
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
            if (current.pointers.find(letter) != null) {
                current = current.pointers.find(letter);
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
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
