import java.util.Arrays;
import java.util.Scanner;

/**
 * NetworkConnect: Analyze a network of switches/routers and determine if it
 * is possible to create a new link and increase the bandwidth capacity of
 * the system
 *
 * @author William Leuschner wel2138@rit.edu
 * @author Alvin Lin axl1439@rit.edu
 */
public class NetworkConnect {

  /**
   * A weighted edge in a graph.
   */
  private static class Edge {

    /**
     * The index of the source node
     */
    int source;
    /**
     * The index of the destination node
     */
    int destination;
    /**
     * The weight of this edge
     */
    int weight;

    /**
     * Construct a new edge.
     *
     * @param source The index of the source node
     * @param destination The index of the destination node
     * @param weight The weight of this edge
     */
    Edge(int source, int destination, int weight) {
      this.source = source;
      this.destination = destination;
      this.weight = weight;
    }

    /**
     * Convert this Edge into a string (in the output format required by the
     * course submission guidelines.
     *
     * @return String.format(" % d % d ", this.source, this.destination)
     */
    public String toString() {
      return this.source + " " + this.destination;
    }
  }

  /**
   * Loop over the edges in the edge list and convert them into an adjacency
   * matrix.
   *
   * @param edges A list of {@link Edge}s
   * @param vertices The number of vertices in the graph
   * @return An adjacency matrix representation of the edges in the graph
   */
  private static int[][] getAdjacencyMatrix(Edge[] edges, int vertices) {
    int[][] adjacencyMatrix = new int[vertices][vertices];
    for (Edge edge : edges) {
      adjacencyMatrix[edge.source][edge.destination] = edge.weight;
    }
    return adjacencyMatrix;
  }

  /**
   * Run a breadth-first traversal on the graph in the adjacency matrix.
   * Modifies the predecessor and boolean array passed to it.
   *
   * @param adjacencyMatrix The adjacency matrix to traverse
   * @param vertices The number of vertices in the graph
   * @param predecessors The array of predecessors for each vertex, populated
   * by this function
   * @param visited The array of visited vertices, populated by this function
   * @param source The source node
   */
  private static void bfs(int[][] adjacencyMatrix, int vertices,
      int[] predecessors, boolean[] visited, int source) {

    // Initialize data structures necessary for a breadth first search.
    Deque<Integer> queue = new Deque<>();  // search queue

    // Initialize predecessors array to -1
    if (predecessors.length != vertices) {
      throw new RuntimeException("Invalid length predecessors array!");
    } else if (visited.length != vertices) {
      throw new RuntimeException("Invalid length visited array!");
    }

    // Reset the predecessors and visited array
    for (int i = 0; i < vertices; ++i) {
      predecessors[i] = -1;
      visited[i] = false;
    }

    // Queue the source node
    queue.pushFirst(source);
    visited[source] = true;

    // Breadth first search through the graph.
    while (!queue.isEmpty()) {
      int current = queue.popLast();
      for (int i = 0; i < vertices; ++i) {
        // Traverse the graph if forwardPath is true
        if (adjacencyMatrix[current][i] != 0 && !visited[i]) {
          queue.pushFirst(i);
          visited[i] = true;
          predecessors[i] = current;
        }
      }
    }
  }

  /**
   * Run a breadth-first traversal on the graph transpose in the adjacency
   * matrix. Modifies the predecessor and boolean array passed to it.
   *
   * @param adjacencyMatrix The adjacency matrix to traverse
   * @param vertices The number of vertices in the graph
   * @param predecessors The array of predecessors for each vertex, populated
   * by this function
   * @param visited The array of visited vertices, populated by this function
   * @param sink The sink node
   */
  private static void bfsT(int[][] adjacencyMatrix, int vertices,
      int[] predecessors, boolean[] visited, int sink) {

    // Initialize data structures necessary for a breadth first search.
    Deque<Integer> queue = new Deque<>();  // search queue

    // Initialize predecessors array to -1
    if (predecessors.length != vertices) {
      throw new RuntimeException("Invalid length predecessors array!");
    } else if (visited.length != vertices) {
      throw new RuntimeException("Invalid length visited array!");
    }

    // Reset the predecessors and visited array
    for (int i = 0; i < vertices; ++i) {
      predecessors[i] = -1;
      visited[i] = false;
    }

    // Queue the sink node
    queue.pushFirst(sink);
    visited[sink] = true;

    // Breadth first search through the graph.
    while (!queue.isEmpty()) {
      int current = queue.popLast();
      for (int i = 0; i < vertices; ++i) {
        if (adjacencyMatrix[i][current] != 0 && !visited[i]) {
          queue.pushFirst(i);
          visited[i] = true;
          predecessors[current] = i;
        }
      }
    }
  }

