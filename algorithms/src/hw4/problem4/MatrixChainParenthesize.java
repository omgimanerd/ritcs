import java.util.Scanner;

/**
 * MatrixChainParenthesize: Given a chain of matrices to multiply, optimize the
 * order of multiplication to reduce the time it takes to calculate the
 * product.
 *
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class MatrixChainParenthesize {

  private int nMatrices;

  private int[][] solution;
  private int[][][] groupings;

  private boolean solved;

  public MatrixChainParenthesize(int nMatrices) {
    this.nMatrices = nMatrices;
    int dimensions = nMatrices + 1;
    this.solution = new int[dimensions][dimensions];
    this.groupings = new int[dimensions][dimensions][3];
    this.solved = false;
  }

  private String reconstructSolution() {
    if (!solved) {
      throw new IllegalStateException("No solution computed!");
    }
    return reconstructSolutionRecursive(groupings[0][nMatrices - 1]);
  }

  private String reconstructSolutionRecursive(int[] solutionGroup) {
    int l = solutionGroup[0];
    int k = solutionGroup[1];
    int r = solutionGroup[2];
    if (l + 1 == r) {
      return String.format("( A%d x A%d )", l + 1, r + 1);
    } else if (l == k) {
      return String.format("( A%d x %s )", l + 1,
          reconstructSolutionRecursive(groupings[k + 1][r]));
    } else if (k + 1 == r) {
      return String.format("( %s x A%d )",
          reconstructSolutionRecursive(groupings[l][k]), r + 1);
    }
    return String.format("( %s x %s )",
        reconstructSolutionRecursive(groupings[l][k]),
        reconstructSolutionRecursive(groupings[k + 1][r]));
  }

  private void solve(int[] dimensions) {
    if (solved) {
      throw new IllegalStateException("Already in a solved state!");
    }
    int n = dimensions.length - 1;
    for (int i = 0; i < n; ++i) {
      solution[i] = new int[n];
      groupings[i] = new int[n][3];
    }
    // Compute the cost for every pair of matrices, then the cost for every
    // triplet of matrices, and so on to build the solution matrix.
    for (int d = 1; d < n; ++d) {
      for (int l = 0; l < n - d; ++l) {
        int r = l + d;
        solution[l][r] = Integer.MAX_VALUE;
        for (int k = l; k < r; ++k) {
          int tmp = solution[l][k] + solution[k + 1][r] + (dimensions[l] *
              dimensions[k + 1] * dimensions[r + 1]);
          if (tmp < solution[l][r]) {
            solution[l][r] = tmp;
            groupings[l][r] = new int[3];
            groupings[l][r][0] = l;
            groupings[l][r][1] = k;
            groupings[l][r][2] = r;
          }
        }
      }
    }
    solved = true;
  }

  /**
   * The main function for MatrixChainParenthesize, which accepts input from
   * stdin and delegates responsibilities appropriately.
   *
   * @param args Any command line arguments to this program
   */
  public static void main(String[] args) {
    try (Scanner stdin = new Scanner(System.in)) {
      int nMatrices = stdin.nextInt();
      int nDimensions = nMatrices + 1;
      int[] dimensions = new int[nDimensions];
      for (int i = 0; i < nDimensions; i++) {
        dimensions[i] = stdin.nextInt();
      }
      MatrixChainParenthesize solver = new MatrixChainParenthesize(nMatrices);
      solver.solve(dimensions);
      System.out.println(solver.solution[0][nMatrices - 1]);
      System.out.println(solver.reconstructSolution());
    }
  }
}
