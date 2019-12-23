import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestDonutComponents {

  @Test
  public void testHeapMedianEven() {
    int[] data = {5, 1, 4, 2};
    float answer = Donut.heapMedian(data, 0, 4);
    assertEquals(3, answer, 0.01);
  }

  @Test
  public void testHeapMedianOdd() {
    int[] data = {5, 1, 3, 4, 2};
    float answer = Donut.heapMedian(data, 0, 5);
    assertEquals(3, answer, 0.01);
  }

  @Test
  public void testKthSmallestThreeGroups() {
    int[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    assertEquals(8, Donut.linearKthSmallest(data, 8));
  }

  @Test
  public void testKthSmallestTrivial() {
    int[] data = {1, 2, 3};
    assertEquals(2, Donut.linearKthSmallest(data, 2));
  }
}
