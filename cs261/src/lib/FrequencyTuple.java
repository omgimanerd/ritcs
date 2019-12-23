/**
 * FrequencyTuple: A Java implementation of an Tuple which is used to store
 * values and frequencies. It implements Comparable so it can be compared by the
 * stored value.
 *
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class FrequencyTuple implements Comparable<FrequencyTuple> {

  public long value;
  public long frequency;

  public FrequencyTuple(long value, long frequency) {
    this.value = value;
    this.frequency = frequency;
  }

  public FrequencyTuple(long value) {
    this(value, 0);
  }

  public static Comparator<FrequencyTuple> comparator() {
    return (o1, o2) -> o1.compareTo(o2);
  }

  @Override
  public int compareTo(FrequencyTuple other) {
    return (new Long(value)).compareTo(other.value);
  }

  public String toString() {
    return String.format("Value: %s Frequency: %s", value, frequency);
  }
}
