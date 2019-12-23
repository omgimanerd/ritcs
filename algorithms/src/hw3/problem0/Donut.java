import java.util.Scanner;

/**
 * Donut: Where should we build a donut shop to maximize profits, given the
 * typical locations of all of our city's police cars? *something something
 * stereotypes something something*
 *
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class Donut {

  public static long getSumManhattanDistance(int[][] values, int x, int y) {
    long sum = 0;
    for (int[] value : values) {
      sum += Math.abs(value[0] - x) + Math.abs(value[1] - y);
    }
    return sum;
  }

  public static int heapKthSmallest(int[] values, int k) {
    Heap<Integer> heap = new Heap<>(Heap.Type.MIN, (i1, i2) -> i1 - i2);
    for (int value : values) {
      heap.add(value);
    }
    for (int i = 0; i < k - 1; ++i) {
      heap.pop();
    }
    return heap.pop();
  }

  /**
   * Use a heap to find the median of a list of values. Even-length sets are
   * resolved by taking the mean of the two middle values.
   *
   * @param values The list of values to examine
   * @param start Where to begin examining them
   * @param end Where to end examining them
   * @return The median value of the list.
   */
  public static float heapMedian(int[] values, int start, int end) {
    Heap<Integer> heap = new Heap<>(Heap.Type.MIN, (i1, i2) -> i1 - i2);
    for (int i = start; i < end; ++i) {
      heap.add(values[i]);
    }
    // How many items are we finding the median of?
    int n = end - start;
    // Temporary storage for the median value
    int lowMedian = 0;
    // Pull half of the elements out of the heap, putting each one into
    // lowMedian
    for (int i = 0; i < n / 2; ++i) {
      lowMedian = heap.pop();
    }
    // If the number of elements is odd,
    if (n % 2 == 1) {
      // just return the next largest item in the heap
      return heap.pop();
    } else {
      // Otherwise, resolve a tie with the mean.
      return (float) (lowMedian + heap.pop()) / 2;
    }
  }

  public static int linearKthSmallest(int[] values, int k) {
    if (values.length <= 5) {
      return heapKthSmallest(values, k);
    }

    // Get the median of every 5th element
    int[] medians = new int[(values.length / 5) + 1];
    for (int i = 0, j = 0; i < values.length; i += 5, ++j) {
      medians[j] = (int) heapMedian(values, i, Math.min(i + 5, values.length));
    }

    // Recursively invoke this to get the median of the medians.
    int medianB = linearKthSmallest(medians, (medians.length / 2) + 1);

    // Partition the array based on the median.
    int[] partition = new int[values.length];
    int left = 0, right = values.length - 1;
    for (int i = 0; i < values.length; ++i) {
      if (values[i] < medianB) {
        partition[left++] = values[i];
      } else if (values[i] > medianB) {
        partition[right--] = values[i];
      }
    }
    for (int i = left; i < right + 1; ++i) {
      partition[i] = medianB;
    }

    // Pick the pivot median closest to n / 2.
    int pivotIndex = left;
    if (pivotIndex != right && right < values.length / 2) {
      pivotIndex = right;
    }
    if (k < pivotIndex) {
      int[] slice = new int[pivotIndex];
      System.arraycopy(partition, 0, slice, 0, pivotIndex);
      return linearKthSmallest(slice, k);
    } else if (k > pivotIndex) {
      int[] slice = new int[partition.length - pivotIndex - 1];
      System.arraycopy(partition, pivotIndex + 1, slice, 0,
          partition.length - pivotIndex - 1);
      return linearKthSmallest(slice, k - pivotIndex);
    }
    return medianB;
  }

  public static float linearMedian(int[] values) {
    int k = values.length / 2 - 1;
    int l = k + 1;
    if (values.length % 2 == 1) {
      return linearKthSmallest(values, l);
    }
    return (float) (linearKthSmallest(values, k) + linearKthSmallest(values, l))
        / 2;
  }

  /**
   * The main function for Donut. Accepts input from stdin and writes the answer
   * to stdout.
   *
   * @param args Any command-line arguments to this function.
   */
  public static void main(String[] args) {
    int n;
    int[][] coordinates;
    int[][] coordinatesT;
    try (Scanner stdin = new Scanner(System.in)) {
      n = stdin.nextInt();
      coordinates = new int[n][];
      coordinatesT = new int[2][];
      coordinatesT[0] = new int[n];
      coordinatesT[1] = new int[n];
      for (int i = 0; i < n; ++i) {
        coordinates[i] = new int[2];
        coordinates[i][0] = stdin.nextInt();
        coordinates[i][1] = stdin.nextInt();
        coordinatesT[0][i] = coordinates[i][0];
        coordinatesT[1][i] = coordinates[i][1];
      }
    }
    int medianX = Math.round(linearMedian(coordinatesT[0]));
    int medianY = Math.round(linearMedian(coordinatesT[1]));
    long minDistance = getSumManhattanDistance(coordinates, medianX, medianY);
    int[][] neighborhood = new int[][]{
        {-1, -1}, {-1, 0}, {-1, 1},
        {0, -1}, {0, 1},
        {1, -1}, {1, 0}, {1, 1}
    };
    for (int[] offset : neighborhood) {
      long distance = getSumManhattanDistance(coordinates, medianX +
          offset[0], medianY + offset[1]);
      if (distance < minDistance) {
        minDistance = distance;
      }
    }
    System.out.println(minDistance);
  }
}
