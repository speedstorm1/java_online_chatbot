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

    private AVLNode findHelper(K key, V value, AVLNode currentRoot) {
        if (currentRoot == null) {
            return new AVLNode(key, value, 0);
        }
        int compare = key.compareTo(currentRoot.key);
        if (compare < 0) {
            currentRoot.children[0] = findHelper(key, value, (AVLNode) currentRoot.children[0]);
        } else if (compare > 0) {
            currentRoot.children[1] = findHelper(key, value, (AVLNode) currentRoot.children[1]);
        }
        // else you found the node and just update the value
        return balance(currentRoot);
    }

    private AVLNode balance(AVLNode currentRoot) {
        if (currentRoot == null) {
            return null;
        }

        if (height(currentRoot.getLeft()) - height(currentRoot.getRight()) > 1) {
            if (height(currentRoot.getLeft().getLeft()) - height(currentRoot.getLeft().getRight()) >= 0) {
                //  left rotation (case 1)
            } else {
                // left right rotation (case 2)
            }
        } else if (height(currentRoot.getLeft()) - height(currentRoot.getRight()) < -1) {
            if (height(currentRoot.getRight().getRight()) - height(currentRoot.getRight().getLeft()) >= 0) {
                //  right rotation (case 4)
            } else {
                //  right left rotation (case 3)
            }
        }
        currentRoot.height = 0;
        return null;
    }

    @Override
    protected BinarySearchTree<K, V>.BSTNode find(K key, V value) {
        AVLNode prev = null;
        AVLNode current = (AVLNode) this.root;
        WorkList<AVLNode> path = new ArrayStack<>(); // the path of nodes to get to the point
        WorkList<AVLNode> pathDelete = new ArrayStack<>(); // the path of nodes to get to the point
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
                pathDelete.add(prev);
                //System.out.println(path);
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
            for (AVLNode node : pathDelete) {
                node.height = node.height + 1;
            }

            // now we check for imbalances
            int numMoves = path.size();
//            System.out.println(numMoves);

            for (int i = 0; i < numMoves - 1; i++) {
                prev = path.next();
                AVLNode grandparent = path.peek();
                int direction2 = decisions.next();
                int direction1 = decisions.peek();
                // first check if the grandparent has any null right / left sides
                if (grandparent.getLeft() == null || grandparent.getRight() == null) {
                    System.out.println("hit null");
                    System.out.println("d2: " + direction2 + " d1: " + direction1);
                    if (grandparent.getLeft() == null) {
                        if (direction2 == -1) {
                            rotateLeft(((AVLNode) grandparent).getRight());
                            grandparent = rotateRight((AVLNode) grandparent);
                        } else {
                            grandparent.rotateRight();
                        }
                    } else {
                        if (direction2 == -1) {
                            rotateLeft(grandparent);
                        } else {
                            grandparent = rotateRight(((AVLNode) grandparent).getLeft());
                            rotateLeft((AVLNode) grandparent);
                        }
                    }
                    break;
                }
                System.out.println("GP Height" + grandparent.height);
                if (Math.abs(grandparent.getLeft().height - grandparent.getRight().height) > 1) { // imbalance @ GP
                    // determine cases
                    System.out.println(i);

                    if (direction1 == -1) { // first move was left
                        if (direction2 == -1) { // second move was left
                            // left-left
                            rotateLeft(grandparent);
                        } else {
                            // left-right
                            grandparent = rotateRight(((AVLNode) grandparent).getLeft());
                            rotateLeft((AVLNode) grandparent);
                        }
                    } else { // first move was a right
                        if (direction2 == -1) { // second move was left
                            // right-left
                            rotateLeft(((AVLNode) grandparent).getRight());
                            grandparent = rotateRight((AVLNode) grandparent);
                        } else {
                            // right-right
                            grandparent = rotateRight(grandparent);
                        }
                    }
                    break;
                }
            //    decisions.next();
            }


        }
        //  verifyAVL((AVLNode)root);
