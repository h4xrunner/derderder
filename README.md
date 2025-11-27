public class Lab10 {
    public static void main(String[] args) {

        // You may test your BST and AVL implementations here.
        // Official testing will be done using JUnit.

    }
}

// ---------------------------------------------
// Node
// ---------------------------------------------
class Node<E extends Comparable<E>> {
    E data;
    Node<E> left;
    Node<E> right;
    int height;

    Node(E data) {
        this.data = data;
        this.height = 1; // for AVL
    }
}

// ---------------------------------------------
// IBST
// ---------------------------------------------
interface IBST<E extends Comparable<E>> {
    void insert(E element);
    void remove(E element);
    boolean contains(E element);
    int size();
    boolean isEmpty();

    Node<E> findMin(Node<E> n);
}

// ---------------------------------------------
// IAVL
// ---------------------------------------------
interface IAVL<E extends Comparable<E>> extends IBST<E> {
    int height();
    int getBalanceFactor(Node<E> node);

    Node<E> balance(Node<E> node);

    Node<E> rotateLeft(Node<E> node);
    Node<E> rotateRight(Node<E> node);
    Node<E> rotateLeftRight(Node<E> node);
    Node<E> rotateRightLeft(Node<E> node);
}

// ---------------------------------------------
// BST Implementation (Unbalanced)
// ---------------------------------------------
class BST<E extends Comparable<E>> implements IBST<E> {

    protected Node<E> root;
    protected int size;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(E element) {
        return containsRec(root, element);
    }

    protected boolean containsRec(Node<E> n, E e) {
        // TODO: implement recursive search
        if (n == null) {
            return false;
        }
        int comp = e.compareTo(n.data);
        if (comp < 0) {
            return containsRec(n.left, e);
        } else if (comp > 0) {
            return containsRec(n.right, e);
        } else {
            return true;
        }
    }

    @Override
    public void insert(E element) {
        root = insertRec(root, element);
    }

    protected Node<E> insertRec(Node<E> n, E e) {
        // TODO: standard BST insert (no balancing)
        if (n == null) {
            size++;
            return new Node<>(e);
        }
        int comp = e.compareTo(n.data);
        if (comp < 0) {
            n.left = insertRec(n.left, e);
        } else if (comp > 0) {
            n.right = insertRec(n.right, e);
        }
        return n;
    }

    @Override
    public void remove(E element) {
        root = removeRec(root, element);
    }

    protected Node<E> removeRec(Node<E> n, E e) {
        // TODO: BST remove (3 cases)
        if (n == null) {
            return null;
        }

        int comp = e.compareTo(n.data);
        if (comp < 0) {
            n.left = removeRec(n.left, e);
        } else if (comp > 0) {
            n.right = removeRec(n.right, e);
        } else {
            if (n.left == null) {
                size--;
                return n.right;
            } else if (n.right == null) {
                size--;
                return n.left;
            }

            Node<E> temp = findMin(n.right);
            n.data = temp.data;
            n.right = removeRec(n.right, temp.data);
        }
        return n;
    }

    @Override
    public Node<E> findMin(Node<E> n) {
        // TODO: return leftmost node
        if (n == null) {
            return null;
        }
        while (n.left != null) {
            n = n.left;
        }
        return n;
    }
}

