import java.util.Scanner;

public class LongestIncreasingSubseqRecursive {

  private static class ArrayListLengthComparator implements
      Comparator<ArrayList<Integer>> {

    public int compare(ArrayList a1, ArrayList a2) {
      if (a1.size() > a2.size()) {
        return 1;
      } else {
        return (a1.size() < a2.size() ? -1 : 0);
      }
    }
  }

  private static ArrayList<ArrayList<Integer>> memoizer;

  private static long getLongestIncreasingSubsequenceLength(int[] sequence) {
    int maxLen = 0;
    int newLen;
    memoizer = new ArrayList<>(sequence.length);
    for (int i = 0; i < sequence.length; ++i) {
      memoizer.add(null);
    }
    for (int j = sequence.length - 1; j >= 0; j--) {
      newLen = incrSubseqRecursive(j, sequence).size();
      if (newLen > maxLen) {
        maxLen = newLen;
      }
    }
    return maxLen;
  }

  private static ArrayList<Integer> incrSubseqRecursive(
      int index, int[] A) {
//    if (memoizer.get(index) != null) {
//      return memoizer.get(index);
//    }
    ArrayList<Integer> answer;
    Heap<ArrayList<Integer>> possibilities = new Heap<>(Heap.Type.MAX,
        new ArrayListLengthComparator());
    for (int i = index - 1; i >= 0; i--) {
      if (A[i] < A[index]) {
        possibilities.add(incrSubseqRecursive(i, A));
      }
    }
    if (!possibilities.isEmpty()) {
      answer = new ArrayList<>(possibilities.pop());
    } else {
      answer = new ArrayList<>();
    }
    answer.add(A[index]);
//    memoizer.set(index, answer);
    return answer;
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
