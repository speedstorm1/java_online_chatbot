package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.LIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/LIFOWorkList.java
 * for method specifications.
 */
public class ArrayStack<E> extends LIFOWorkList<E> {
    private int size;
    private int capacity;
    private E[] values;
    public ArrayStack() {
        capacity = 10;
        values = (E[])new Object[capacity];
    }

    @Override
    public void add(E work) {
        if (size == capacity) { // is full, must copy into double size array
            E[] newValues = (E[]) new Object[capacity * 2];
            for (int i = 0; i < capacity; i++) {
                newValues[i] = values[i];
            }
            values = newValues;
            capacity *= 2;
        }
        values[size] = work;
        size++;
    }

    @Override
    public E peek() {
        if (hasWork()) {
            return values[size - 1];
        }
        // if it doesn't have work
        throw new NoSuchElementException();
    }

    @Override
    public E next() {
        E work = peek(); // throws exception if it doesn't have work
        values[size - 1] = null;
        size--;
        return work;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        capacity = 10;
        values = (E[])new Object[capacity];
    }
}
