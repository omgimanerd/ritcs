import java.util.Scanner;

/**
 * StringPuzzle: A program which computes a variant of Levenshtein distance for
 * two strings, specified on stdin. Specifically, it only works for strings of
 * the same length, and it calculates the number of +/- shifts for contiguous
 * groups of letters required to change one string into the other.
 *
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class StringPuzzle {

  /**
   * Subtract the absolute minimum value of the input ArrayList from each of its
   * elements.
   *
   * @param chunk The chunk to "cut the bottom off of"
   * @return The number that was subtracted from each element
   * @time O(2n)
   */
  private static short chopMin(ArrayList<Short> chunk) {
    short minValue = min(chunk); // O(n)
    for (int i = 0; i < chunk.size(); i++) { // O(n)
      chunk.set(i, (short) (chunk.get(i) - minValue));
    }
    return (short) Math.abs(minValue);
  }

  /**
   * Break the input ArrayList into chunks at boundaries where two elements
   * define a line which crosses the x axis.
   *
   * @param distances An ArrayList produced by StringPuzzle.stringDistance()
   * @return An ArrayList of ArrayLists where each element in the outer
   * ArrayList is a contiguous group of elements from the input list, and is all
   * on one side of the x axis
   * @time O(n)
   */
  private static ArrayList<ArrayList<Short>> chunk(ArrayList<Short> distances) {
    // When the two strings are subtracted from each other, we receive a list
    // of the distances between each letter in the two strings. This list of
    // distances can be represented as a series of heights/depths in a mountain
    // range. We will refer to the letter distances as heights, and the
    // contiguous groups of letters as "mountains" or "valleys".
    //
    // Mountains can have valleys inside of them and vice versa. For example,
    // given the following list of letter distances:
    // [ 1 3 2 3 2 1 0 0 2 3 0 2 2 2 0 -1 -2 -3 -2 -3 -1 ]
    //
    // We can split this into four mountains for processing:
    // [ 1 3 2 3 2 1 ]
    // [ 2 3 ]
    // [ 2 2 2 ]
    // [ -1 -2 -3 -2 -3 -1 ]
    //
    // Note that [ 1 3 2 3 2 1 ] is a mountain with a valley inside.
    // Also note that [ -1 -2 -3 -2 -3 -1 ] is a valley with a mountain inside.
    //
    // Mountains and valleys will be handled the same way in the end, so we will
    // refer to them both as mountains for the purposes of this algorithm.
    ArrayList<ArrayList<Short>> mountains = new ArrayList<>();
    // We will use a sliding window of size two to group together distances into
    // a single mountain. lastHeight contains the height of the last point so
    // that we know if we cross zero.
    // We initialize the first mountain to be empty.
    int lastHeight = 0;
    ArrayList<Short> mountain = new ArrayList<>();
    for (int i = 0; i < distances.size(); ++i) {
      short currentHeight = distances.get(i);
      // There are a few cases that can happen. If the last height was 0 and the
      // current height is 0, then we do nothing. The last mountain ended and no
      // new mountain is starting.
      if (lastHeight == 0 && currentHeight == 0) {
        // In the second case, the last height was 0 and the current height is
        // non-zero, meaning we are starting to climb another mountain. We should
        // initialize a mountain and add this height as a part of the mountain
        // range.
      } else if (lastHeight == 0 && currentHeight != 0) {
        mountain.add(currentHeight);
        // In the third case, the last height was non-zero and the current height
        // is 0, meaning we are coming down back to the ground from a mountain.
        // This means the mountain has ended, we should add this mountain to the
        // list of mountains and start a new one since the next non-zero value
        // will be part of a new mountain.
      } else if (lastHeight != 0 && currentHeight == 0) {
        mountains.add(mountain);
        mountain = new ArrayList<>();
        // In the fourth case, the last height and the current height are on
        // opposite sides of zero. This means that:
        // 1) the sliding window exited a valley and is climbing a mountain
        // 2) the sliding window came down from a mountain and is going into
        //    a valley.
        // In this case, we add the current mountain/valley to the array of
        // mountains and start a new one. The current height value is on the
        // opposite side of zero as the previous value so it goes in the new
        // mountain/valley.
      } else if (crossesZero(lastHeight, currentHeight)) {
        mountains.add(mountain);
        mountain = new ArrayList<>();
        mountain.add(currentHeight);
        // In the last case, the current height is on the same side of zero
        // as the previous value, and is also non-zero, meaning it is part of
        // the same mountain/valley range. We add it to the existing mountain
        // and continue.
      } else {
        mountain.add(currentHeight);
      }
      // Set the last height to the current height to shift our sliding window.
      lastHeight = currentHeight;
    }
    // Once we reach the end of the list of letter distances, we know the last
    // mountain must have ended, so we add it to the list of mountains and
    // return the list of mountains.
    if (mountain.size() != 0) {
      mountains.add(mountain);
    }
    return mountains;
  }

  /**
   * Does the line between a and b cross the x axis?
   *
   * @param a The first point to check
   * @param b The second point to check
   * @return True if the line between a and b crosses the x axis, false
   * otherwise
   */
  private static boolean crossesZero(int a, int b) {
    return (a > 0 && b < 0) || (a < 0 && b > 0);
  }

  /**
   * Find the absolute minimum value (closest to 0) in the ArrayList
   *
   * @return The absolute minimum value in the ArrayList
   * @time O(n)
   */
  private static short min(ArrayList<Short> list) {
    int minValue = list.get(0);
    int tmpValue;
    for (int i = 0; i < list.size(); i++) {
      tmpValue = list.get(i);
      if (Math.abs(tmpValue) < Math.abs(minValue)) {
        minValue = tmpValue;
      }
    }
    return (short) minValue;
  }

  /**
   * Create an ArrayList where each value is the difference in character
   * codepoints between s and t. (s_i - t_i = return_i)
   *
   * @param s The subtrahend
   * @param t The minuend
   * @return An ArrayList in which each term is the difference of s and t
   * @time O(n)
   */
  private static ArrayList<Short> stringDistance(String s, String t) {
    int sl = s.length();
    int tl = t.length();
    if (sl != tl) {
      throw new RuntimeException("Strings are not the same length");
    }
    ArrayList<Short> d = new ArrayList<>();
    for (int i = 0; i < sl; ++i) {
      d.add((short) (s.charAt(i) - t.charAt(i)));
    }
    return d;
  }

  /**
   * The main function for StringPuzzle.java
   *
   * @param args The command-line arguments for this program
   * @time O(2n² + 2n)
   */
  public static void main(String[] args) {
    try (Scanner in = new Scanner(System.in)) {
      int shifts = 0;
      String[] input = in.nextLine().split(" ");
      String s1 = input[0];
      String s2 = input[1];
      ArrayList<Short> d = stringDistance(s1, s2);

      // Create a Queue to push the chunks onto
      Deque<ArrayList<Short>> queue = new Deque<>();
      // Split the d array into chunks at places where it crosses 0
      ArrayList<ArrayList<Short>> chunks = chunk(d); // O(n)
      // Add the d array chunks into the processing queue
      for (int i = 0; i < chunks.size(); ++i) { // O(n)
        queue.pushLast(chunks.get(i));
      }
      // The number of executions of this loop is bounded linearly by the
      // size of the input. To understand why, take a look at it from a
      // different perspective, one that's rotated about 90º.
      // The greatest number of times that an input needs to be chunked is a
      // fraction of n, plus or minus a small constant. In the worst case,
      // the input results in a d array like this:
      // [-25, 25, -25, 25, -25, 25, -25, 25,...]
      // This would be split up into n chunks to be chopMin()ed, which means
      // that the time complexity of this loop is O(n), with a moderately large
      // constant.
      while (!queue.isEmpty()) {
        // Take one mountain off the queue
        ArrayList<Short> currentMountain = queue.popFirst(); // O(1)
        // Cut the bottom off of it and record the number of shifts
        shifts += chopMin(currentMountain); // O(2n)
        // Re-chunk the newly trimmed mountain in case it was cut down to a
        // minimum
        ArrayList<ArrayList<Short>> subMountains = chunk(currentMountain);//O(n)
        // Add the potential new mountains into the queue for further
        // processing.
        for (int i = 0; i < subMountains.size(); ++i) {
          queue.pushLast(subMountains.get(i));
        }
      }
      System.out.println(shifts);
    }
  }
}
