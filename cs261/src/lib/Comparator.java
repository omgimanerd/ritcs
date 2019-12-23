/**
 * This is basically a java.util.Comparator. We'll do it our own way with
 * blackjack and hookers.
 *
 * @param <T> The type of the object that this comparator will compare
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public interface Comparator<T> {

  /**
   * Compare the two objects passed as arguments. Returns 0 if equal, a negative
   * number if o1 &lt; o2, and a positive number if o1 &gt; o2
   *
   * @param o1 The first object to compare
   * @param o2 The second object to compare
   * @return 0 if o1=o2, &lt 0 if o1&lt;o2, and &gt; 0 if o1&gt;o2.
   */
  int compare(T o1, T o2);

}
