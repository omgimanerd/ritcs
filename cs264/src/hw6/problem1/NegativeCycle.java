import java.util.Scanner;

/**
 * NegativeCycle: Determine whether a graph with exactly one negative edge has a
 * negative cycle.
 *
 * @author William Leuschner wel2138@rit.edu
 * @author Alvin Lin axl1439@rit.edu
 */
public class NegativeCycle {

  /**
   * Edge: A class to hold a weighted directed edge in a graph.
   */
  private static class Edge {

    int source;
    int destination;
    int weight;

    Edge(int source, int destination, int weight) {
      this.source = source;
      this.destination = destination;
      this.weight = weight;
    }

    public String toString() {
      return this.source + " " + this.destination + " " + this.weight;
    }
  }

  /**
   * Uses the Bellman-Ford algorithm to determine the distances of every node
   * from a single source node, returns null if the graph contains a negative
   * weight cycle.
   *
   * @param edges The list of edges in the graph
   * @param vertices The number of vertices in the graph
   * @param source The source node to run the Bellman-Ford algorithm from
   * @return The array of node distances, null if the graph contains a negative
   * weight cycle.
   */
  private static int[] singleSourceDistances(Edge[] edges, int vertices,
      int source) {
    int distances[] = new int[vertices];
    for (int i = 0; i < vertices; ++i) {
      distances[i] = Integer.MAX_VALUE;
    }
    distances[source] = 0;
    for (int i = 0; i < vertices - 1; ++i) {
      for (Edge edge : edges) {
        if (distances[edge.source] != Integer.MAX_VALUE &&
            distances[edge.destination] >
                distances[edge.source] + edge.weight) {
          distances[edge.destination] = distances[edge.source] + edge.weight;
        }
      }
    }
    for (Edge edge : edges) {
      if (distances[edge.source] != Integer.MAX_VALUE &&
          distances[edge.destination] > distances[edge.source] + edge.weight) {
        return null;
      }
    }
    return distances;
  }

  /**
   * The main function for NegativeCycle. Accepts input from stdin and delegates
   * tasks appropriately.
   *
   * @param args Any command line arguments to this program
   */
  public static void main(String[] args) {
    try (Scanner stdin = new Scanner(System.in)) {
      int n = stdin.nextInt();
      int m = stdin.nextInt();
      Edge[] edges = new Edge[m];
      for (int i = 0; i < m; ++i) {
        edges[i] = new Edge(
            stdin.nextInt() - 1, stdin.nextInt() - 1, stdin.nextInt());
      }
      System.out.println(
          singleSourceDistances(edges, n, 0) != null ? "YES" : "NO");
    }
  }
}
