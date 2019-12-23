import java.util.Scanner;

/**
 * FindMaxDiffPairs: Given a list of numbers and its size (on stdin), compute
 * the number that can be most often obtained by subtracting a unique pair of
 * elements from the list.
 *
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class FindMaxDiffPairs {

  /**
   * Find the maximum number of difference pairs in the input set.
   *
   * @param data The list of numbers in which to find pairs
   * @return The number of pairs in the input array which have the same
   * difference, guaranteed to be the highest number of such pairs
   * @time O(n²*2log(n))
   */
  public static long findMaxDiffPairs(long[] data) {
    // Initialize variables
    AVLTree<FrequencyTuple> treeData = new AVLTree<>(
        FrequencyTuple.comparator());
    // Loop over all of the input data
    for (int i = 0; i < data.length; i++) {
      // Loop over all of the input data after i
      for (int j = i + 1; j < data.length; j++) {
        // Create a FrequencyTuple with the absolute value of the difference
        // between the element at i and the element at j
        FrequencyTuple absDifference =
            new FrequencyTuple(Math.abs(data[i] - data[j]), 1);
        // Search the tree for that tuple
        AVLTree.AVLNode<FrequencyTuple> differenceNode =
            treeData.find(absDifference);
        // If the tree already contains this value,
        if (differenceNode != null) {
          // Increment the frequency counter
          differenceNode.value.frequency++;
          // Otherwise,
        } else {
          // Insert the tuple into the tree.
          treeData.add(absDifference);
        }
      }
    }

    return findMaxFrequencyInTree(treeData).value;
  }

  /**
   * Find the tuple with the maximum frequency value in the AVLTree provided as
   * input.
   *
   * @param tree The tree through which to search
   * @return The FrequencyTuple with the greatest frequency attribute in the
   * tree
   * @time O(n)
   */
  public static FrequencyTuple findMaxFrequencyInTree(
      AVLTree<FrequencyTuple> tree) {
    AVLTree.AVLNode<FrequencyTuple> root = tree.root;
    FrequencyTuple max = new FrequencyTuple(0, 0);

    Deque<AVLTree.AVLNode<FrequencyTuple>> stack = new Deque<>();
    AVLTree.AVLNode<FrequencyTuple> tmp;

    stack.push(root);

    while (!stack.isEmpty()) {
      tmp = stack.pop();
      if (tmp != null && tmp.value.frequency > max.frequency) {
        max = tmp.value;
      }
      if (tmp.getLeft() != null) {
        stack.push(tmp.getLeft());
      }
      if (tmp.getRight() != null) {
        stack.push(tmp.getRight());
      }
    }
    return max;
  }

  /**
   * The main function for FindMaxDiffPairs, which reads the input from stdin.
   *
   * @param args The command-line arguments to this program
   * @time O(n²*2log(n) + n)
   */
  public static void main(String[] args) {
    int n;
    long[] data;
    try (Scanner in = new Scanner(System.in)) {
      n = in.nextInt();
      data = new long[n];
      for (int i = 0; i < n; ++i) {
        data[i] = in.nextLong();
      }
    }
    System.out.println(findMaxDiffPairs(data));
  }
}
