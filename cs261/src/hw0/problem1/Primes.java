import java.util.Scanner;

public class Primes {

  public static int[] sieve(int n) {
    int numbers[] = new int[n + 1];
    // Mark the index with 1 to represent that the number is not prime
    numbers[0] = 1;
    numbers[1] = 1;
    // Perform the sieve of eratosthenes
    for (int i = 2; i < n + 1; ++i) {
      if (numbers[i] != 1) {
        for (int j = i + i; j < n + 1; j += i) {
          // Mark all multiples because they are composite
          numbers[j] = 1;
        }
      }
    }
    // Isolate all the non-marked indices
    int results[] = new int[n + 1];
    int c = 0;
    for (int i = 0; i < n + 1; ++i) {
      if (numbers[i] == 0) {
        results[c++] = i;
      }
    }
    // Returns an array whose first k elements contain the k primes from 0 to n
    // The rest of the array elements will be empty
    return results;
  }

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    int[] results = sieve(in.nextInt());
    int i = 0;
    while (results[i] != 0) {
      System.out.println(results[i++]);
    }
  }
}
