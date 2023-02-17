package p2.sorts;

import cse332.exceptions.NotYetImplementedException;

import java.util.Comparator;

public class QuickSort {
    public static <E extends Comparable<E>> void sort(E[] array) {
        QuickSort.sort(array, (x, y) -> x.compareTo(y));
    }

    public static <E> void sort(E[] array, Comparator<E> comparator) {
        sortHelper(array, comparator, 0, array.length - 1);
    }

    private static <E> void sortHelper(E[] array, Comparator<E> comparator, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(array, low, high, comparator);
            sortHelper(array, comparator, low, pivotIndex);
            sortHelper(array, comparator, pivotIndex + 1, high);
        }
    }

    // partitions array into sections greater or lower than the pivot element (chosen at the end of the array)
    // and returns the middle index of the partition --> where the pivot fits
    private static <E> int partition(E[] array, int low, int high, Comparator<E> comparator) {
        if (high - low == 1) {
            if (comparator.compare(array[low], array[high]) > 0) {
                E temp = array[low];
                array[low] = array[high];
                array[high] = temp;
                return low;
            }
        }
        E pivot = array[high];
        int i = low;
        int j = high - 1;
        while (i < j) {
            if (comparator.compare(array[i], pivot) < 0) {
                i++;
            } else if (comparator.compare(array[j], pivot) > 0) {
                j--;
            } else {
                E temp = array[j];
                array[j] = array[i];
                array[i] = temp;
                i++;
            }
        }
        if (comparator.compare(array[high], array[i]) < 0) {
            array[high] = array[i];
            array[i] = pivot;
        }
        return i;
    }
}
