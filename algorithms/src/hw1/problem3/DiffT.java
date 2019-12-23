import java.util.Scanner;

/**
 * DiffT: Given a number t, a list of numbers, and the size of that list, find
 * the number of unique pairs in the list whose difference is t.
 *
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class DiffT {

  /**
   * A naïve approach to solving the problem which brute-forces the solution in
   * O(n²) time.
   *
   * @param n The size of the data
   * @param t The difference to find
   * @param data The data through which to search
   * @return THe number of unique pairs which subtract to t
   */
  public static int diffPairs1(int n, int t, int[] data) {
    int count = 0;
    for (int i = 0; i < n; ++i) {
      for (int j = i; j < n; ++j) {
        if (Math.abs(data[i] - data[j]) == t) {
          ++count;
        }
      }
    }
    return count;
  }

  /**
   * Calculate the number of pairs with difference t in the input set data
   *
   * @param n How many elements are in data
   * @param t The difference to find
   * @param data The data in which to find it
   * @return The number of unique pairs whose difference is t
   * @time O(n*3log(n))
   */
  public static long diffPairs2(int n, int t, int[] data) {
    AVLTree<FrequencyTuple> avlt = new AVLTree<>(FrequencyTuple.comparator());
    long count = 0;
    for (int i = 0; i < n; ++i) { // O(n)
      // We create anonymous FrequencyTuple objects so we can search for
      // data[i] - t and data[i] + t. These anonymous FrequencyTuple objects are
      // never inserted into the tree and are only used for searching since
      // we consider FrequencyTuples equivalent if their values are equivalent
      // but not their frequencies.
      AVLTree.AVLNode<FrequencyTuple> t1 = avlt.find(
          new FrequencyTuple(data[i] + t, 0)); // O(log(n))
      AVLTree.AVLNode<FrequencyTuple> t2 = avlt.find(
          new FrequencyTuple(data[i] - t, 0)); // O(log(n))
      if (t1 != null) {
        // If we find a Node containing this matching value in the AVLTree,
        // then we increment our count by the Node's frequency because that
        // frequency represents the number of "diffpairs" to the current
        // data[i].
        count += t1.value.frequency;
      }
      // We have the t != 0 edge case here because if t is zero, then t1 and
      // t2 will point to the same node and we don't want to increment the
      // frequency twice.
      if (t2 != null && t != 0) {
        count += t2.value.frequency;
      }
      // After each loop iteration, we add the current Node to the tree. If
      // a Node of the current value is already in the tree, then we fetch it
      // and increment its frequency.
      FrequencyTuple current = new FrequencyTuple(data[i], 1);
      AVLTree.AVLNode<FrequencyTuple> currentNode = avlt.find(current);
      if (currentNode != null) {
        currentNode.value.frequency++;
      } else {
        avlt.add(current);
      }
    }
    return count;
  }

  /**
   * The main function for DiffT, which takes input from stdin and delegates
   * responsibility appropriately
   *
   * @param args The command-line arguments to this program
   */
  public static void main(String[] args) {
    int n, t;
    int[] data;
    try (Scanner in = new Scanner(System.in)) {
      n = in.nextInt();
      t = in.nextInt();
      data = new int[n];
      for (int i = 0; i < n; ++i) {
        data[i] = in.nextInt();
      }
    }
    System.out.println(diffPairs2(n, t, data));
  }
}
