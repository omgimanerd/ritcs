import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArrayListTest {

  private ArrayList<Integer> testArrayList;

  @Before
  public void setup() {
    testArrayList = new ArrayList<>();
  }

  @After
  public void tearDown() {
    testArrayList = null;
  }

  @Test
  public void testAdd() {
    testArrayList.add(1);
    assertEquals(1, testArrayList.size());
    testArrayList.add(10);
    assertEquals(2, testArrayList.size());
    assertEquals(10, (int) testArrayList.get(1));
    for (int i = 0; i < 1000; ++i) {
      testArrayList.add(i);
    }
    assertEquals(1002, testArrayList.size());
  }

  @Test
  public void testAddAsInsert() {
    for (int i = 0; i < 5; ++i) {
      testArrayList.add(i);
    }
    testArrayList.add(4, 10);
    assertEquals(10, (int) testArrayList.get(4));
    testArrayList.add(0, 20);
    assertEquals(20, (int) testArrayList.get(0));
  }

  @Test
  public void testClear() {
    testArrayList.add(1);
    testArrayList.clear();
    assertEquals(0, testArrayList.size());
    for (int i = 0; i < 100; ++i) {
      testArrayList.add(i);
    }
    testArrayList.clear();
    assertEquals(0, testArrayList.size());
  }

  @Test
  public void testContains() {
    assertFalse(testArrayList.contains(1));
    testArrayList.add(1);
    assertTrue(testArrayList.contains(1));
    for (int i = 0; i < 20; ++i) {
      testArrayList.add(i);
    }
    for (int i = 0; i < 20; ++i) {
      assertTrue(testArrayList.contains(i));
    }
  }

  @Test
  public void testCopy() {
    testArrayList.add(1);
    testArrayList.add(2);
    testArrayList.add(10);
    ArrayList<Integer> a = new ArrayList<>();
    ArrayList.copy(testArrayList, 0, a, 0, testArrayList.size());
    assertEquals(testArrayList, a);
  }

  @Test
  public void testEquals() {
    ArrayList<Integer> a = new ArrayList<>();
    ArrayList<Integer> b = new ArrayList<>();
    assertTrue(a.equals(b));
    assertTrue(b.equals(a));
    a.add(1);
    assertFalse(a.equals(b));
    assertFalse(b.equals(a));
    b.add(1);
    assertTrue(a.equals(b));
    assertTrue(b.equals(a));
  }

  @Test
  public void testGet() {
    for (int i = 0; i < 10; ++i) {
      testArrayList.add(i);
    }
    for (int i = 0; i < 10; ++i) {
      assertEquals(i, (int) testArrayList.get(i));
    }
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testInvalidAdd() {
    testArrayList.add(10, 1);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testInvalidGet() {
    testArrayList.get(3);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testInvalidRemove() {
    testArrayList.remove(1);
  }

  @Test
  public void testIsEmpty() {
    assertTrue(testArrayList.isEmpty());
    testArrayList.add(1);
    assertFalse(testArrayList.isEmpty());
  }

  @Test
  public void testRemove() {
    testArrayList.add(10);
    assertEquals(10, (int) testArrayList.remove(0));

    assertFalse(testArrayList.remove((Integer) 1));
    testArrayList.add(1);
    assertTrue(testArrayList.remove((Integer) 1));
  }

  @Test
  public void testSet() {
    testArrayList.add(1);
    testArrayList.set(0, 10);
    assertEquals(1, testArrayList.size());
    assertEquals(10, (int) testArrayList.get(0));
    testArrayList.add(3);
    testArrayList.set(1, 1);
    assertEquals(2, testArrayList.size());
    assertEquals(1, (int) testArrayList.get(1));
  }

  @Test
  public void testSize() {
    assertEquals(0, testArrayList.size());
    for (int i = 1; i < 10; ++i) {
      testArrayList.add(i);
      assertEquals(i, testArrayList.size());
    }
  }

  @Test
  public void testToString() {
    testArrayList.add(1);
    testArrayList.add(50);
    assertEquals("[ 1 50 ]", testArrayList.toString());
    testArrayList.add(67);
    assertEquals("[ 1 50 67 ]", testArrayList.toString());
  }
}
