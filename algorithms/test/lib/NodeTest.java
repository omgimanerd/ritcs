import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NodeTest {

  @Test
  public void testGetLeft() {
    Node<Integer> testLeft = new Node<>(0);
    Node<Integer> testNode = new Node<>(1, testLeft, null);
    assertEquals(testLeft, testNode.getLeft());
  }

  @Test
  public void testGetRight() {
    Node<Integer> testRight = new Node<>(0);
    Node<Integer> testNode = new Node<>(1, null, testRight);
    assertEquals(testRight, testNode.getRight());
  }

  @Test
  public void testSetLeft() {
    Node<Integer> testLeft = new Node<>(0);
    Node<Integer> testNode = new Node<>(1);
    assertEquals(null, testNode.getLeft());
    testNode.setLeft(testLeft);
    assertEquals(testLeft, testNode.getLeft());
  }

  @Test
  public void testSetRight() {
    Node<Integer> testRight = new Node<>(0);
    Node<Integer> testNode = new Node<>(1);
    assertEquals(null, testNode.getRight());
    testNode.setRight(testRight);
    assertEquals(testRight, testNode.getRight());
  }

  @Test
  public void testToString() {
    Node<String> testNode = new Node<>("TEST");
    assertEquals("TEST", testNode.toString());
  }

  @Test
  public void testValConstructor() {
    Node<Integer> testNode = new Node<>(999);
    assertEquals(new Integer(999), testNode.value);
  }

  @Test
  public void testValNodeNodeConstructor() {
    Node<Integer> testLeft = new Node<>(1);
    Node<Integer> testRight = new Node<>(3);
    Node<Integer> testNode = new Node<>(2, testLeft, testRight);
    assertEquals(new Integer(2), testNode.value);
    assertEquals(testLeft, testNode.left);
    assertEquals(testRight, testNode.right);
  }
}
