import java.util.Scanner;

/**
 * NumPaths: Calculate the total number of shortest paths between two nodes in a
 * graph.
 *
 * @author William Leuschner wel2138@rit.edu
 * @author Alvin Lin axl1439@rit.edu
 */
public class NumPaths {

  /**
   * Convert the edge-list representation of a graph to an adjacency-list
   * representation.
   *
   * @param n The number of nodes
   * @param edges The list of edges
   * @return An adjacency-list representation of <code>edges</code>
   */
  private static int[][] edgesToAdjacency(int n, int[][] edges) {
    // Adjacency list
    int adjList[][] = new int[n][];
    // The size of each row in the adjacency list
    int adjSize[] = new int[n];
    // The next index into the adjacency list
    int nextIdx[] = new int[n];

    // Loop over all of the edges to count how many entries there will need
    // to be in their adjacency lists. We could use an arraylist, but that's
    // kinda slow and not linear in memory.
    for (int edge[] : edges) {
      adjSize[edge[0] - 1] += 1;
      adjSize[edge[1] - 1] += 1;
    }

    // Now, loop over every entry in the sizes array and use its value to
    // create an array of the proper size in the matrix
    for (int i = 0; i < n; i++) {
      adjList[i] = new int[adjSize[i]];
    }

    // Finally, loop over every edge again and
    for (int[] edge : edges) {
      // pull out the current edge for convenience
      // Set the next free entry in the adjacency list for the start node of
      // the edge to the end node for that edge (minus 1 to fix obo errors)
      adjList[edge[0] - 1][nextIdx[edge[0] - 1]] = edge[1] - 1;
      // Increment the next index counter for the start node
      nextIdx[edge[0] - 1] += 1;
      // Set the next free entry in the adjacency list for the end node of
      // the edge to the start node for that edge (minus 1 to fix obo errors)
      adjList[edge[1] - 1][nextIdx[edge[1] - 1]] = edge[0] - 1;
      // Increment the next index counter for the end node
      nextIdx[edge[1] - 1] += 1;
    }

    return adjList;
  }

  /**
   * Find the number of shortest paths between s and t in the graph edges.
   *
   * @param n The number of nodes in the graph
   * @param s The starting node
   * @param t The ending node
   * @param edges The graph in which to search
   * @return The number of shortest paths from s to t
   */
  private static int findNumPaths(int n, int s, int t, int[][] edges) {
    int adjacencyList[][] = edgesToAdjacency(n, edges);

    // Nodes will be represented using their node ID.
    Deque<Integer> queue = new Deque<>();
    boolean visited[] = new boolean[n];

    // This is sort of a dynamic programm-y solution, since the only thing we
    // care is the number of shortest length paths from node s to node t. If our
    // solution array is S, then S[i] represents the number of shortest
    // length paths from node s to node i.
    int[] pathMultiplicities = new int[n];

    queue.pushLast(s);
    visited[s] = true;
    pathMultiplicities[s] = 1;

    // We will use a breadth first search style algorithm to populate the
    // path multiplicities array.
    while (!queue.isEmpty()) {
      int queueSize = queue.size();
      boolean visitedDuringCurrentBatch[] = new boolean[n];

      // We batch the current elements in the queue so that we can iterate
      // through them without counting the elements that these nodes will to
      // the queue.
      for (int i = 0; i < queueSize; ++i) {
        int nodeId = queue.popFirst();

        // For every neighbor to the current node, if we've visited that
        // neighbor before, then we ignore it unless we visited that neighbor
        // during this batch of nodes. If we've visited that neighbor during
        // this batch of nodes, then the number of shortest paths to that
        // neighbor is incremented by the number of shortest paths to the
        // current node.
        // If we haven't visited this neighbor before, then we add the
        // neighbor to the queue, set its path multiplicity equal to the
        // number of shortest paths to the current node. We then mark the
        // node as visited, while remembering that it was visited during this
        // batch of nodes to be processed.
        for (int neighbor : adjacencyList[nodeId]) {
          if (!visited[neighbor]) {
            queue.pushLast(neighbor);
            pathMultiplicities[neighbor] = pathMultiplicities[nodeId];
            visited[neighbor] = true;
            visitedDuringCurrentBatch[neighbor] = true;
          } else if (visitedDuringCurrentBatch[neighbor]) {
            pathMultiplicities[neighbor] += pathMultiplicities[nodeId];
          }
        }
      }
    }

    return pathMultiplicities[t];
  }

  /**
   * The main function for NumPaths. Reads input from stdin and delegates
   * responsibilities as appropriate.
   *
   * @param args Any arguments to this program from the command-line
   */
  public static void main(String[] args) {
    try (Scanner stdin = new Scanner(System.in)) {
      // Read in the four constants at the top of the input
      int n = stdin.nextInt();
      int m = stdin.nextInt();
      // Minus 1 to fix obo error
      int s = stdin.nextInt() - 1;
      int t = stdin.nextInt() - 1;

      // Loop over the rest of the input and read in the edges
      int edges[][] = new int[m][2];
      for (int i = 0; i < m; i++) {
        edges[i] = new int[2];
        edges[i][0] = stdin.nextInt();
        edges[i][1] = stdin.nextInt();
      }

      System.out.println(findNumPaths(n, s, t, edges));
    }
  }
}
