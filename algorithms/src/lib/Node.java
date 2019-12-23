/**
 * Node: A Java implementation of an Node which can be used for linked lists,
 * trees, etc.
 *
 * @param <T> The type of the values in the Node.
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class Node<T> {

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

  public Node<T> getLeft() {
    return left;
  }

  public void setLeft(Node<T> left) {
    this.left = left;
  }

  public Node<T> getRight() {
    return right;
  }

  public void setRight(Node<T> right) {
    this.right = right;
  }

  public String toString() {
    return value.toString();
  }
}
