package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FixedSizeFIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FixedSizeFIFOWorkList.java
 * for method specifications.
 */
public class CircularArrayFIFOQueue<E extends Comparable<E>> extends FixedSizeFIFOWorkList<E> {
    private int front;
    private E[] values;
    private int back;
    private int size;
    public CircularArrayFIFOQueue(int capacity) {
        super(capacity);
        values = (E[])new Comparable[capacity];
    }

    @Override
    public void add(E work) {
        if (isFull()) {
            throw new IllegalStateException();
        }
        values[back] = work;
        size++;
        back = (back + 1) % capacity();
    }

    @Override
    public E peek() {
        return(peek(0));
    }

    @Override
    public E peek(int i) {
        if (!(hasWork())) {
            throw new NoSuchElementException();
        }
        if (i < 0 || i >= size()) {
            throw new IndexOutOfBoundsException();
        }
        return values[(front + i) % capacity()];
    }

    @Override
    public E next() {
        E frontValue = peek();
        front = (front + 1) % capacity();
        size--;
        return frontValue;
    }

    @Override
    public void update(int i, E value) {
        if (!(hasWork())) {
            throw new NoSuchElementException();
        }
        if (i < 0 || i >= size()) {
            throw new IndexOutOfBoundsException();
        }
        values[(front + i) % capacity()] = value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        front = 0;
        back = 0;
        size = 0;
    }

    @Override
    public int compareTo(FixedSizeFIFOWorkList<E> other) {
        int index = 0;
        while (index < this.size && index < other.size()) {
            if (this.peek(index).compareTo(other.peek(index)) != 0) {
                return this.peek(index).compareTo(other.peek(index));
            }
            index++;
        }
        if (this.size == other.size()) {
            return 0;
        }
        return this.size - other.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof FixedSizeFIFOWorkList<?>)) {
            return false;
        } else {
            FixedSizeFIFOWorkList<E> other = (FixedSizeFIFOWorkList<E>) obj;
            if (this.size != other.size()) {
                return false;
            }
            int queueSize = this.size;
            for (int i = 0; i < queueSize; i++) {
                if (this.peek(i).compareTo(other.peek(i)) != 0) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        if (size == 0) {
            return 0;
        }
        int result = 0;
        for (int i = 0; i < size; i++) {
            result = 31 * result + values[i].hashCode();
        }
        return result;
    }
}
