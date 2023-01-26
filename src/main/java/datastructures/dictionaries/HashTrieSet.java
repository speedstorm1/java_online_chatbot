package datastructures.dictionaries;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.trie.TrieSet;
import cse332.types.BString;

public class HashTrieSet<A extends Comparable<A>, E extends BString<A>> extends TrieSet<A, E> {
    /* Note: You should not be adding any methods to this class...you only need to implement
     *       the constructor!  */

    public HashTrieSet(Class<E> Type) {
        super(new HashTrieMap<>(Type));
    }

}