  /**
   * Locate two vertices that could be connected in order to increase the
   * bandwidth between the source and the sink.  This edge will always be
   * across the min st-cut of the graph between source and sink, which we can
   * find using Ford-Fulkerson (actually Edmonds-Karp, since we use BFS).  To
   * resolve ties between equivalent edges, the edges with the lowest vertex
   * indices are used.
   *
   * @param edges An array with all of the edges of the graph to analyze
   * @param vertices The number of vertices in the graph
   * @param source The source node
   * @param sink The sink node
   * @return A new {@link Edge} that could be added to the graph in order to
   * increase the bandwidth, or null if no such edge exists.
   */
  private static Edge getBandwidthBottleneckEdge(Edge[] edges, int vertices,
      int source, int sink) {
    // Convert the edge-list representation of the graph into an adjacency
    // matrix representation
    int[][] adjacencyMatrix = getAdjacencyMatrix(edges, vertices);
    int[][] copy = getAdjacencyMatrix(edges, vertices);

    // Predecessors array that will be modified by bfs().
    int[] predecessors = new int[vertices];
    // Visited array that will be modified by bfs().
    boolean[] visited = new boolean[vertices];

    // Run Edmond-Karp to remove all of the source-to-sink augmenting paths
    // from the graph.
    bfs(adjacencyMatrix, vertices, predecessors, visited, source);
    // visited is re-calculated at the end of the loop, so this loop runs
    // while there exist paths from the source to the sink
    while (visited[sink]) {
      // Find the maximum flow that can go through this path, equal to the
      // minimum capacity along all the paths.  We don't actually care about
      // the value of the min cut, we just want to find its location so we
      // can draw a new edge across it.  Consequently, we don't accumulate
      // minFlow.
      int minFlow = Integer.MAX_VALUE;
      int currentNode = sink;

      // Travel backwards along this augmenting path and find the minimum
      // capacity along it
      while (currentNode != source) {
        int predecessor = predecessors[currentNode];
        minFlow = Math.min(minFlow, adjacencyMatrix[predecessor][currentNode]);
        currentNode = predecessor;
      }

      // Traverse the same path again, this time reducing the capacity of
      // each edge by the minimum capacity, and building backwards edges
      currentNode = sink;
      while (currentNode != source) {
        int predecessor = predecessors[currentNode];
        adjacencyMatrix[predecessor][currentNode] -= minFlow;
        // Create a reverse path
        adjacencyMatrix[currentNode][predecessor] += minFlow;
        currentNode = predecessor;
      }

      // Recalculate the predecessors list to see if there are any more paths
      // to the sink node.
      bfs(adjacencyMatrix, vertices, predecessors, visited, source);
    }

    // Now we do a breadth-first search of the transpose of the graph (taking
    // only nodes that point to the destination node).
    bfsT(adjacencyMatrix, vertices, predecessors, visited, sink);

    // Find the lexicographically lowest node from the sink that we can point
    // the source to.
    Heap<Integer> heap = new Heap<>(Heap.Type.MIN, Integer::compareTo);
    for (int v : predecessors) {
      if (v != -1) {
        heap.add(v);
      }
    }

    // See which node we can connect the source to.
    while (!heap.isEmpty()) {
      int connector = heap.pop();
      // Skip if the edge we are proposing existed in the original graph.
      if (copy[source][connector] == 0) {
        // Create a new @link Edge} with the proper source and destination,
        // and 0 weight since that's not part of the answer we're looking for.
        return new Edge(source + 1, connector + 1, 0);
      }
    }

    // If we failed to find an edge, return null.
    return null;
  }

  /**
   * The main function for NetworkConnect. Accepts input from stdin and
   * delegates tasks appropriately.
   *
   * @param args Any command line arguments to this program
   */
  public static void main(String[] args) {
    try (Scanner stdin = new Scanner(System.in)) {
      // Scan the number of nodes
      int n = stdin.nextInt();
      // Scan the number of edges
      int m = stdin.nextInt();
      // Scan the source node
      int server = stdin.nextInt() - 1;
      // Scan the sink node
      int user = stdin.nextInt() - 1;
      // Scan the edges
      Edge[] edges = new Edge[m];
      for (int i = 0; i < m; ++i) {
        edges[i] = new Edge(
            stdin.nextInt() - 1, stdin.nextInt() - 1, stdin.nextInt());
      }
      // Locate an additional edge
      Edge bottleneckEdge = getBandwidthBottleneckEdge(edges, n, server, user);
      // Print it out, or print "NO" if one could not be found
      System.out.println(bottleneckEdge != null ? bottleneckEdge : "NO");
    }
  }
}
