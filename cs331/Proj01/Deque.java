/**
 * Deque: A Java implementation of an double ended queue.
 * Reused from CSCI 264: Analysis of Algorithms
 *
 * @param <T> The type of the values in the deque.
 * @author Alvin Lin axl1439@rit.edu
 */
public class Deque<T> {

  private static final class Node<T> {

    public T value;
    public Node<T> left;
    public Node<T> right;

    public Node(T value, Node<T> left, Node<T> right) {
      this.value = value;
      this.left = left;
      this.right = right;
    }

    public Node(T value) {
      this(value, null, null);
    }

    public String toString() {
      return value.toString();
    }
  }

  private Node<T> head;
  private Node<T> tail;
  private int size;

  public Deque() {
    this.size = 0;
  }

  public void clear() {
    head = tail = null;
    size = 0;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public T popFirst() {
    if (head == null) {
      throw new RuntimeException("No Such Element");
    }
    T temp = head.value;
    if (head == tail) {
      head = tail = null;
      size = 0;
      return temp;
    }
    if (head.right != null) {
      head.right.left = null;
    }
    head = head.right;
    size--;
    return temp;
  }

  public T popLast() {
    if (tail == null) {
      throw new RuntimeException("No Such Element");
    }
    T temp = tail.value;
    if (head == tail) {
      head = tail = null;
      size = 0;
      return temp;
    }
    if (tail.left != null) {
      tail.left.right = null;
    }
    tail = tail.left;
    size--;
    return temp;
  }

  public void pushFirst(T t) {
    Node<T> newHead = new Node<>(t, null, head);
    if (head == null && tail == null) {
      tail = newHead;
    } else {
      head.left = newHead;
    }
    head = newHead;
    size++;
  }

  public void pushLast(T t) {
    Node<T> newTail = new Node<>(t, tail, null);
    if (tail == null && head == null) {
      head = newTail;
    } else {
      tail.right = newTail;
    }
    tail = newTail;
    size++;
  }

  public int size() {
    return size;
  }

  public String toString() {
    StringBuilder result = new StringBuilder("[ ");
    Node<T> temp = head;
    while (temp != null) {
      result.append(temp + ", ");
      temp = temp.right;
    }
    result.append("]");
    return result.toString();
  }
}
