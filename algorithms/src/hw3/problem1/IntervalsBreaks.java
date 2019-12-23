import java.util.Scanner;

/**
 * IntervalsBreaks: A scheduling algorithm that seeks to maximize the number of
 * classes that can be taken. It's a good thing we're in the honors program,
 * otherwise this algorithm could get expensive!
 *
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class IntervalsBreaks {

  /**
   * IndexedInterval: An interval that also has an index attached. We need this
   * to keep track of the intervals after we sort them.
   *
   * @param <T> What kind of number is inside the interval?
   */
  private static class IndexedInterval<T extends Comparable<T>> extends
      Interval<T> {

    public int index;

    /**
     * Create a new IndexedInterval
     *
     * @param index Where it was originally located in the array
     * @param start When it starts
     * @param end When it ends
     */
    public IndexedInterval(int index, T start, T end) {
      super(start, end);
      this.index = index;
    }

    /**
     * Make this IndexedInterval into a string
     *
     * @return A string representation of this interval
     */
    public String toString() {
      return "(" + this.getStart() + "," + this.getEnd() + ")(" + index + ") ";
    }
  }

  /**
   * IntervalStartComparator: Compare two IndexedIntervals by their start time
   */
  private static class IntervalStartComparator implements
      Comparator<IndexedInterval<Integer>> {

    /**
     * @inheritDoc
     */
    @Override
    public int compare(IndexedInterval<Integer> i1,
        IndexedInterval<Integer> i2) {
      return i1.getStart().compareTo(i2.getStart());
    }
  }

  /**
   * Heap sort the provided ArrayList of intervals by ascending start time
   *
   * @param data The ArrayList to sort
   */
  private static void heapSort(ArrayList<IndexedInterval<Integer>> data) {
    // Create the heap to sort with
    Heap<IndexedInterval<Integer>> heap =
        new Heap<>(Heap.Type.MIN, new IntervalStartComparator());
    // Add every element to the heap
    for (int i = 0; i < data.size(); i++) {
      heap.add(data.get(i));
    }
    // And pop them back off again
    for (int i = 0; i < data.size(); i++) {
      data.set(i, heap.pop());
    }
  }

  /**
   * Find the greatest number of non-overlapping intervals that satisfy the
   * travel time requirement.
   *
   * @param intervals The set of intervals to examine
   * @param travelTimes The nxn matrix of travel times between all of the
   * intervals
   * @return The size of the largest set of non-overlapping intervals which can
   * be traveled to in an appropriate amount of time
   */
  public static int findGreatestNumberOfIntervals(
      ArrayList<IndexedInterval<Integer>> intervals, int[][] travelTimes) {
    // Sort all the intervals by their end time and put them into a deque.
    // We store the interval's original indices so that we can get their
    // travel times to other classes.
    assert intervals.size() > 1;
    heapSort(intervals);

    // We will use a dynamic programming solution where solution[j] is the
    // maximum number of class one can take ending with class j.
    int[] solution = new int[intervals.size()];
    // By default, the the maximum number of classes one can take given only
    // one class is 1.
    solution[0] = 1;
    for (int i = 1; i < intervals.size(); ++i) {
      // Take the current interval.
      IndexedInterval<Integer> current = intervals.get(i);
      // By default, this solution starts at one, but if we find a sequence
      // of intervals before this which fit into the schedule allotting for
      // travel time, then we update the solution value.
      solution[i] = 1;
      for (int j = 0; j < i; ++j) {
        // Iterate through our solutions array.
        IndexedInterval<Integer> tmp = intervals.get(j);
        // Calculate the end time, allotting for travel, of each interval
        // we have previously checked.
        int tmpEndTime = getEndTimeWithTravel(tmp, current, travelTimes);
        // If that interval fits before our current class, allotting for
        // travel time, and its solution value is higher than the current,
        // the we update the current solution value.
        if (solution[j] >= solution[i] && tmpEndTime <= current.getStart()) {
          solution[i] = solution[j] + 1;
        }
      }
    }
    // Now we find the largest value in our solutions array.
    int result = solution[0];
    for (int i = 1; i < solution.length; ++i) {
      if (solution[i] > result) {
        result = solution[i];
      }
    }
    return result;
  }

  /**
   * Add the travel time from one interval to another to the first interval's
   * end time and return it
   *
   * @param startClass The starting interval
   * @param endClass The ending interval
   * @param travelTimes The matrix of travel times
   * @return startClass.end + travelTimes[startClass][endClass]
   */
  public static int getEndTimeWithTravel(
      IndexedInterval<Integer> startClass, IndexedInterval<Integer> endClass,
      int[][] travelTimes) {
    return startClass.getEnd() + travelTimes[startClass.index][endClass.index];
  }

  /**
   * The main function for IntervalsBreaks. Takes input from stdin and delegates
   * responsibilities appropriately.
   *
   * @param args The command-line arguments to this program.
   */
  public static void main(String[] args) {
    int n;
    IndexedInterval<Integer> indexedInterval;
    ArrayList<IndexedInterval<Integer>> indexedIntervals;
    int[][] travelTimes;
    // Try-with-resources to read the data from stdin
    try (Scanner stdin = new Scanner(System.in)) {
      n = stdin.nextInt();
      indexedIntervals = new ArrayList<>(n);
      travelTimes = new int[n][n];

      // List of intervals
      for (int i = 0; i < n; i++) {
        indexedInterval = new IndexedInterval<>(
            i, stdin.nextInt(), stdin.nextInt());
        indexedIntervals.add(indexedInterval);
      }

      // Matrix of travel times
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          travelTimes[i][j] = stdin.nextInt();
        }
      }
    }
    // Actually do the calculation
    System.out.println(
        findGreatestNumberOfIntervals(indexedIntervals, travelTimes));
  }
}
