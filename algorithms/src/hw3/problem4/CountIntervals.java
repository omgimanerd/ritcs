import java.math.BigInteger;
import java.util.Scanner;

public class CountIntervals {

  /**
   * Compare two integer intervals by their end times
   */
  private static class IntervalEndComparator implements
      Comparator<Interval<Integer>> {

    /**
     * @inheritDoc
     */
    @Override
    public int compare(Interval<Integer> i1, Interval<Integer> i2) {
      return i1.getEnd().compareTo(i2.getEnd());
    }
  }

  /**
   * Compare two integer intervals by their start times
   */
  private static class IntervalStartComparator implements
      Comparator<Interval<Integer>> {

    /**
     * @inheritDoc
     */
    @Override
    public int compare(Interval<Integer> i1, Interval<Integer> i2) {
      return i1.getStart().compareTo(i2.getStart());
    }
  }

  public static final BigInteger TWO = new BigInteger("2");

  private static BigInteger countNonOverlappingIntervalSubsets
      (ArrayList<Interval<Integer>> intervals) {
    intervals = heapSort(intervals);
//    BigInteger[] appearanceCount = new BigInteger[intervals.size()];
//    for (int i = 0; i < intervals.size(); i++) {
//      appearanceCount[i] = BigInteger.ZERO;
//    }
    BigInteger solution = BigInteger.ONE;
    for (int i = 0; i < intervals.size(); ++i) {
      solution = solution.multiply(TWO);
      for (int j = 0; j < i; ++j) {
//        appearanceCount[j] = appearanceCount[j].add(BigInteger.ONE);
        if (i != j && intervals.get(i).overlaps(intervals.get(j))) {
//          solution = solution.subtract(appearanceCount[j]);
          solution = solution.subtract(BigInteger.ONE);
        } else {
        }
      }
    }
    return solution;
  }

  private static BigInteger countNonOverlappingIntervalSubsets2(
      ArrayList<Interval<Integer>> intervals) {
    BigInteger[] answer = new BigInteger[intervals.size()];
    for (int i = 0; i < intervals.size(); i++) {
      answer[i] = BigInteger.ZERO;
      for (int j = 0; j < intervals.size(); j++) {
        if (j != i && intervals.get(i).overlaps(intervals.get(j))) {
          answer[i] = answer[i].add(BigInteger.ONE);
        }
      }
    }
    BigInteger sum = BigInteger.ZERO;
    for (int i = 0; i < intervals.size(); i++) {
      sum = sum.add(answer[i]);
    }
    BigInteger twoUpN = TWO.pow(intervals.size());
    return twoUpN.subtract(sum);
  }

  /**
   * Heapsort the provided ArrayList of indices by descending finishing time.
   * Instead of returning an ArrayList however, this special method will return
   * the result inside a Deque.
   *
   * @param data The ArrayList to sort
   * @return <pre>data</pre>, sorted descending by finishing time
   */
  private static ArrayList<Interval<Integer>> heapSort(
      ArrayList<Interval<Integer>> data) {
    ArrayList<Interval<Integer>> result = new ArrayList<>();
    Heap<Interval<Integer>> heap =
        new Heap<>(Heap.Type.MIN, new IntervalStartComparator());
    for (int i = 0; i < data.size(); i++) {
      heap.add(data.get(i));
    }
    while (!heap.isEmpty()) {
      result.add(heap.pop());
    }
    return result;
  }

  public static void main(String[] args) {
    int n;
    ArrayList<Interval<Integer>> intervals;
    try (Scanner stdin = new Scanner(System.in)) {
      n = stdin.nextInt();
      intervals = new ArrayList<>();
      for (int i = 0; i < n; i++) {
        intervals.add(new Interval<>(stdin.nextInt(), stdin.nextInt()));
      }
    }
    System.out.println(countNonOverlappingIntervalSubsets(intervals));
  }
}
