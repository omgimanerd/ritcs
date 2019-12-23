/**
 * AVLTree: A Java implementation of an AVL Tree which sort-of conforms to the
 * java.util.Set interface.
 *
 * @param <T> The type of the values in the tree.
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class AVLTree<T> implements Collection<T> {

  /**
   * A node in an AVLTree
   *
   * @param <T> The type of the values in the tree.
   */
  public static class AVLNode<T> extends Node<T> {

    // The height of the subtree rooted at this node
    public int height;

    /**
     * Constructor for an AVLNode.
     *
     * @param value The value to hold in this node
     * @param left A pointer to the left child
     * @param right A pointer to the right child
     */
    public AVLNode(T value, AVLNode<T> left, AVLNode<T> right) {
      super(value, left, right);
      this.height = 1;
    }

    public AVLNode(T value) {
      this(value, null, null);
    }

    public AVLNode<T> getLeft() {
      return (AVLNode<T>) super.getLeft();
    }

    public AVLNode<T> getRight() {
      return (AVLNode<T>) super.getRight();
    }

    /**
     * Recalculates the height of the subtree rooted at this node.
     */
    public void updateHeight() {
      AVLNode<T> leftNode = getLeft();
      AVLNode<T> rightNode = getRight();
      int leftHeight = leftNode == null ? 0 : leftNode.height;
      int rightHeight = rightNode == null ? 0 : rightNode.height;
      height = Math.max(leftHeight, rightHeight) + 1;
    }
  }

  public abstract static class TraversalAction<T> {

    public abstract void execute(T node);
  }

  // The root of this tree
  public AVLNode<T> root;
  // The number of nodes in this tree
  public int size;
  // The Comparator used to compare the values stored in the tree
  private Comparator<T> comparator;

  // No-args constructor
  public AVLTree(Comparator<T> comparator) {
    this.size = 0;
    this.comparator = comparator;
  }

  /**
   * Recursive helper function to add a value to a tree and perform the tree
   * balancing.
   *
   * @param node The root node of the tree or subtree to add a value to
   * @param value The value to add to the tree
   * @return The root of the tree to which the value has been added to
   */
  private AVLNode<T> add(AVLNode<T> node, T value) {
    if (node == null) {
      return new AVLNode<T>(value);
    }
    // Add the node first, but if it a duplicate then we stop.
    if (comparator.compare(node.value, value) > 0) {
      node.left = add(node.getLeft(), value);
    } else if (comparator.compare(node.value, value) < 0) {
      node.right = add(node.getRight(), value);
    } else {
      return node;
    }
    // Update the height of the node and attempt to rebalance the tree.
    node.updateHeight();
    int balance = getBalance(node);
    // Left Left (/) Case
    if (balance > 1 && comparator.compare(node.left.value, value) >= 0) {
      return rotateRight(node);
      // Right Right (\) Case
    } else if (balance < -1
        && comparator.compare(node.right.value, value) <= 0) {
      return rotateLeft(node);
      // Left Right (>) Case
    } else if (balance > 1 && comparator.compare(node.left.value, value) >= 0) {
      node.left = rotateLeft(node.getLeft());
      return rotateRight(node);
      // Right Left (<) Case
    } else if (balance < -1
        && comparator.compare(node.right.value, value) >= 0) {
      node.right = rotateRight(node.getRight());
      return rotateLeft(node);
    }
    return node;
  }

  private void inOrderTraverse(Node<T> n, TraversalAction<T> action) {
    if (n != null) {
      preOrderTraverse(n.left, action);
      action.execute(n.value);
      preOrderTraverse(n.right, action);
    }
  }

  private void postOrderTraverse(Node<T> n, TraversalAction<T> action) {
    if (n != null) {
      preOrderTraverse(n.left, action);
      action.execute(n.value);
      preOrderTraverse(n.right, action);
    }
  }

  private void preOrderTraverse(Node<T> n, TraversalAction<T> action) {
    if (n != null) {
      action.execute(n.value);
      preOrderTraverse(n.left, action);
      preOrderTraverse(n.right, action);
    }
  }

  /**
   * Rotate left on the given node to rebalance the tree.
   *
   * @param node The node to rotate left on
   * @return The new parent node
   * @pre 1 \ 2 / 3
   * @post 2 / \ 1   3
   */
  private AVLNode<T> rotateLeft(AVLNode<T> node) {
    AVLNode<T> rightChild = node.getRight();
    AVLNode<T> leftGrandchild = rightChild.getLeft();

    rightChild.left = node;
    node.right = leftGrandchild;

    node.updateHeight();
    rightChild.updateHeight();
    return rightChild;
  }

  /**
   * Rotate right on the given node.
   *
   * @param node The node to rotate right on
   * @pre 1 / 2 \ 3
   * @post 2 / \ 3   1
   */
  private AVLNode<T> rotateRight(AVLNode<T> node) {
    AVLNode<T> leftChild = node.getLeft();
    AVLNode<T> leftRightChild = leftChild.getRight();

    leftChild.right = node;
    node.left = leftRightChild;

    node.updateHeight();
    leftChild.updateHeight();
    return leftChild;
  }

  /**
   * Add a node to the tree.
   *
   * @param value The value to put inside that node
   */
  public boolean add(T value) {
    root = add(root, value);
    size++;
    return true;
  }

  /**
   * Empty the tree.
   */
  public void clear() {
    root = null;
    size = 0;
  }

  @Override
  public boolean contains(T o) {
    return find(o) == null;
  }

  /**
   * Searches for a node in the tree containing the specified value and returns
   * the node.
   *
   * @param value The value to search for in the tree
   * @return The node in the tree containing the specified value
   */
  public AVLNode<T> find(T value) {
    // Bail out if the tree is empty
    if (root == null) {
      return null;
    }
    // Get the root
    AVLNode<T> temp = root;
    // While the temp node isn't null,
    while (temp != null) {
      // Compare its value to the one we're looking for
      if (comparator.compare(temp.value, value) == 0) {
        // Return the node if we find it,
        return temp;
        // If we don't, venture down the appropriate branch to keep looking
      } else if (comparator.compare(temp.value, value) > 0) {
        temp = temp.getLeft();
      } else {
        temp = temp.getRight();
      }
    }
    // If all else fails, return null.
    return null;
  }

  /**
   * Get the balance of the specified node. (-1,1) is balanced-ish, anything
   * outside that range is not.
   *
   * @param node The node of which to get the balance value for
   */
  public int getBalance(AVLNode<T> node) {
    AVLNode<T> leftNode = node.getLeft();
    AVLNode<T> rightNode = node.getRight();
    int leftHeight = leftNode == null ? 0 : leftNode.height;
    int rightHeight = rightNode == null ? 0 : rightNode.height;
    return node == null ? 0 : leftHeight - rightHeight;
  }

  public void inOrderTraverse(TraversalAction<T> action) {
    inOrderTraverse(action);
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  public void postOrderTraverse(TraversalAction<T> action) {
    postOrderTraverse(action);
  }

  public void preOrderTraverse(TraversalAction<T> action) {
    preOrderTraverse(root, action);
  }

  @Override
  public boolean remove(T o) {
    throw new RuntimeException("Not Implemented");
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public Object[] toArray() {
    throw new RuntimeException("Not Implemented");
  }
}
