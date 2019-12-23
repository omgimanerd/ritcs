import java.util.Scanner;

public class SmallestTwo {

  public int l1 = Integer.MAX_VALUE;
  public int l2 = Integer.MAX_VALUE;

  public void update(int n) {
    if (n < l1) {
      l2 = l1;
      l1 = n;
    } else if (n < l2) {
      l2 = n;
    }
  }

  public static void main(String[] args) {
    SmallestTwo t = new SmallestTwo();
    Scanner in = new Scanner(System.in);
    int n = in.nextInt();
    for (int i = 0; i < n; ++i) {
      t.update(in.nextInt());
    }
    System.out.println(t.l1);
    System.out.println(t.l2);
  }
}
