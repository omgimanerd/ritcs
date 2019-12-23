/**
 * Deque: A Java implementation of an double ended queue.
 *
 * @param <T> The type of the values in the deque.
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class Deque<T> implements Collection<T> {

  private Node<T> head;
  private Node<T> tail;
  private int size;

  public Deque() {
    size = 0;
  }

  @Override
  public boolean add(T element) {
    pushLast(element);
    return true;
  }

  @Override
  public void clear() {
    head = tail = null;
    size = 0;
  }

  @Override
  public boolean contains(T o) {
    Node<T> temp = head;
    while (temp != null) {
      if (temp.value.equals(o)) {
        return true;
      }
      temp = temp.right;
    }
    return false;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public T peek() {
    return peekFirst();
  }

  public T peekFirst() {
    return head.value;
  }

  public T peekLast() {
    return tail.value;
  }

  public T pop() {
    return popFirst();
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

  public void push(T t) {
    pushFirst(t);
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

  @Override
  public boolean remove(T o) {
    return false;
  }

  @Override
  public int size() {
    return size;
  }

  /**
   * @inheritDoc
   */
  public Object[] toArray() {
    throw new RuntimeException("Not Implemented");
  }

  /**
   * Returns a String representation of this Deque
   *
   * @return A string representation of this Deque
   */
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
