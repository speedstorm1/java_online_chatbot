package p2.sorts;

import cse332.exceptions.NotYetImplementedException;
import datastructures.worklists.MinFourHeap;

import java.util.Comparator;
import java.util.Iterator;

public class HeapSort {
    public static <E extends Comparable<E>> void sort(E[] array) {
        sort(array, (x, y) -> x.compareTo(y));
    }

    public static <E> void sort(E[] array, Comparator<E> comparator) {
        MinFourHeap sortHeap = new MinFourHeap(comparator);
        for (E value : array) {
            sortHeap.add(value);
        }

        int counter = 0;
        for (Object value : sortHeap) {
            E castedValue = (E) value;
            array[counter] = castedValue;
            counter++;
        }
    }
}
