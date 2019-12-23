import java.util.Scanner;

/**
 * SpanTree: Create a minimum-weight spanning tree for the given graph which
 * includes a certain subset of vertices.
 *
 * @author William Leuschner wel2138@rit.edu
 * @author Alvin Lin axl1439@rit.edu
 */
public class SpanTree {

  private static class Edge {

    int vertex1;
    int vertex2;
    int weight;
    boolean isF;

    Edge(String[] data) {
      this.vertex1 = Integer.parseInt(data[0]) - 1;
      this.vertex2 = Integer.parseInt(data[1]) - 1;
      this.weight = Integer.parseInt(data[2]);
      this.isF = data[3].equals("1");
    }

    public String toString() {
      return String.format(
          "V1: %d, V2: %d, W: %d, isF: %b", vertex1, vertex2, weight, isF);
    }
  }

  /**
   * Who works at the Krusty Komparator? GraphBob
   * Hello, is this the Krusty Komparator? No, this is Prim.
   * Anyway, this Comparator compares edges against one another to sort them.
   * We first prioritize by whether or not the edge is a member of the set F
   * as described by the problem, then we compare the weights of the edges.
   */
  private static Comparator<Edge> krustyKomparator = (e1, e2) -> {
    if (e1.isF == e2.isF) {
      return e1.weight - e2.weight;
    }
    return e1.isF ? -1 : 1;
  };

  /**
   * This method runs Kruskal's minimum spanning tree algorithm on the list
   * of edges after sorting the edges first by whether they are in the subset
   * F, and secondly by weight.
   * @param n The number of nodes in the graph
   * @param edges The list of edges in the graph
   * @return The total weights of all the edges in the minimum spanning tree
   * that contains F, or -1 if no such minimum spanning tree exists.
   */
  private static int krusty(int n, Edge[] edges) {
    // Heap sort the edges by whether or not they're included in the subset F
    // and by weight.
    Heap<Edge> heap = new Heap<>(Heap.Type.MIN, krustyKomparator);
    for (Edge edge : edges) {
      heap.add(edge);
    }

    ArrayList<Edge> tree = new ArrayList<>();
    DisjointSet set = new DisjointSet(n);
    while (!heap.isEmpty()) {
      Edge edge = heap.pop();
      // If adding the current edge does not create a cycle in the spanning
      // tree, then add it to the tree. We are using a disjoint set data
      // structure to keep track of the forest and whether or not a cycle is
      // created because if the two end vertices are in the same set in the
      // group of disjoint sets, then we have a cycle.
      if (set.getBoss(edge.vertex1) != set.getBoss(edge.vertex2)) {
        tree.add(edge);
        set.union(edge.vertex1, edge.vertex2);
      } else if (edge.isF) {
        // If we excluded an edge but that edge was in F, then a minimum
        // spanning tree is not possible. This is also due to the fact that
        // we sorted all the edges by whether or not they were in F, so if we
        // excluded an edge here, then minimum spanning tree can occur.
        return -1;
      }
    }

    // Sum up the weights of all the edges in the minimum spanning tree.
    // At this point, we also check the bosses of one of the vertexes in each
    // of the included edges, if all the bosses are not the same, then we
    // have a forest instead of a single spanning tree.
    int weight = 0;
    int boss = -1;
    for (int i = 0; i < tree.size(); ++i) {
      Edge edge = tree.get(i);
      weight += edge.weight;
      if (boss == -1) {
        boss = set.getBoss(edge.vertex1);
      } else if (boss != set.getBoss(edge.vertex1)) {
        return -1;
      }
    }
    return weight;
  }

  /**
   * The main function for SpanTree. Reads input from stdin and delegates
   * responsibilities as appropriate.
   *
   * @param args Any arguments to this program from the command-line
   */
  public static void main(String[] args) {
    try (Scanner stdin = new Scanner(System.in)) {
      int n = stdin.nextInt();
      int m = stdin.nextInt();
      // Ooh I'm feeling edgy
      Edge[] edges = new Edge[m];
      stdin.nextLine();
      for (int i = 0; i < m; i++) {
        edges[i] = new Edge(stdin.nextLine().split(" "));
      }
      System.out.println(krusty(n, edges));
    }
  }
}
