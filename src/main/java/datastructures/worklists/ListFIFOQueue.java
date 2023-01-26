package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FIFOWorkList.java
 * for method specifications.
 */
public class ListFIFOQueue<E> extends FIFOWorkList<E> {
    private ListNode<E> head;
    private ListNode<E> tail;
    private int size;

    @Override
    public void add(E work) {
        if (head == null) {
            head = new ListNode<>(work, null);
            tail = head;
        } else {
            tail.next = new ListNode<>(work, null);
            tail = tail.next;
        }
        size++;
    }

    @Override
    public E peek() {
        if (hasWork()) {
            return head.data;
        }
        throw new NoSuchElementException();
    }

    @Override
    public E next() {
        E work = peek();
        head = head.next;
        size--;
        return work;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    private class ListNode<E> {
        public E data;
        public ListNode<E> next;
        public ListNode(E data, ListNode<E> next) {
            this.data = data;
            this.next = next;
        }
    }

}
