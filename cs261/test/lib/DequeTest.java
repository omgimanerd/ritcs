import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link Deque}
 *
 * @author William Leuschner wel2138
 */
public class DequeTest {

  private Deque<Integer> d;

  /**
   * Set up the test for the Deque
   */
  @Before
  public void setup() {
    d = new Deque<>();
  }

  /**
   * Test to ensure that {@link Deque#Deque} doesn't fail?
   */
  @Test
  public void testConstructorShouldSucceedAlways() {
    Deque test = new Deque();
    assertNotNull("The constructor for Deque didn't create a deque. This "
        + "shouldn't happen, ever.", test);
  }

  @Test
  public void testAddShouldPutElementAtTailOfDeque() {
    d.add(1);
    d.add(2);
    assertEquals("2 was not the element at the tail of the deque, but it "
        + "should have been", 2, (int) d.popLast());
  }

  @Test
  public void testClearShouldNotBreakEmptyDeque() {
    d.clear();
    d.add(1);
    assertNotNull("Popping from the deque returned null after Deque#clear()",
        d.popFirst());
  }

  @Test
  public void testContainsShouldReturnFalseWhenDequeIsEmpty() {
    assertFalse("Deque#contains(T) did not return false when the item was not"
        + " in the deque", d.contains(1));
  }

  @Test
  public void testContainsShouldReturnFalseWhenDequeDoesNotContainItem() {
    d.add(1);
    assertFalse("Deque#contains(T) reported that the item was in the deque, "
        + "even though it was not", d.contains(2));
  }

  @Test
  public void testIsEmptyShouldReturnTrueWhenDequeIsEmpty() {
    assertTrue("Deque#isEmpty() returned false, even though the deque was "
        + "just created and not yet used", d.isEmpty());
  }

  @Test
  public void testIsEmptyShouldReturnTrueWhenAllElementsPopped() {
    d.add(1);
    d.add(2);
    d.pop();
    d.pop();
    assertTrue("Deque#isEmpty() returned false, even though all of the "
        + "elements were popped from it", d.isEmpty());
  }

  @Test
  public void testisEmptyShouldReturnFalseWhenThereAreThingsInTheDeque() {
    d.add(1);
    assertFalse("Deque#isEmpty() returned true, even though there weas an "
        + "item in the deque", d.isEmpty());
  }

  @Test
  public void testPeekShouldShowFirstElementOfDeque() {
    d.add(1);
    d.add(2);
    assertEquals("Deque#peek() did not return the item at the head of the "
        + "deque", 1, (int) d.peek());
  }

  @Test
  public void testPeekShouldNotRemoveElementFromDeque() {
    d.add(1);
    d.add(2);
    assertEquals("Deque#peek() did not return the item at the head of the "
        + "deque", 1, (int) d.peek());
    assertEquals("Deque#peek() did not return the same value when called "
        + "twice in sequence", 1, (int) d.peek());
  }

  @Test
  public void testPeekFirstShouldShowFirstElementOfDeque() {
    d.add(1);
    d.add(2);
    assertEquals("Deque#peekFirst() did not return the item at the head of the "
        + "deque", 1, (int) d.peekFirst());
  }

  @Test
  public void testPeekFirstShouldNotRemoveElementFromDeque() {
    d.add(1);
    d.add(2);
    assertEquals("Deque#peekFirst() did not return the item at the head of the "
        + "deque", 1, (int) d.peekFirst());
    assertEquals("Deque#peekFirst() did not return the same value when called "
        + "twice in sequence", 1, (int) d.peekFirst());
  }

  @Test
  public void testPeekLastShouldShowLastElementOfDeque() {
    d.add(1);
    d.add(2);
    assertEquals("Deque#peekLast() did not return the item at the tail of the "
        + "deque", 2, (int) d.peekLast());
  }

  @Test
  public void testPeekLastShouldNotRemoveElementFromDeque() {
    d.add(1);
    d.add(2);
    assertEquals("Deque#peekLast() did not return the item at the tail of the "
        + "deque", 2, (int) d.peekLast());
    assertEquals("Deque#peekLast() did not return the same value when called "
        + "twice in sequence", 2, (int) d.peekLast());
  }

  @Test
  public void testPopShouldReturnFirstElementOfDeque() {
    d.add(1);
    d.add(2);
    assertEquals("Deque#pop() did not return the item at the head of the "
        + "deque", 1, (int) d.pop());
  }

  @Test
  public void testPopShouldRemoveItemFromDeque() {
    d.add(1);
    d.add(2);
    assertEquals("Deque#pop() did not return the item at the head of the "
        + "deque", 1, (int) d.pop());
    assertEquals("Deque#pop() did not remove the item from the deque", 2,
        (int) d.peek());
  }

  @Test
  public void testPopFirstShouldReturnFirstElementOfDeque() {
    d.add(1);
    d.add(2);
    assertEquals("Deque#popFirst() did not return the item at the head of the "
        + "deque", 1, (int) d.popFirst());
  }

  @Test
  public void testPopFirstShouldRemoveItemFromDeque() {
    d.add(1);
    d.add(2);
    assertEquals("Deque#popFirst() did not return the item at the head of the "
        + "deque", 1, (int) d.popFirst());
    assertEquals("Deque#popFirst() did not remove the item from the deque", 2,
        (int) d.peekFirst());
  }

  @Test
  public void testPopLastShouldReturnLastElementOfDeque() {
    d.add(1);
    d.add(2);
    assertEquals("Deque#popLast() did not return the item at the tail of the "
        + "deque", 2, (int) d.popLast());
  }

  @Test
  public void testPopLastShouldRemoveItemFromDeque() {
    d.add(1);
    d.add(2);
    assertEquals("Deque#popLast() did not return the item at the head of the "
        + "deque", 2, (int) d.popLast());
    assertEquals("Deque#popLast() did not remove the item from the deque", 1,
        (int) d.peekLast());
  }

  @Test
  public void testPushShouldPushItemToFrontOfDeque() {
    d.push(1);
    d.push(2);
    assertEquals("Deque#push(T) didn't push to the front of the queue", 2,
        (int) d.peekFirst());
  }

  @Test
  public void testPushFirstShouldPushItemToFrontOfDeque() {
    d.pushFirst(1);
    d.pushFirst(2);
    assertEquals("Deque#pushFirst(T) didn't push to the front of the queue", 2,
        (int) d.peekFirst());
  }

  @Test
  public void testPushLastShouldPushItemToEndOfDeque() {
    d.pushLast(1);
    d.pushLast(2);
    assertEquals("Deque#pushLast(T) didn't push to the front of the queue", 2,
        (int) d.peekLast());
  }

  @Test
  public void testToStringShouldProduceEmptyBracketsForEmptyDeque() {
    assertEquals("Deque#toString() did not produce \"[ ]\" for an empty deque",
        "[ ]", d.toString());
  }

  @Test
  public void testToStringShouldShowElementsInHeadToTailOrder() {
    d.add(1);
    d.add(2);
    d.add(3);
    assertEquals("Deque#toString() did not produce a string representation of"
        + " the deque with the elements in head-to-tail order",
        "[ 1, 2, 3, ]", d.toString());
  }
}
