import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HeapTest {

  private Heap<Integer> testMinHeap;
  private Heap<Integer> testMaxHeap;

  @Before
  public void setup() {
    testMinHeap = new Heap<>(Heap.Type.MIN, Integer::compareTo);
    testMaxHeap = new Heap<>(Heap.Type.MAX, Integer::compareTo);
  }

  @After
  public void tearDown() {
    testMinHeap = null;
    testMaxHeap = null;
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testHeapEmptyPop() {
    testMinHeap.pop();
  }

  @Test
  public void testHeapSort() {
    Random generator = new Random(1);
    for (int i = 0; i < 10000; ++i) {
      testMaxHeap.add(generator.nextInt());
    }
    int lastValue = testMaxHeap.pop();
    System.out.println(lastValue);
    for (int i = 9999; i > 0; --i) {
      int currentValue = testMaxHeap.pop();
      assertTrue(currentValue <= lastValue);
      lastValue = currentValue;
    }
  }

  @Test
  public void testMaxHeapClear() {
    assertEquals(0, testMaxHeap.size());
    testMaxHeap.add(1);
    testMaxHeap.add(2);
    assertEquals(2, testMaxHeap.size());
    testMaxHeap.clear();
    assertEquals(0, testMaxHeap.size());
  }

  @Test
  public void testMaxHeapInsertPop() {
    testMaxHeap.add(1);
    assertEquals(new Integer(1), testMaxHeap.pop());
    testMaxHeap.add(1);
    testMaxHeap.add(3);
    testMaxHeap.add(6565);
    testMaxHeap.add(444);
    testMaxHeap.add(2445);
    assertEquals(new Integer(6565), testMaxHeap.pop());
    assertEquals(new Integer(2445), testMaxHeap.pop());
    assertEquals(new Integer(444), testMaxHeap.pop());
    assertEquals(new Integer(3), testMaxHeap.pop());
    assertEquals(new Integer(1), testMaxHeap.pop());

    testMaxHeap.add(1);
    testMaxHeap.add(2);
    testMaxHeap.add(3);
    testMaxHeap.add(4);
    testMaxHeap.add(5);
    assertEquals(new Integer(5), testMaxHeap.pop());
    assertEquals(new Integer(4), testMaxHeap.pop());
    assertEquals(new Integer(3), testMaxHeap.pop());
    assertEquals(new Integer(2), testMaxHeap.pop());
    assertEquals(new Integer(1), testMaxHeap.pop());
  }

  @Test
  public void testMaxIsEmptyFalse() {
    testMaxHeap.add(0);
    assertFalse(
        "testMaxHeap reported itself as falsely empty",
        testMaxHeap.isEmpty()
    );
  }

  @Test
  public void testMaxIsEmptyTrue() {
    assertTrue(
        "testMaxHeap reported itself as falsely not empty",
        testMaxHeap.isEmpty()
    );
  }

  @Test
  public void testMaxLength() {
    testMaxHeap.add(0);
    assertEquals(1, testMaxHeap.size());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testMaxPeekEmpty() {
    testMaxHeap.peek();
  }

  @Test
  public void testMaxPeekNotEmpty() {
    testMaxHeap.add(5);
    assertEquals(new Integer(5), testMaxHeap.peek());
    assertEquals(1, testMaxHeap.size());
  }

  @Test
  public void testMinHeapClear() {
    assertEquals(0, testMinHeap.size());
    testMinHeap.add(1);
    testMinHeap.add(2);
    assertEquals(2, testMinHeap.size());
    testMinHeap.clear();
    assertEquals(0, testMinHeap.size());
  }

  @Test
  public void testMinHeapInsertPop() {
    testMinHeap.add(1123);
    assertEquals(new Integer(1123), testMinHeap.pop());
    testMinHeap.add(2);
    testMinHeap.add(5);
    testMinHeap.add(3);
    testMinHeap.add(1);
    testMinHeap.add(4);
    assertEquals(new Integer(1), testMinHeap.pop());
    assertEquals(new Integer(2), testMinHeap.pop());
    assertEquals(new Integer(3), testMinHeap.pop());
    assertEquals(new Integer(4), testMinHeap.pop());
    assertEquals(new Integer(5), testMinHeap.pop());

    testMinHeap.add(5);
    testMinHeap.add(4);
    testMinHeap.add(3);
    testMinHeap.add(2);
    testMinHeap.add(1);
    assertEquals(new Integer(1), testMinHeap.pop());
    assertEquals(new Integer(2), testMinHeap.pop());
    assertEquals(new Integer(3), testMinHeap.pop());
    assertEquals(new Integer(4), testMinHeap.pop());
    assertEquals(new Integer(5), testMinHeap.pop());
  }

  @Test
  public void testMinIsEmptyFalse() {
    testMinHeap.add(0);
    assertFalse(
        "testMinHeap reported itself as falsely empty",
        testMinHeap.isEmpty()
    );
  }

  @Test
  public void testMinIsEmptyTrue() {
    assertTrue(
        "testMinHeap reported itself as falsely not empty",
        testMinHeap.isEmpty()
    );
  }

  @Test
  public void testMinLength() {
    testMinHeap.add(0);
    assertEquals(1, testMinHeap.size());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testMinPeekEmpty() {
    testMinHeap.peek();
  }

  @Test
  public void testMinPeekNotEmpty() {
    testMinHeap.add(5);
    assertEquals(new Integer(5), testMinHeap.peek());
    assertEquals(1, testMinHeap.size());
  }
}
