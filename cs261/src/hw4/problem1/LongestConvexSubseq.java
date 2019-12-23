import java.util.Scanner;

/**
 * LongestConvexSubseq: Find the longest subsequence of a given input that is
 * convex: We say that a subsequence a_j1, a_j2, ..., a_jk, where j_1 &lt; j_2
 * &lt; ... &lt; j_k, is convex if a_j(i−1) + a_j(i+1) ≥ 2a_ji for every i ∈ {2,
 * 3, ..., k−1}
 *
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class LongestConvexSubseq {

  /**
   * The meat of the algorithm. Calculate the longest convex subsequence in the
   * input sequence.
   *
   * @param sequence The sequence to examine
   * @return The length of the longest convex subsequence in
   * <code>sequence</code>
   * @time O(n³)
   */
  private static int getLongestConvexSubsequenceL(int[] sequence) {
    int n = sequence.length;
    // Heart of the solution: S[i][j] is the length of the longest convex
    // subsequence ending with a_i whose second to last element is a_j.
    int[][] solution = new int[n][n];
    // Populate the solution matrix
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j <= i; ++j) {
        // Base cases for sequences less than length 3.
        if (i < 2) {
          solution[i][j] = i + 1;
        } else if (j == 0) {
          solution[i][j] = 2;
        } else if (i == 2) {
          // For a subsequence ending with a_i where i is 2, it can either be
          // a length 3 convex subsequence or a length 2 non-convex subsequence.
          if (isConvexTriplet(sequence[0], sequence[1], sequence[2])) {
            solution[i][j] = 3;
          } else {
            solution[i][j] = 2;
          }
        } else if (i == j) {
          // Having i == j doesn't make sense with our heart of the solution,
          // but we will use this part of the solution matrix to store the
          // maximum length achieved in row i for quick and easy reference
          // later.
          for (int k = 0; k < i; ++k) {
            if (solution[i][k] > solution[i][j]) {
              solution[i][j] = solution[i][k];
            }
          }
        } else {
          // In all other cases, we take a_i and assume it is the last
          // element of a convex subsequence whose second to last element is
          // a_j, we then iterate from 0 to j to find a third to last element
          // a_k that would satisfy the convexity of a_k, a_j, and a_i. If
          // such elements exist, we take the a_k that would yield the
          // longest length convex subsequence. If no such k exists, then the
          // length of this subsequence containing a_i and a_j is 2.
          for (int k = 0; k < j; ++k) {
            if (isConvexTriplet(sequence[k], sequence[j], sequence[i])) {
              solution[i][j] = Math.max(solution[i][j], solution[j][k] + 1);
            } else {
              solution[i][j] = Math.max(solution[i][j], 2);
            }
          }
        }
      }
    }
    // Since we have already stored the maximums along the diagonal, we
    // simply look along the diagonal at the end to find the solution.
    int max = 0;
    for (int i = 0; i < n; ++i) {
      max = Math.max(max, solution[i][i]);
    }
    return max;
  }

  /**
   * Do the three numbers provided as arguments satisfy the conditions of a
   * convex triplet? Namely, is (a + c) >= 2b?
   *
   * @param a The first element in the triplet
   * @param b The second element in the triplet
   * @param c The third element in the triplet
   * @return True if (a + c) >= 2*b, false otherwise
   * @time O(1)
   */
  private static boolean isConvexTriplet(int a, int b, int c) {
    return (a + c) >= 2 * b;
  }

  /**
   * The main function for LongestConvexSubseq, which accepts input from stdin
   * and delegates responsibilities appropriately.
   *
   * @param args Any command line arguments to this program.
   */
  public static void main(String[] args) {
    int n;
    int[] sequence;
    try (Scanner stdin = new Scanner(System.in)) {
      n = stdin.nextInt();
      sequence = new int[n];
      for (int i = 0; i < n; ++i) {
        sequence[i] = stdin.nextInt();
      }
    }
    System.out.println(getLongestConvexSubsequenceL(sequence));
  }

}
