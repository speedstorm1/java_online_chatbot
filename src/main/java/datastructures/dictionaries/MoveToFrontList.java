package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.datastructures.trees.BinarySearchTree;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.SimpleIterator;
import cse332.interfaces.worklists.WorkList;
import datastructures.worklists.ArrayStack;
import datastructures.worklists.ListFIFOQueue;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 1. The list is typically not sorted.
 * 2. Add new items to the front of the list.
 * 3. Whenever find or insert is called on an existing key, move it
 * to the front of the list. This means you remove the node from its
 * current position and make it the first node in the list.
 * 4. You need to implement an iterator. The iterator SHOULD NOT move
 * elements to the front.  The iterator should return elements in
 * the order they are stored in the list, starting with the first
 * element in the list. When implementing your iterator, you should
 * NOT copy every item to another dictionary/list and return that
 * dictionary/list's iterator.
 */
public class MoveToFrontList<K, V> extends DeletelessDictionary<K, V> {
    private ListNode root;
    @Override
    public V insert(K key, V value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        V oldVal = find(key);
        if (root == null) {
            root = new ListNode<>(key, value, null);
            size++;
        } else if (root.key.equals(key)) {
            oldVal = (V) root.data;
            root.data = value;
            return oldVal;
        } else if (findPrev(key) == null) { // list doesn't contain this key, so we just insert new node @ front
            root = new ListNode(key, value, root);
            size++;
        } else {
            ListNode prev = findPrev(key);
            ListNode newRoot = prev.next;
            prev.next = prev.next.next;
            newRoot.next = root;
            root = newRoot;
        }
        root.data = value;
        return oldVal;
    }


    public ListNode findPrev(K key) { // returns the previous node of the node with the value
        ListNode curr = root.next;
        ListNode prev = root;
        while (curr != null) {
            if (curr.key.equals(key)) {
                return prev;
            }
            prev = prev.next;
            curr = curr.next;
        }
        return null;
    }

    @Override
    public V find(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            return null;
        }
        if (root.key.equals(key)) {
            return (V) root.data;
        }
        ListNode prev = findPrev(key);
        if (prev == null) {
            return null;
        }
        return (V) prev.next.data;
    }

    @Override
    public Iterator<Item<K, V>> iterator() {
        return new FLIterator();
    }




    private class FLIterator extends SimpleIterator<Item<K, V>> {
        private MoveToFrontList.ListNode current;
        public FLIterator() {
        //    if (root != null) {
                this.current = root;
          //  }
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item<K, V> next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            Item result = new Item(current.key, current.data);
            current = current.next;
            return result;
        }
    }

    private class ListNode<K, V> {
        public K key;
        public V data;
        public MoveToFrontList.ListNode next;
        public ListNode(K key, V data, MoveToFrontList.ListNode next) {
            this.data = data;
            this.next = next;
            this.key = key;
        }
    }
}

/*
*  private class FLIterator extends SimpleIterator<Item<K, V>> {
        private final WorkList<MoveToFrontList.ListNode> nodes;
        private MoveToFrontList.ListNode current;

        public FLIterator() {
            if (root != null) {
                this.current = root;
            }
            this.nodes = new ArrayStack<>();
        }

        @Override
        public boolean hasNext() {
            return this.current != null || this.nodes.hasWork();
        }

        @Override
        public Item<K, V> next() {
            while (this.current != null) {
                this.nodes.add(current);
                this.current = current.next;
            }
            current = nodes.next();
            Item<K, V> value = new Item(current.key, current.data);
            return value;
        }
    }
* */