//        System.out.println();
        return current;
    }



    private AVLNode rotateRight(AVLNode node) {
        AVLNode temp = node.getRight();
        node.children[1] = temp.getLeft();
        temp.children[0] = node;
//        node.height = Math.max(node.getRight().height, node.getLeft().height) + 1;
        node.height = getMaxHeight(node);
        temp.height = getMaxHeight(temp);
        return temp;
    }

    private int getMaxHeight(AVLNode node) {
        int rightHeight = -1;
        int leftHeight = -1;
        if (node.getRight() != null) {
            rightHeight = node.getRight().height;
        }
        if (node.getLeft() != null) {
            leftHeight = node.getLeft().height;
        }
        return (Math.max(leftHeight, rightHeight) + 1);
    }

    private void rotateLeft(AVLNode node) {
        AVLNode temp = node.getLeft();
        node.children[0] = temp.getRight();
        temp.children[1] = node;
        node.height = Math.max(node.getRight().height, node.getLeft().height) + 1;
        temp.height = Math.max(temp.getRight().height, temp.getLeft().height) + 1;
        node = temp;
    }



    @Override
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {

        }
        AVLNode current = (AVLNode) find(key, value);
        V oldValue = current.value;
        current.value = value;
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

        private void rotateRight() {
            AVLNode temp = this.getRight();
            this.children[1] = temp.getLeft();
            temp.children[0] = this;
//        node.height = Math.max(node.getRight().height, node.getLeft().height) + 1;
            this.height = getMaxHeight(this);
            temp.height = getMaxHeight(temp);
        }

        private void rotateLeft() {
            AVLNode temp = this.getLeft();
            this.children[0] = temp.getRight();
            temp.children[1] = this;
//        node.height = Math.max(node.getRight().height, node.getLeft().height) + 1;
            this.height = getMaxHeight(this);
            temp.height = getMaxHeight(temp);
        }
    }

    public boolean verifyAVL(AVLNode root) {
        /* TODO: Edit this with your code */
        // throw new IllegalStateException();
        K min = getMin(root);
        K max = getMax(root);
        System.out.println("new tree");
        inOrder(root);
//        System.out.println((int) (int) root.key);
        System.out.println("BST Verification: " + verifyBST(root, getMin(root), getMax(root)));
        System.out.println("Height Verification: " + verifyHeight(root));
        System.out.println("Balance Verification:" + verifyBalanced(root));
        return (verifyBST(root, getMin(root), getMax(root)) && verifyHeight(root) && verifyBalanced(root));
    }

    private K getMin(AVLNode root) {
        if (root == null) {
            return null;
        }
        K min = root.key;
        while (root != null) {
            if (min.compareTo(root.key) > 0) {
                min = root.key;
            }
            root = root.getLeft();
        }
        return min;
    }

    private K getMax(AVLNode root) {
        if (root == null) {
            return null;
        }
        K max = root.key;
        while (root != null) {
            if (max.compareTo(root.key) < 0) {
                max = root.key;
            }
            root = root.getRight();
        }
        return max;
    }


    private void inOrder(AVLNode root) {
        if (root != null) {
            inOrder(root.getLeft());
            System.out.println(root.key);
            inOrder(root.getRight());
        }
    }

    private boolean verifyHeight(AVLNode root) {
        if (root == null) {
            return true;
        }
        if (root.height == 0) {
            return (root.getLeft() == null && root.getRight() == null);
        }
        if (root.getLeft() == null) {
            return (root.getRight().height + 1 == root.height && verifyHeight(root.getRight()));
        }
        if (root.getRight() == null) {
            return (root.getLeft().height + 1 == root.height && verifyHeight(root.getLeft()));
        }
        return (Math.max(root.getLeft().height, root.getRight().height) + 1 == root.height && verifyHeight(root.getLeft())
                && verifyHeight(root.getRight()));
    }

    private boolean verifyBalanced(AVLNode root) {
        if (root == null) {
            return true;
        }
        if (root.getLeft() == null && root.getRight() == null) {
            return true;
        }
        if (root.getLeft() == null) {
            return (root.getRight().height == 0);
        }
        if (root.getRight() == null) {
            return (root.getLeft().height == 0);
        }
        return (Math.abs(root.getRight().height - root.getLeft().height) <= 1 &&
                verifyBalanced(root.getRight()) && verifyBalanced(root.getLeft()));
    }
    private boolean verifyBST(AVLNode root, K min, K max) {
        if (root == null) {
            return true;
        }
        if (root.key.compareTo(min) < 0) {
            return false;
        }
        if (root.key.compareTo(max) > 0) {
            return false;
        }
        return (verifyBST(root.getLeft(), min, root.key) && verifyBST(root.getRight(), root.key, max));
    }


}
