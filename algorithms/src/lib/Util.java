/**
 * Utility functions.
 *
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class Util {

  /**
   * Returns the nth digit of num where n = 0 is the ones digit, n = 1 is the
   * tens digit, etc.
   *
   * @param num The number to find the digit of
   * @param n The digit to find
   * @return The nth digit of num
   */
  public static int getDigit(long num, int n) {
    while (n-- > 0) {
      num /= 10;
    }
    return (int) (num % 10);
  }

  public static int getDigits(long num) {
    int count = 0;
    while (num > 0) {
      num /= 10;
      ++count;
    }
    return count;
  }

  /**
   * Given an integer n, this function returns n if the integer is positive, -1
   * if the integer is negative, and 0 otherwise.
   *
   * @param n The integer to compare
   * @return -1, 0, 1 depending on n
   */
  public static int getSign(int n) {
    if (n > 0) {
      return 1;
    } else if (n < 0) {
      return -1;
    }
    return 0;
  }

  /**
   * Helper function to compute a log with a custom base.
   *
   * @param x The number to compute the log of
   * @param base The log base to compute
   * @return The computed logarithm
   */
  public static float log(float x, float base) {
    return (float) (Math.log(x) / Math.log(base));
  }

  /**
   * Find the maximum of a set of integers
   *
   * @param values The integers to find the maximum value among
   * @return The value of the largest integer in the set
   */
  public static int max(int... values) {
    // Pick the first element as an arbitrary starting value
    int max = values[0];
    // For each one,
    for (int i = 1; i < values.length; ++i) {
      // If it's larger than the current maximum, keep it
      if (values[i] > max) {
        max = values[i];
      }
    }
    // Return the max value
    return max;
  }

  /**
   * Calculate the sum of all positive integers (number class, not Integer()) up
   * to and including n.
   *
   * @param n The number to sum up to
   * @return The sum of 1...n
   * @time O(1)
   */
  public static long sumToN(long n) {
    return (n * (n + 1)) / 2;
  }


}
