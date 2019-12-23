import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FrequencyTupleTest {

  @Test
  public void testCompareToEqualDiffFreq() {
    FrequencyTuple testTuple = new FrequencyTuple(0, 1);
    FrequencyTuple equalTuple = new FrequencyTuple(0, 0);
    assertTrue(testTuple.compareTo(equalTuple) == 0);
  }

  @Test
  public void testCompareToEqualSameFreq() {
    FrequencyTuple testTuple = new FrequencyTuple(0, 0);
    FrequencyTuple equalTuple = new FrequencyTuple(0, 0);
    assertTrue(testTuple.compareTo(equalTuple) == 0);
  }

  @Test
  public void testCompareToGreater() {
    FrequencyTuple testTuple = new FrequencyTuple(0);
    FrequencyTuple greaterTuple = new FrequencyTuple(1);
    assertTrue(testTuple.compareTo(greaterTuple) < 0);
  }

  @Test
  public void testCompareToLess() {
    FrequencyTuple testTuple = new FrequencyTuple(1);
    FrequencyTuple lessTuple = new FrequencyTuple(0);
    assertTrue(testTuple.compareTo(lessTuple) > 0);
  }

  @Test
  public void testToString() {
    FrequencyTuple testTuple = new FrequencyTuple(100, -4);
    assertEquals("Value: 100 Frequency: -4", testTuple.toString());
  }

  @Test
  public void testValConstructor() {
    FrequencyTuple testTuple = new FrequencyTuple(1);
    assertEquals(1, testTuple.value);
  }

  @Test
  public void testValFreqConstructor() {
    FrequencyTuple testTuple = new FrequencyTuple(1, 2);
    assertEquals(1, testTuple.value);
    assertEquals(2, testTuple.frequency);
  }

}
