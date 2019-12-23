/**
 * Heap: A Java implementation of a heap.
 * Reused from CSCI 264: Analysis of Algorithms
 *
 * @param <T> The type of the values in the heap.
 * @author Alvin Lin axl1439@rit.edu
 */
public class Heap<T extends Comparable<T>> {

  public enum Type {
    MIN, MAX
  }

  private Object[] heap;
  private int size;
  private Type type;

  public Heap(Type type) {
    this.heap = new Object[10];
    this.size = 0;
    this.type = type;
  }

  private int getLeft(int n) {
    return 2 * n + 1;
  }

  private int getParent(int n) {
    return (n - 1) / 2;
  }

  private int getRight(int n) {
    return 2 * n + 2;
  }

  @SuppressWarnings("unchecked")
  private T heap(int index) {
    if (index > size) {
      return null;
    }
    return (T) heap[index];
  }

  private void heapifyDown(int current) {
    T currentObj = heap(current);
    int left = getLeft(current);
    if (left >= size) {
      return;
    }
    T leftObj = heap(left);
    // Is the left node greater than the root?
    boolean leftRootGreater = currentObj.compareTo(leftObj) > 0;
    int right = getRight(current);
    if (right > size) {
      // This is a special case where we just have a left node and no right
      // node. If our left node is greater/lesser than our root (depending on
      // whether or not this is a min/max heap) then we need to swap.
      if ((type == Type.MIN && leftRootGreater) ||
          (type == Type.MAX && !leftRootGreater)) {
        swap(current, left);
        return;
      }
    }
    T rightObj = heap(right);
    // Is the right node greater than the root?
    boolean rightRootGreater = currentObj.compareTo(rightObj) > 0;
    // Is the left node greater than the right node?
    boolean leftRightGreater = leftObj.compareTo(rightObj) > 0;
    // We care about the left node being greater than the root, right node
    // being greater than the root, and left node being greater than the right
    // if we are heapifying a min heap. If we are heapifying a max heap, then
    // we reverse these conditions because we want to know the analogous
    // relations, but in lesser-than form.
    if (type == Type.MAX) {
      leftRootGreater = !leftRootGreater;
      rightRootGreater = !rightRootGreater;
      leftRightGreater = !leftRightGreater;
    }
    // For a min heap, if the root is greater than both the left and right node,
    // then we swap with the lesser of the two. Because of the negation in the
    // previous block, this also takes care of the analogous condition in max
    // heaps.
    //
    // For a min heap, if the root is greater than the left node, then we swap
    // it with the left. If the root is greater than the right node, then we
    // swap it with the right node. Again, the analogous conditions are covered
    // by the previous block for max heaps.
    if (leftRootGreater && rightRootGreater) {
      swap(current, leftRightGreater ? right : left);
      heapifyDown(leftRightGreater ? right : left);
    } else if (leftRootGreater) {
      swap(current, left);
      heapifyDown(left);
    } else if (rightRootGreater) {
      swap(current, right);
      heapifyDown(right);
    }
  }

  private void resize() {
    Object[] resized = new Object[heap.length * 2];
    for (int i = 0; i <= size; ++i) {
      resized[i] = heap[i];
    }
    heap = resized;
  }

  private void swap(int a, int b) {
    Object tmp = heap[a];
    heap[a] = heap[b];
    heap[b] = tmp;
  }

  public void clear() {
    heap = new Object[10];
    size = 0;
  }

  public void heapifyUp(int current) {
    if (current == 0) {
      return;
    }
    int parent = getParent(current);
    T parentObj = heap(parent);
    T currentObj = heap(current);
    if (type == Type.MIN && parentObj.compareTo(currentObj) > 0) {
      swap(current, parent);
      heapifyUp(parent);
    } else if (type == Type.MAX && parentObj.compareTo(currentObj) < 0) {
      swap(current, parent);
      heapifyUp(parent);
    }
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public T peek() {
    if (size == 0) {
      throw new IndexOutOfBoundsException(
        "peek() was called on an empty heap!");
    }
    return heap(0);
  }

  public T pop() {
    if (size == 0) {
      throw new IndexOutOfBoundsException("pop() was called on an empty heap!");
    }
    T tmp = heap(0);
    heap[0] = heap[--size];
    heapifyDown(0);
    return tmp;
  }

  public void push(T value) {
    heap[size] = value;
    heapifyUp(size++);
    if (size == heap.length - 1) {
      resize();
    }
  }

  public int size() {
    return size;
  }

  public String toString() {
    StringBuilder result = new StringBuilder("[ ");
    for (int i = 0; i < size; ++i) {
      result.append(heap(i) + " ");
    }
    result.append("]");
    return result.toString();
  }
}
