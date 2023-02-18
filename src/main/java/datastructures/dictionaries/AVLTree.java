package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.datastructures.trees.BinarySearchTree;
import cse332.interfaces.worklists.WorkList;
import datastructures.worklists.ArrayStack;

import java.lang.reflect.Array;

/**
 * AVLTree must be a subclass of BinarySearchTree<E> and must use
 * inheritance and calls to superclass methods to avoid unnecessary
 * duplication or copying of functionality.
 * <p>
 * 1. Create a subclass of BSTNode, perhaps named AVLNode.
 * 2. Override the insert method such that it creates AVLNode instances
 * instead of BSTNode instances.
 * 3. Do NOT "replace" the children array in BSTNode with a new
 * children array or left and right fields in AVLNode.  This will
 * instead mask the super-class fields (i.e., the resulting node
 * would actually have multiple copies of the node fields, with
 * code accessing one pair or the other depending on the type of
 * the references used to access the instance).  Such masking will
 * lead to highly perplexing and erroneous behavior. Instead,
 * continue using the existing BSTNode children array.
 * 4. Ensure that the class does not have redundant methods
 * 5. Cast a BSTNode to an AVLNode whenever necessary in your AVLTree.
 * This will result a lot of casts, so we recommend you make private methods
 * that encapsulate those casts.
 * 6. Do NOT override the toString method. It is used for grading.
 * 7. The internal structure of your AVLTree (from this.root to the leaves) must be correct
 */

public class AVLTree<K extends Comparable<? super K>, V> extends BinarySearchTree<K, V> {
    // TODO: Implement me!


    // given a root node and a key, it updates the tree with the key, performing
    // all the necessary rotations and then returns the root
    private AVLNode findHelper(K key, AVLNode currentRoot) {
        if (currentRoot == null) {
            return new AVLNode(key, null, 0);
        }
        int compare = key.compareTo(currentRoot.key);
        if (compare < 0) {
            currentRoot.children[0] = findHelper(key, (AVLNode) currentRoot.children[0]);
        } else if (compare > 0) {
            currentRoot.children[1] = findHelper(key, (AVLNode) currentRoot.children[1]);
        }
        // else you found the node and just update the value and balance the tree
        return balance(currentRoot);
    }

    // avl balances the modified tree
    private AVLNode balance(AVLNode currentRoot) {
        if (currentRoot == null) {
            return null;
        }
        if (height(currentRoot.getLeft()) - height(currentRoot.getRight()) > 1) {
            if (height(currentRoot.getLeft().getLeft()) - height(currentRoot.getLeft().getRight()) >= 0) {
                currentRoot = rotateLeftLeft(currentRoot);
            } else {
                currentRoot = rotateLeftRight(currentRoot);
            }
        } else if (height(currentRoot.getLeft()) - height(currentRoot.getRight()) < -1) {
            if (height(currentRoot.getRight().getRight()) - height(currentRoot.getRight().getLeft()) >= 0) {
                currentRoot = rotateRightRight(currentRoot);
            } else {
                currentRoot = rotateRightLeft(currentRoot);
            }
        }
        currentRoot.height = Math.max(height(currentRoot.getLeft()), height(currentRoot.getRight())) + 1;
        return currentRoot;
    }

    // case 1 (left-left) of avl rotation
    private AVLNode rotateLeftLeft(AVLNode currentRoot) {
        AVLNode newRoot = (AVLNode) currentRoot.children[0];
        currentRoot.children[0] = newRoot.children[1];
        newRoot.children[1] = currentRoot;
        currentRoot.height = Math.max(height(currentRoot.getLeft()), height(currentRoot.getRight())) + 1;
        newRoot.height = Math.max(height(newRoot.getLeft()), height(currentRoot)) + 1;
        return newRoot;
    }

    // case 2 (left-right) of avl rotation
    private AVLNode rotateLeftRight(AVLNode currentRoot) {
        currentRoot.children[0] = rotateRightRight((AVLNode) currentRoot.children[0]);
        currentRoot = rotateLeftLeft(currentRoot);
        return currentRoot;
    }

    // case 3 (right-left) of avl rotation
    private AVLNode rotateRightLeft(AVLNode currentRoot) {
        currentRoot.children[1] = rotateLeftLeft((AVLNode) currentRoot.children[1]);
        currentRoot = rotateRightRight(currentRoot);
        return currentRoot;
    }

    // case 4 (right-right) of avl rotation
    private AVLNode rotateRightRight(AVLNode currentRoot) {
        AVLNode newRoot = (AVLNode) currentRoot.children[1];
        currentRoot.children[1] = newRoot.children[0];
        newRoot.children[0] = currentRoot;
        currentRoot.height = Math.max(height(currentRoot.getLeft()), height(currentRoot.getRight())) + 1;
        newRoot.height = Math.max(height(newRoot.getRight()), height(currentRoot)) + 1;
        return newRoot;
    }


    // Returns the newly inserted node of the given key and value
    // with all necessary avl tree adjustments made
    @Override
    protected BinarySearchTree<K, V>.BSTNode find(K key, V value) {
        if (value != null) {
            root = findHelper(key, (AVLNode) root);
        }
        // updated tree, now must find the actual node
        AVLNode current = (AVLNode) root;
        int child = -1;
        while (current != null) {
            int direction = Integer.signum(key.compareTo(current.key));
            // We found the key
            if (direction == 0) {
                return current;
            }
            else {
                // direction + 1 = {0, 2} -> {0, 1}
                child = Integer.signum(direction + 1);
                current = (AVLNode) current.children[child];
            }
        }
        return current;
    }

    @Override
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }

        if (find(key) != null) { // does this key exist?
            AVLNode result = (AVLNode) find(key, null);
            V oldValue = result.value;
            result.value = value;
            return oldValue;
        }

        AVLNode current = (AVLNode) find(key, value);
        V oldValue = current.value;
        current.value = value;
        this.size++;
        return oldValue;
    }

    private int height(AVLNode node) {
        if (node == null) {
            return -1;
        }
        return node.height;
    }
    /**
     * Inner class to represent a node in the tree. Each node includes a data of
     * type E and an integer count. The class is protected so that subclasses of
     * BinarySearchTree can access it.
     */

    
    public class AVLNode extends BSTNode {
        private int height;

        /**
         * Create a new data node.
         *
         * @param key
         *            key with which the specified value is to be associated
         * @param value
         *            data element to be stored at this node.
         * @param height
         *            height of this node.
         */
        @SuppressWarnings("unchecked")
        public AVLNode(K key, V value, int height) {
            super(key, value);
            this.children = (BinarySearchTree.BSTNode[]) Array.newInstance(BinarySearchTree.BSTNode.class, 2);
            this.height = height;
        }

        @SuppressWarnings("unchecked")
        private AVLNode getLeft() {
            return ((AVLNode) this.children[0]);
        }

        @SuppressWarnings("unchecked")
        private AVLNode getRight() {
            return ((AVLNode) this.children[1]);
        }
    }
}
