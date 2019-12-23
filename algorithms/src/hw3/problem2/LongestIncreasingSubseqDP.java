import java.util.Scanner;

public class LongestIncreasingSubseqDP {

  private static long getLongestIncreasingSubsequenceLength(int[] sequence) {
    int[] solution = new int[sequence.length];
    for (int i = 0; i < sequence.length; ++i) {
      solution[i] = 1;
      for (int j = 0; j < i; ++j) {
        if (sequence[j] < sequence[i] && solution[i] < solution[j] + 1) {
          solution[i] = solution[j] + 1;
        }
      }
    }
    int max = 0;
    for (int element : solution) {
      if (element > max) {
        max = element;
      }
    }
    return max;
  }

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
    System.out.println(getLongestIncreasingSubsequenceLength(sequence));
  }
}
