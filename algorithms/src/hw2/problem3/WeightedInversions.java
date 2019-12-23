import java.util.Scanner;

/**
 * WeightedInversions: Given a series of numbers, calculate the weight of all of
 * the inversions in the series. That is, every time some element of the series
 * after k has a value less than k, increase the weighted inversions counter by
 * the the difference between k's index and the other element's index.
 *
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class WeightedInversions {

  /**
   * InversionsCounterMergeSort: This class contains the business logic of the
   * inversion counter stuff.
   */
  private static class InversionsCounterMergeSort {

    // What data am I sorting?
    private ArrayList<KVTuple<Long, Long>> data;
    // What is the total weight of the inversions so far
    private long weightedInversionCount;

    /**
     * Create a new InversionsCounterMergeSort
     *
     * @param data The data to be sorted
     */
    public InversionsCounterMergeSort(ArrayList<KVTuple<Long, Long>> data) {
      this.data = data;
      this.weightedInversionCount = 0;
    }

    /**
     * The merge part of the merge sort. This is where all of the inversion
     * counting happens.
     *
     * @param left The left half of the data to merge
     * @param right The right half of the data to merge
     * @return The left and right halves of the data, merged and in order
     * @post this.weightedInversionCount has been incremented by the number of
     * weighted inversions encountered in this merge
     * @time O(3n/2)
     */
    private ArrayList<KVTuple<Long, Long>> merge(
        ArrayList<KVTuple<Long, Long>> left,
        ArrayList<KVTuple<Long, Long>> right) {
      // How long the array must be in order to hold both the left and right
      // halves of the data
      int resultLength = left.size() + right.size();
      // Create the array to merge into
      ArrayList<KVTuple<Long, Long>> result = new ArrayList<>(resultLength);
      // Initialize indices
      int leftIndex = 0;
      int rightIndex = 0;

      // Pre-compute the sum of the indices in the left half of the array
      long leftSum = 0;
      for (int i = 0; i < left.size(); i++) { // O(n/2)
        leftSum += left.get(i).getValue();
      }

      // Loop until the result array has been filled up
      while (result.size() < resultLength) { // O(n)
        // If everything in the left half of the array has already been
        // inserted, put in the next element from the right half of the array
        if (leftIndex == left.size()) {
          result.add(right.get(rightIndex++));
          // If everything in the right half of the array has already been
          // inserted, put in the next element from the left half of the array
        } else if (rightIndex == right.size()) {
          result.add(left.get(leftIndex++));
          // If the key in the left half is greater than the key in the right
          // half, then there's an inversion!
        } else if (left.get(leftIndex).getKey().compareTo(
            right.get(rightIndex).getKey()) > 0) {
          // Right, this the complicated bit.
          // When we're calculating the weight of the inversion, we have to
          // take into account the fact that an inverted element on the right
          // counts as an inversion with every subsequent element on the left.
          // The formula for this is:
          //    r = right original index
          //    aₓ = the xth index on the left
          //    (r - aₓ₊₁) + (r - aₓ₊₂) + ⋯ + (r - aₙ)
          // This sum can be expressed as
          //    Σ(n)(r-aₙ),
          // which is the same as
          //    n*r - Σ(n)(aₙ)
          // In terms of our variables, Σ(n)(aₙ) is our precomputed left sum,
          // n is the number of elements on the left side, and r is the right
          // element's value, yielding:
          //    ((leftLen - leftIndex) * rightValue) - leftSum
          // where:
          //    n = (leftLen - leftIndex)
          //    r = rightValue
          //    Σ(n)(aₙ) = leftSum
          weightedInversionCount +=
              ((left.size() - leftIndex) * right.get(rightIndex).getValue()) -
                  leftSum;
          result.add(right.get(rightIndex++));
        } else {
          // Now, the complicated math above only works if leftSum is
          // accurate. After we've merged an element into the result array,
          // it shouldn't count toward the sum. Thus, after inserting it into
          // the result array, we subtract its value from the precomputed sum
          // so that our computation is accurate.
          result.add(left.get(leftIndex));
          leftSum -= left.get(leftIndex++).getValue();
        }
      }
      return result;
    }

    /**
     * Calculate the total weight of all inversions in the data
     *
     * @return The total weight of all inversions in the data
     */
    public long calculateWeightedInversions() {
      data = mergeSortHelper(data);
      return weightedInversionCount;
    }

    /**
     * Do a merge sort on <pre>data</pre>.
     *
     * @param data The data to sort
     * @return <pre>data</pre>, sorted.
     * @post <pre>this.weightedInversionsCount</pre> is set to the number of
     * weighted inversions in <pre>data</pre>
     * @time O(n*log(n))
     */
    public ArrayList<KVTuple<Long, Long>> mergeSortHelper(
        ArrayList<KVTuple<Long, Long>> data) {
      // Base case
      if (data.size() == 1) {
        return data;
      }
      int middle = data.size() / 2;
      // Create the two arrays to hold each half of the list
      ArrayList<KVTuple<Long, Long>> a = new ArrayList<>(middle);
      ArrayList<KVTuple<Long, Long>> b = new ArrayList<>(data.size() -
          middle);
      // Copy the data into those arrays
      ArrayList.copy(data, 0, a, 0, middle); // O(n/2)
      ArrayList.copy(data, middle, b, 0, data.size() - middle);
      // ^ O(n/2)
      // Do the magic sorting thing
      return merge(mergeSortHelper(a), mergeSortHelper(b)); // O(n*log(n))
    }
  }

  /**
   * The main function for WeightedInversions. Reads data from stdin and
   * delegates actions appropriately.
   *
   * @param args The command line arguments to this program
   * @time O(n*log(n) + n)
   */
  public static void main(String[] args) {
    int n;
    ArrayList<KVTuple<Long, Long>> data;
    try (Scanner in = new Scanner(System.in)) {
      n = in.nextInt();
      data = new ArrayList<>(n);
      for (int i = 0; i < n; ++i) {
        data.add(new KVTuple<>(in.nextLong(), (long) i));
      }
    }
    InversionsCounterMergeSort m = new InversionsCounterMergeSort(data);
    System.out.println(m.calculateWeightedInversions());
  }
}
