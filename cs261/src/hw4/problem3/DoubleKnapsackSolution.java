import java.util.Scanner;

/**
 * DoubleKnapsackSolution: The Knapsack Problem, but with two knapsacks to fill.
 * Also, this version reconstructs the solution.
 *
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class DoubleKnapsackSolution {

  /**
   * Given a list of items, each with a cost and a weight, as well as the weight
   * capacities of two knapsacks, find the list of items to put in each knapsack
   * such that the cost of all items in knapsacks is maximized.
   *
   * @param items The items to examine (item[0] = weight, item[1] = cost)
   * @param w1 The weight capacity of knapsack 1
   * @param w2 The weight capacity of knapsack 2
   * @post The indexes of each element in the solution are printed to stdout
   */
  private static void solutionSetDoubleKnapsack(int[][] items, int w1, int w2) {
    //
    // Use problem 2 to get a solution matrix
    //
    DoubleKnapsack dk = new DoubleKnapsack(items, w1, w2);
    int[][][] solution = dk.solve();

    //
    // Reconstruct the solution
    //
    Deque<Integer> knapsack1 = new Deque<>();
    Deque<Integer> knapsack2 = new Deque<>();

    // Counters for the current values of W1 and W2
    int curW1 = w1;
    int curW2 = w2;
    // Loop over each item
    for (int i = items.length - 1; i >= 0; i--) {
      // Pull out the item's weight
      int weight = items[i][0];
      // Pull out the item's cost
      int cost = items[i][1];
      // Pull out the solution value at the current location in the matrix
      int currentMatrixValue = solution[i + 1][curW1][curW2];
      // Pull out the previous solution set
      int[][] prevSoln = solution[i];
      // If the current knapsack 1 weight minus this item's weight is greater
      // than or equal to zero, and if the current matrix value is equal to the
      // cost  of this item, plus the previous cost excluding this item from
      // knapsack 1,
      if (curW1 - weight >= 0 && currentMatrixValue == cost +
          prevSoln[curW1 - weight][curW2]) {
        // then this item is part of the solution. Push it into the stack for
        // knapsack 1 and subtract its weight from the current weight in
        // knapsack 1
        knapsack1.pushFirst(i + 1);
        curW1 -= weight;
        // If the current knapsack 2 weight minus this item's weight is greater
        // than or equal to 0, and if the current matrix value is equal to the
        // cost of this item, plus the previous cost excluding this item from
        // knapsack 2,
      } else if (curW2 - weight >= 0 && currentMatrixValue == cost +
          prevSoln[curW1][curW2 - weight]) {
        // then this item is part of the solution. Push it into the stack for
        // knapsack 2 and subtract its weight from the current weight in
        // knapsack 2
        knapsack2.pushFirst(i + 1);
        curW2 -= weight;
      }
      // Otherwise, it isn't part of the solution, so we do nothing.
    }
    // Pop all of the stuff out of knapsack 1's stack and print it
    while (!knapsack1.isEmpty()) {
      System.out.print(knapsack1.popFirst() + " ");
    }
    System.out.println("");
    // Pop all of the stuff out of knapsack 2's stack and print it
    while (!knapsack2.isEmpty()) {
      System.out.print(knapsack2.popFirst() + " ");
    }
    System.out.println("");
  }

  /**
   * The main function for DoubleKnapsackSolution, which accepts input from
   * stdin and delegates responsibilities appropriately.
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
      // Do the hard part, which prints out the result.
      solutionSetDoubleKnapsack(items, w1, w2);
    }
  }
}
