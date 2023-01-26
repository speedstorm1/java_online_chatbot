package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.PriorityWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/PriorityWorkList.java
 * for method specifications.
 */
public class MinFourHeapComparable<E extends Comparable<E>> extends PriorityWorkList<E> {
    /* Do not change the name of this field; the tests rely on it to work correctly. */
    private E[] data;
    private int size;
    private int capacity;

    public MinFourHeapComparable() {
        capacity = 10;
        data = (E[])new Comparable[capacity];
    }

//    @Override
//    public boolean hasWork() {
//        throw new NotYetImplementedException();
//    }

    @Override
    public void add(E work) {
        if (size == capacity) {
            E[] newData = (E[]) new Comparable[capacity * 2];
            for (int i = 0; i < capacity; i++) {
                newData[i] = data[i];
            }
            data = newData;
            capacity *= 2;
        }
        int i = percolateUp(size, work);
        data[i] = work;
        size++;
    }

    private int percolateUp(int index, E work) {
        while (index > 0 && work.compareTo(data[parent(index)]) < 0) {
            data[index] = data[parent(index)];
            index = parent(index);
        }
        return index;
    }

    @Override
    public E peek() {
        if (hasWork()) {
            return data[0];
        }
        // if it doesn't have work
        throw new NoSuchElementException();
    }

    @Override
    public E next() {
        E work = peek(); // throws exception if it doesn't have work
        int index = percolateDown(0, data[size - 1]);
        data[index] = data[size - 1];
        data[size - 1] = null;
        size--;
        return work;
    }

    private int percolateDown(int index, E work) {
        while (4 * index + 1 < size) {
            int minChild = minChildIndex(index);
            if (data[minChild].compareTo(work) < 0) {
                data[index] = data[minChild];
                index = minChild;
            } else {
                break;
            }
        }
        return index;
    }

    private int minChildIndex(int parent) {
        int min = 4 * parent + 1;
        for (int i = 1; i < 4; i++) {
            if (min + i >= size) {
                break;
            }
            if (data[4 * parent + 1 + i].compareTo(data[min]) < 0) {
                min = 4 * parent + 1 + i;
            }
        }
        return min;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        capacity = 10;
        data = (E[])new Comparable[capacity];
        size = 0;
    }

    private int parent(int child) {
        return (child - 1) / 4;
    }
}
