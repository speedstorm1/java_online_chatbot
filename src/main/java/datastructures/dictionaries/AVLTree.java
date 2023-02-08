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

    @Override
    protected BinarySearchTree<K, V>.BSTNode find(K key, V value) {
        AVLNode prev = null;
        AVLNode current = (AVLNode) this.root;
        WorkList<AVLNode> path = new ArrayStack<>(); // the path of nodes to get to the point
        WorkList<Integer> decisions = new ArrayStack<>(); // the sequence of moves (left: -1 or right: 1)

        int direction = -1;

        while (current != null) {
            direction = Integer.signum(key.compareTo(current.key));

            // Node already exists in our tree, and we don't need to rotate
            if (direction == 0) {
                return current;
            }
            else {
                prev = current;
                if (direction == -1) {
                    current = current.getLeft();
                } else {
                    current = current.getRight();
                }
                decisions.add(direction);
                path.add(prev);
            }
        }

        // If value is not null, we need to actually add in the new value
        if (value != null) {
            this.size++;
            if (this.root == null) { // in this case, we are all good
                this.root = new AVLNode(key, null, 0);
                return this.root;
            }

            // initialize our new AVL Node - height will change later
            current = new AVLNode(key, null, 0);
            prev.children[(direction + 1) / 2] = current;

            // update all the ancestor heights by 1
            for (AVLNode node : path) {
                node.height++;
            }

            // now we check for imbalances
            int numMoves = path.size();

            for (int i = 0; i < numMoves - 1; i++) {
                prev = path.next();
                AVLNode grandparent = path.peek();
                // first check if the grandparent has any null right / left sides
                if (grandparent.getLeft() == null || grandparent.getRight() == null) {

                }
                if (Math.abs(grandparent.getLeft().height - grandparent.getRight().height) > 1) { // imbalance @ GP
                    // determine cases
                    int direction2 = decisions.next();
                    int direction1 = decisions.next();
                    if (direction1 == -1) { // first move was left
                        if (direction2 == -1) { // second move was left
                            // left-left
                            rotateLeft(grandparent);
                        } else {
                            // left-right
                            rotateRight(((AVLNode) root).getLeft());
                            rotateLeft((AVLNode) root);
                        }
                    } else { // first move was a right
                        if (direction2 == -1) { // second move was left
                            // right-left
                            rotateLeft(((AVLNode) root).getRight());
                            rotateRight((AVLNode) root);
                        } else {
                            // right-right
                            rotateRight(grandparent);
                        }
                    }


                }
                decisions.next();
            }


        }

        return current;
    }

    private void rotateRight(AVLNode node) {
        AVLNode temp = node.getRight();
        node.children[1] = temp.getLeft();
        temp.children[0] = node;
        node.height = Math.max(node.getRight().height, node.getLeft().height) + 1;
        temp.height = Math.max(temp.getRight().height, temp.getLeft().height) + 1;
        node = temp;
    }

    private void rotateLeft(AVLNode node) {
        AVLNode temp = node.getLeft();
        node.children[0] = temp.getRight();
        temp.children[1] = node;
        node.height = Math.max(node.getRight().height, node.getLeft().height) + 1;
        temp.height = Math.max(temp.getRight().height, temp.getLeft().height) + 1;
        node = temp;
    }

//    protected BSTNode find(K key, V value) {
//        BSTNode prev = null;
//        BSTNode current = this.root;
//
//        int child = -1;
//
//        while (current != null) {
//            int direction = Integer.signum(key.compareTo(current.key));
//
//            // We found the key!
//            if (direction == 0) {
//                return current;
//            }
//            else {
//                // direction + 1 = {0, 2} -> {0, 1}
//                child = Integer.signum(direction + 1);
//                prev = current;
//                current = current.children[child];
//            }
//        }
//
//        // If value is not null, we need to actually add in the new value
//        if (value != null) {
//            current = new BSTNode(key, null);
//            if (this.root == null) {
//                this.root = current;
//            }
//            else {
//                assert(child >= 0); // child should have been set in the loop
//                // above
//                prev.children[child] = current;
//            }
//            this.size++;
//        }
//
//        return current;
//    }

    @Override
    public V insert(K key, V value) {
        return super.insert(key, value);
    }

    /**
     * Inner class to represent a node in the tree. Each node includes a data of
     * type E and an integer count. The class is protected so that subclasses of
     * BinarySearchTree can access it.
     */
    public class AVLNode extends BSTNode {
        // public BinarySearchTree.BSTNode[] children; // The children of this node. - inherited
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
