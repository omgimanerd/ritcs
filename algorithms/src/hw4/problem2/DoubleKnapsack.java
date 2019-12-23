import java.util.Scanner;

/**
 * DoubleKnapsack: The Knapsack problem, but with two knapsacks to fill
 *
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class DoubleKnapsack {

  /**
   * Instance Variables
   */
  private int[][][] solution;
  private int[][] items;
  private int w1;
  private int w2;

  /**
   * Create a new DoubleKnapsack solver
   *
   * @param items The items to examine (item[0] = weight, item[1] = cost)
   * @param w1 The maximum weight capacity of knapsack 1
   * @param w2 The maximum weight capacity of knapsack 2
   */
  public DoubleKnapsack(int[][] items, int w1, int w2) {
    this.items = items;
    this.w1 = w1;
    this.w2 = w2;
    // initialize the Dynamic Programming solution matrix
    solution = new int[items.length + 1][w1 + 1][w2 + 1];
    // Make initial arrays inside the matrix, since Java doesn't do that
    // automatically for us
    for (int i = 0; i < items.length + 1; ++i) {
      solution[i] = new int[w1 + 1][w2 + 1];
      for (int j = 0; j < w1 + 1; ++j) {
        solution[i][j] = new int[w2 + 1];
      }
    }
  }

  /**
   * Use the DoubleKnapsack instance to solve the problem.
   *
   * @param items The items to examine (item[0] = weight, item[1] = cost)
   * @param w1 The maximum weight capacity of knapsack 1
   * @param w2 The maximum weight capacity of knapsack 2
   * @return The maximum cost of a set of items that can be put into the
   * knapsacks
   */
  private static int maxCostDoubleKnapsack(int[][] items, int w1, int w2) {
    // Return the solution
    DoubleKnapsack dk = new DoubleKnapsack(items, w1, w2);
    int[][][] solution = dk.solve();
    return solution[items.length][w1][w2];
  }

  /**
   * The meat of the algorithm. Given a set of items, each with a cost and a
   * weight, as well as the maximum mass capacity of two knapsacks, find the
   * maximum cost of a set of items that can be put into the knapsacks.
   *
   * @return A dynamic programming 3D matrix of solutions
   */
  public int[][][] solve() {
    // Convenience name
    int n = items.length;
    // Loop over every element
    for (int k = 0; k < n + 1; ++k) {
      // Loop over every weight for knapsack 1
      for (int w1Counter = 0; w1Counter < w1 + 1; ++w1Counter) {
        // Loop over every weight for knapsack 2
        for (int w2Counter = 0; w2Counter < w2 + 1; ++w2Counter) {
          // Initialize the first layer of the matrix to 0
          if (k == 0) {
            solution[k][w1Counter][w2Counter] = 0;
          } else {
            // Pull out the current item
            int[] item = items[k - 1];
            // Pull out the current item's weight
            int weight = item[0];
            // Pull out the current item's cost
            int cost = item[1];
            // Pull out the previous row, for convenience
            int[][] previous = solution[k - 1];
            // If the item's weight could fit in both, then we take the maximum
            // cost from including it in either.
            if (weight <= w1Counter && weight <= w2Counter) {
              solution[k][w1Counter][w2Counter] = Util.max(
                  previous[w1Counter][w2Counter],
                  cost + previous[w1Counter - weight][w2Counter],
                  cost + previous[w1Counter][w2Counter - weight]
              );
              // If the item's weight fits only inside the first knapsack, then
              // we take the maximum cost from including it or not including it.
            } else if (weight <= w1Counter) {
              solution[k][w1Counter][w2Counter] = Util.max(
                  previous[w1Counter][w2Counter],
                  cost + previous[w1Counter - weight][w2Counter]
              );
              // If the item's weight fits only inside the second knapsack, then
              // we take the maximum cost from including it or not including it.
            } else if (weight <= w2Counter) {
              solution[k][w1Counter][w2Counter] = Util.max(
                  previous[w1Counter][w2Counter],
                  cost + previous[w1Counter][w2Counter - weight]
              );
              // If the item fits in neither, then we just take the cost from
              // not including it.
            } else {
              solution[k][w1Counter][w2Counter] = previous[w1Counter][w2Counter];
            }
          }
        }
      }
    }
    return solution;
  }

  /**
   * The main function for DoubleKnapsack, which accepts input from stdin and
   * delegates responsibilities appropriately.
   *
   * @param args Any command line arguments to this program
   */
  public static void main(String[] args) {
    try (Scanner stdin = new Scanner(System.in)) {
      // Read in the number of items
      int n = stdin.nextInt();
      // Read in the weight capacity for knapsack 1
      int w1 = stdin.nextInt();
      // Read in the weight capacity for knapsack 2
      int w2 = stdin.nextInt();
      // Read in the items
      int[][] items = new int[n][];
      for (int i = 0; i < n; i++) {
        items[i] = new int[2];
        items[i][0] = stdin.nextInt();
        items[i][1] = stdin.nextInt();
      }
      // Do the hard part (and print out the result)
      System.out.println(maxCostDoubleKnapsack(items, w1, w2));
    }
  }
}