// ---------------------------------------------
// AVL Implementation (Balanced BST)
// ---------------------------------------------
class AVLTree<E extends Comparable<E>>
        extends BST<E>
        implements IAVL<E> {

    @Override
    protected Node<E> insertRec(Node<E> n, E e) {
        // TODO:
        // 1) perform BST insert
        // 2) update height
        // 3) return balance(n)
        if (n == null) {
            size++;
            return new Node<>(e);
        }

        int comp = e.compareTo(n.data);
        if (comp < 0) {
            n.left = insertRec(n.left, e);
        } else if (comp > 0) {
            n.right = insertRec(n.right, e);
        } else {
            return n;
        }

        updateHeight(n);
        return balance(n);
    }

    @Override
    protected Node<E> removeRec(Node<E> n, E e) {
        // TODO:
        // 1) perform BST remove
        // 2) update height
        // 3) return balance(n)
        if (n == null) {
            return null;
        }

        int comp = e.compareTo(n.data);
        if (comp < 0) {
            n.left = removeRec(n.left, e);
        } else if (comp > 0) {
            n.right = removeRec(n.right, e);
        } else {
            if (n.left == null) {
                size--;
                return n.right;
            } else if (n.right == null) {
                size--;
                return n.left;
            }

            Node<E> temp = findMin(n.right);
            n.data = temp.data;
            n.right = removeRec(n.right, temp.data);
        }
        
        if (n != null) {
            updateHeight(n);
            return balance(n);
        }
        return null;
    }

    // -------------------------------------
    // AVL Helpers
    // -------------------------------------

    private void updateHeight(Node<E> n) {
        // TODO: recompute this node's height based on its children
        if (n != null) {
            n.height = 1 + Math.max(height(n.left), height(n.right));
        }
    }


    @Override
    public int height() {
        // TODO: return height(root)
        return height(root);
    }

    private int height(Node<E> n) {
        // TODO: null -> 0, else n.height
        return (n == null) ? 0 : n.height;
    }

    @Override
    public int getBalanceFactor(Node<E> n) {
        // TODO: compare heights of left and right subtrees
        return (n == null) ? 0 : height(n.left) - height(n.right);
    }


    @Override
    public Node<E> balance(Node<E> n) {
        // TODO:
        // 1) compute how unbalanced this node is (balance factor)
        //
        // 2) If the left subtree is taller than the right:
        //      - look at the LEFT child:
        //        a) if the left child also leans to its LEFT,
        //           perform a single RIGHT rotation
        //              (the left child becomes the new root of this subtree)
        //        b) if the left child leans to its RIGHT,
        //           first rotate the left child LEFT,
        //           then rotate this node RIGHT
        //
        // 3) If the right subtree is taller than the left:
        //      - look at the RIGHT child:
        //        a) if the right child also leans to its RIGHT,
        //           perform a single LEFT rotation
        //              (the right child becomes the new root of this subtree)
        //        b) if the right child leans to its LEFT,
        //           first rotate the right child RIGHT,
        //           then rotate this node LEFT
        //
        // 4) return the new root of this subtree after rotations
        if (n == null) return null;

        int bf = getBalanceFactor(n);

        if (bf > 1) {
            if (getBalanceFactor(n.left) >= 0) {
                return rotateRight(n);
            } else {
                return rotateLeftRight(n);
            }
        }

        if (bf < -1) {
            if (getBalanceFactor(n.right) <= 0) {
                return rotateLeft(n);
            } else {
                return rotateRightLeft(n);
            }
        }

        return n;
    }

    // -------------------------------------
    // Rotations
    // -------------------------------------

    @Override
    public Node<E> rotateLeft(Node<E> y) {
        // TODO:
        // Left rotation:
        // The right child becomes the new root of this subtree,
        // the original root becomes the left child of that node,
        // and the left subtree of the new root must be reattached
        // as the right subtree of the old root.
        //
        // Visually:
        //        y                     x
        //         \                   / \
        //          x      --->       y   R
        //         / \                 \
        //        L   R                L
        //
        // After rearranging, update heights of affected nodes
        // and return the new subtree root.
        Node<E> x = y.right;
        Node<E> t = x.left;

        x.left = y;
        y.right = t;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    @Override
    public Node<E> rotateRight(Node<E> x) {
        // TODO:
        // Right rotation:
        // The left child becomes the new root of this subtree,
        // the original root becomes the right child of that node,
        // and the right subtree of the new root must be reattached
        // as the left subtree of the old root.
        //
        // Visually:
        //        x                     y
        //       /                     / \
        //      y         --->        L   x
        //     / \                       /
        //    L   R                     R
        //
        // After rearranging, update heights of affected nodes
        // and return the new subtree root.
        Node<E> y = x.left;
        Node<E> t = y.right;

        y.right = x;
        x.left = t;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    @Override
    public Node<E> rotateLeftRight(Node<E> n) {
        // TODO: LR = rotateLeft(left) then rotateRight
        n.left = rotateLeft(n.left);
        return rotateRight(n);
    }

    @Override
    public Node<E> rotateRightLeft(Node<E> n) {
        // TODO: RL = rotateRight(right) then rotateLeft
        n.right = rotateRight(n.right);
        return rotateLeft(n);
    }
}
