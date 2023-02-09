package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.Dictionary;
import cse332.interfaces.misc.SimpleIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * 1. You must implement a generic chaining hashtable. You may not
 * restrict the size of the input domain (i.e., it must accept
 * any key) or the number of inputs (i.e., it must grow as necessary).
 * 3. Your HashTable should rehash as appropriate (use load factor as
 * shown in class!).
 * 5. HashTable should be able to resize its capacity to prime numbers for more
 * than 200,000 elements. After more than 200,000 elements, it should
 * continue to resize using some other mechanism.
 * 6. We suggest you hard code some prime numbers. You can use this
 * list: http://primes.utm.edu/lists/small/100000.txt
 * NOTE: Do NOT copy the whole list!
 * 7. When implementing your iterator, you should NOT copy every item to another
 * dictionary/list and return that dictionary/list's iterator.
 */
public class ChainingHashTable<K, V> extends DeletelessDictionary<K, V> {
    private Supplier<Dictionary<K, V>> newChain;
    private static int primeIndex;
    private int capacity;
    public static final int[] primes = {19, 41, 83, 167, 337, 677, 1361, 2729, 5471, 10949, 21911, 43853, 87719, 175447};
    private Dictionary<K, V>[] values;
    public ChainingHashTable(Supplier<Dictionary<K, V>> newChain) {
        this.newChain = newChain;
        values = new Dictionary[19];
    }

    @Override
    public V insert(K key, V value) {
        V oldValue = find(key);
        if (value == null) {
            throw new IllegalArgumentException();
        }
        if (size >= capacity) {
            rehash();
        }
        int hashcode = key.hashCode();
        if (values[hashcode % capacity] == null) {
            values[hashcode % capacity] = newChain.get();
        }
        values[hashcode % capacity].insert(key, value);
        if (value != oldValue) {
            size++;
        }
        return oldValue;
    }

    @Override
    public V find(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (capacity == 0) { // just built table
            capacity = primes[0];
        }
        int hashcode = key.hashCode();
        hashcode = hashcode % capacity;
        if (values[hashcode] == null) {
            return null;
        }
        if ((values[hashcode].find(key)) != null) {
            return values[hashcode].find(key);
        }
        return null;
    }

    private void rehash() {
        resize();
        Dictionary<K, V>[] newValues = new Dictionary[capacity];
        for (Dictionary dict : values) {
            if (dict != null) {
                Iterator itr = dict.iterator();
                while (itr.hasNext()) {
                    Item<K, V> item = (Item<K, V>) itr.next();
                    if (newValues[item.key.hashCode() % capacity] == null) {
                        newValues[item.key.hashCode() % capacity] = newChain.get();
                    }
                    newValues[item.key.hashCode() % capacity].insert(item.key, item.value);
                }
            }
        }
        this.values = newValues;
    }

    private void resize() {
        if (primeIndex != 14) {
            capacity = primes[primeIndex];
            primeIndex++;
        } else {
            // else, find the next prime
            capacity *= 2;
            while (!isPrime(capacity)) {
                capacity++;
            }
        }
    }

    private static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        for (int i = 2; i <= Math.sqrt(n) + 1; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<Item<K, V>> iterator() {
        return new CHTIterator();
    }

    private class CHTIterator extends SimpleIterator<Item<K, V>> {
        int index;
        Iterator <Item<K, V>> pairIterator;
        int dictionaryIndex;
        Dictionary<K, V> currentDictionary;

        public CHTIterator() {
            currentDictionary = values[dictionaryIndex];
            while (currentDictionary == null) {
                dictionaryIndex++;
                currentDictionary = values[dictionaryIndex];
            }
            pairIterator = currentDictionary.iterator();
        }

        @Override
        public Item<K, V> next() {
            if (index > size) {
                throw new NoSuchElementException();
            }
            index++;
            if (pairIterator.hasNext()) {
                return pairIterator.next();
            }
            dictionaryIndex++;
            currentDictionary = values[dictionaryIndex];
            while (currentDictionary == null) {
                dictionaryIndex++;
                if (dictionaryIndex >= capacity) {
                    System.out.println("error reacher");
                    System.out.println("Size: " + size + " Index: " + index + " DI: " + dictionaryIndex);
                }
                currentDictionary = values[dictionaryIndex];
            }
            pairIterator = currentDictionary.iterator();
            return pairIterator.next();
        }

        @Override
        public boolean hasNext() {
            return (index < size);
        }
    }

//    private class ListNode<K, V> {
//        public int hashcode;
//
//        public Dictionary<K, V> value;
//        public ListNode next;
//
//        public ListNode(Dictionary<K, V> value, ListNode next, int hashcode) {
//            this.hashcode = hashcode;
//            this.value = value;
//            this.next = next;
//        }
//    }


}
