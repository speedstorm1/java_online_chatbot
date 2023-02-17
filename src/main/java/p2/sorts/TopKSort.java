package p2.sorts;

import cse332.exceptions.NotYetImplementedException;
import datastructures.worklists.MinFourHeap;

import java.util.Comparator;

public class TopKSort {
    public static <E extends Comparable<E>> void sort(E[] array, int k) {
        sort(array, k, (x, y) -> x.compareTo(y));
    }

    /**
     * Behaviour is undefined when k > array.length
     */
    public static <E> void sort(E[] array, int k, Comparator<E> comparator) {
        if (k <= array.length) {
            MinFourHeap sortHeap = new MinFourHeap(comparator);
            System.out.println(sortHeap.size());
            System.out.println(k);
            for (E value : array) {
                sortHeap.add(value);
                if (sortHeap.size() > k) {
                    sortHeap.next();
                }
            }
            for (int i = 0; i < k; i++) {
                array[i] = (E) sortHeap.next();
            }
            for (int i = k; i < array.length; i++) {
                array[i] = null;
            }
        }
    }
}
