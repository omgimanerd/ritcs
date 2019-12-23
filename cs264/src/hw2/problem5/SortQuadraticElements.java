import java.math.BigInteger;
import java.util.Scanner;

/**
 * SortQuadraticElements: Given a series of numbers that are at most n²-1, sort
 * them in linear time.
 *
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class SortQuadraticElements {

  /**
   * Converts a number from base 10 to base "base" and returns it as an array of
   * shorts where the 0th index of the array is the lowest place, the 1st index
   * is the base^2 place, etc.
   *
   * @param number The number to convert (in base 10)
   * @param base The base to convert to
   * @param digits The length of the resulting short array, if the number has
   * less digits than this quantity, the remaining spaces are zero padded.
   */
  private static short[] convertFromBase10(long number, int base, int digits) {
    Deque<Short> deque = new Deque<>();
    while (number != 0) {
      deque.push((short) (number % base));
      number /= base;
    }
    short[] result = new short[digits];
    int c = 0;
    while (!deque.isEmpty()) {
      result[c++] = deque.popLast();
    }
    return result;
  }

  /**
   * Given a number, calculate a base we can radix sort by and the number of
   * digits that the number will be represented with in that base.
   *
   * @param max The number to optimize the base and digits for. We call it max
   * because it is the largest number in the input set that we have to radix
   * sort.
   * @return A KVTuple whose key is the base and whose value is the number of
   * digits
   */
  private static KVTuple<Integer, Integer> getOptimalBaseDigits(long max) {
    for (int base = 2; base < max; ++base) {
      int digits = (int) Math.ceil(Math.log(max) / Math.log(base));
      if (digits < base) {
        return new KVTuple<>(base, digits);
      }
    }
    throw new UnsupportedOperationException("Oh shit, no valid base found!");
  }

  /**
   * Execute a radix sort on <pre>data</pre>
   *
   * @param data The data to sort as a mxn matrix where m is the number of
   * elements and n is the nth digit of the element.
   * @param base The number base which all the data elements are represented
   * as.
   * @param digits The maximum number of digits in any number. The length of any
   * subarray in data should never exceed this number.
   * @return <pre>data</pre>, sorted
   * @time O(log_2(n))
   */
  private static short[][] radixSort(short[][] data, int base, int digits) {
    // Create two parallel arrays
    ArrayList<Deque<short[]>> buckets1 = new ArrayList<>();
    ArrayList<Deque<short[]>> buckets2 = new ArrayList<>();
    // Put a linked list into every location in both arrays
    // There will be "base" buckets in both groups of buckets.
    for (int i = 0; i < base; ++i) {
      buckets1.add(new Deque<>());
      buckets2.add(new Deque<>());
    }
    // The meat of the Radix Sort
    // First take all the data and put it in bucket1.
    int digit = 0;
    for (short[] number : data) { // O(n)
      buckets1.get(number[digit]).pushLast(number);
    }
    ArrayList<Deque<short[]>> emptyBuckets = buckets2;
    ArrayList<Deque<short[]>> filledBuckets = buckets1;
    for (; digit < digits; ++digit) {
      // Loop over each of the buckets
      for (int j = 0; j < filledBuckets.size(); ++j) { // O(n)
        // Get the current bucket
        Deque<short[]> currentBucket = filledBuckets.get(j);
        // While it's not empty,
        while (!currentBucket.isEmpty()) {
          // Pop everything out of the bucket
          short[] num = currentBucket.popFirst();
          // Insert it into the proper bucket based on the next digit to examine
          emptyBuckets.get(num[digit]).pushLast(num);
        }
      }
      // Alternate between the buckets to sort the data. Basically we're
      // pouring the data back and forth between the two buckets until
      // they're sorted.
      ArrayList<Deque<short[]>> tmp = filledBuckets;
      filledBuckets = emptyBuckets;
      emptyBuckets = tmp;
    }
    // Traverse through the buckets to get the sorted array.
    short[][] result = new short[data.length][];
    int counter = 0;
    // For each bucket,
    for (int i = 0; i < filledBuckets.size(); ++i) {
      Deque<short[]> currentBucket = filledBuckets.get(i);
      // pop the elements out of the bucket and put them into the result list
      while (!currentBucket.isEmpty()) {
        result[counter++] = currentBucket.popFirst();
      }
    }
    return result;
  }

  public static void main(String[] args) {
    // Read in all the data using Scanner
    try (Scanner in = new Scanner(System.in)) {
      int n = in.nextInt();
      long[] data = new long[n];
      long max = 0;
      for (int i = 0; i < n; ++i) {
        data[i] = in.nextLong();
        if (data[i] > max) {
          max = data[i];
        }
      }

      // Calculate the optimal base to convert the numbers to and the maximum
      // number of digits each number will have.
      KVTuple<Integer, Integer> baseDigits = getOptimalBaseDigits(max);
      int base = baseDigits.getKey();
      int digits = baseDigits.getValue();

      // Take the list of inputs, convert it to the proper base, and split it
      // digit by digit so that we have an n x maxLen matrix where n is the
      // number of elements and maxLen is the number of digits in the largest
      // element. In this matrix of shorts, the nth element is the base^n place.
      short[][] matrix = new short[n][];
      for (int i = 0; i < n; ++i) {
        matrix[i] = convertFromBase10(data[i], base, digits);
      }

      // Radix sort the shit.
      matrix = radixSort(matrix, base, digits);

      // After we radix sort, we have to sum every 10th element as per the
      // modified instructions. We convert every 10th element back to a long
      // so we can add it back to the result.
      BigInteger result = new BigInteger("0");
      BigInteger n_ = new BigInteger(n + "");
      BigInteger mod = n_.multiply(n_);
      for (int i = 9; i < n; i += 10) {
        long currentNumber = 0;
        for (int place = 0; place < digits; ++place) {
          currentNumber += (matrix[i][place] * Math.pow(base, place));
        }
        result = result.add(new BigInteger(currentNumber + "")).mod(mod);
      }
      System.out.println(result);
    }
  }
}
