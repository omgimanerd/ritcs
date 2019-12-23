/**
 * Deque: A Java implementation of a disjoint set data structure.
 *
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class DisjointSet {

  private int size;
  private int[] sizes;
  private int[] bosses;
  private int[][] sets;

  /**
   * Constructor for a DisjointSet object. We give it a size to initialize
   * with. Each of the items starts off in its own set and is identified
   * simply by its index.
   * @param size The number of sets in the DisjointSet.
   */
  public DisjointSet(int size) {
    this.size = size;
    this.sizes = new int[size];
    this.bosses = new int[size];
    this.sets = new int[size][];
    for (int i = 0; i < size; ++i) {
      this.bosses[i] = i;
      this.sets[i] = new int[] { i };
    }
  }

  /**
   * The boss of a disjoint set is the element that represents it. At first,
   * each element is its own boss, but as sets get unioned with one another,
   * an element may be represented by another element that is part of its set.
   * It is guaranteed that two items in the same set have the same boss.
   * @param element The element to find the boss of
   * @return The boss of the given element
   */
  public int getBoss(int element) {
    if (element >= size || element < 0) {
      throw new ArrayIndexOutOfBoundsException(
          "Cannot get the boss of " + element);
    }
    return bosses[element];
  }

  /**
   * Unions two sets in the disjoint sets. This function is given two
   * elements that may or may not be part of the same set. If they are
   * already part of the same set, then nothing is done. If not, then we
   * get the bosses of the two sets that the elements are part of and set the
   * bosses of the smaller set to the bosses of the larger set, thus ensuring
   * that all elements in either set having the same boss. Functionally, the
   * two sets have been unioned.
   * @param element1 An element in the first set to union
   * @param element2 An element in the second set to union
   */
  public void union(int element1, int element2) {
    if (element1 >= size || element2 >= size || element1 < 0 || element2 < 0) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Cannot union %d and %d", element1, element2));
    }
    int boss1 = bosses[element1];
    int boss2 = bosses[element2];
    if (boss1 == boss2) {
      return;
    }
    if (boss1 < boss2) {
      int tmp = boss1;
      boss1 = boss2;
      boss2 = tmp;
    }
    int[] set1 = sets[boss1];
    int[] set2 = sets[boss2];
    sets[boss1] = new int[set1.length + set2.length];
    System.arraycopy(set1, 0, sets[boss1], 0, set1.length);
    System.arraycopy(set2, 0, sets[boss1], set1.length, set2.length);
    sizes[boss1] += sizes[boss2];
    for (int element : set2) {
      bosses[element] = boss1;
    }
  }
}
