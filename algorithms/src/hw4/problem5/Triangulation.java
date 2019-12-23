import java.util.Scanner;

/**
 * Triangulation: Triangulate an arbitrary convex polygon such that the length
 * of all lines "drawn" inside the polygon is maximized.
 *
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class Triangulation {

  /**
   * Calculate the distance between two points, given as double arrays
   *
   * @param p1 The first point
   * @param p2 The second point
   * @return The distance across a cartesian plane between p1 and p2
   */
  private static double getDistance(double[] p1, double[] p2) {
    double x = (p1[0] - p2[0]) * (p1[0] - p2[0]);
    double y = (p1[1] - p2[1]) * (p1[1] - p2[1]);
    return Math.sqrt(x + y);
  }

  /**
   * The meat of the algorithm.
   *
   * @param coordinates A list of coordinates which form a convex polygon
   * @return The minimum length of all internal lines when triangulating this
   * polygon
   */
  private static double getMinTriangulationLength(double[][] coordinates) {
    int n = coordinates.length;
    double[][] solution = new double[n][n];
    // Calculate the minimum triangulation after we split the n-gon into
    // three polygons, a triangle, and two n-gons which we should have
    // previously solved.
    for (int i = 0; i < n; ++i) {
      int j = 0;
      for (int k = i; k < n; ++k) {
        double length = getDistance(coordinates[j], coordinates[k]);
        if (k < j + 2) {
          solution[j][k] = 0;
        } else {
          solution[j][k] = Double.MAX_VALUE;
          for (int s = j + 1; s < k; ++s) {
            solution[j][k] = Math.min(
                solution[j][s] + solution[s][k] + length, solution[j][k]);
          }
          // Prevent the double counting of a triangulation length on the two
          // adjacent polygons.
          if (j == 0 && k == n - 1) {
            solution[j][k] -= length;
          }
        }
        ++j;
      }
    }
    return solution[0][n - 1];
  }

  /**
   * Do subtraction, modulo mod
   *
   * @param subtrahend Subtrahend
   * @param minuend Minuend
   * @param mod modulus
   * @return difference, adjusted for the mod
   */
  private static int mod_sub(int subtrahend, int minuend, int mod) {
    if (minuend > subtrahend) {
      subtrahend += mod;
    }
    return subtrahend - minuend;
  }

  /**
   * Modulo, but the answer is always positive.
   *
   * @param i The number to divide
   * @param n The modulus
   * @return The remainder of dividing i/n
   */
  private static int posMod(int i, int n) {
    return ((i % n) + n) % n;
  }

  /**
   * Print out the provided matrix with %6.2f as the format code
   *
   * @param matrix The matrix to print
   */
  private static void printMatrix(double[][] matrix) {
    for (int i = 0; i < matrix.length; ++i) {
      for (int j = 0; j < matrix.length; ++j) {
        System.out.print(String.format("%7.2f ", matrix[i][j]));
      }
      System.out.println();
    }
    System.out.println();
  }

  /**
   * The main function for Triangulation, which accepts input from stdin and
   * delegates responsibilities appropriately.
   *
   * @param args Any command-line arguments to this program
   */
  public static void main(String[] args) {
    try (Scanner stdin = new Scanner(System.in)) {
      int n = stdin.nextInt();
      double[][] coordinates = new double[n][2];
      for (int i = 0; i < n; i++) {
        coordinates[i][0] = stdin.nextDouble();
        coordinates[i][1] = stdin.nextDouble();
      }
      double result = getMinTriangulationLength(coordinates);
      System.out.println(String.format("%.4f", result));
    }
  }
}
