import java.util.Arrays;
import java.util.Scanner;

/**
 * OneWay: Given an unconnected, directed graph, identify if it is possible to
 * add one edge and make it connected.
 *
 * @author William Leuschner wel2138@rit.edu
 * @author Alvin Lin axl1439@rit.edu
 */
public class OneWay {

  /**
   * Compute the transpose of the graph. The transpose of a directed graph G is
   * identical to G, but with every edge reversed.
   *
   * @param graph The graph to transpose (as an adjacency list)
   * @return <code>graph</code>, transposed
   * @time O(2m + n)
   */
  public static int[][] transpose(int[][] graph) {
    // Create the empty transposed graph
    int graphT[][] = new int[graph.length][];
    // Storage space to hold how large each neighbor list must be in the
    // transposed graph
    int neighborSizes[] = new int[graph.length];
    // Record the index where the next neighbor should go in each transposed
    // neighbor array
    int nextNeighbor[] = new int[graph.length];

    // Loop over every node
    for (int i = 0; i < graph.length; i++) {
      // Pull out its neighbor list
      int neighbors[] = graph[i];
      // For each of its neighbors,
      for (int j = 0; j < neighbors.length; j++) {
        // Add one to the neighbor count for that neighbor (j has one more
        // neighbor: i)
        neighborSizes[graph[i][j]]++;
      }
    }

    // Create the empty neighbor lists in the transposed graph
    for (int i = 0; i < graph.length; i++) {
      graphT[i] = new int[neighborSizes[i]];
    }

    // Loop over every node again
    for (int i = 0; i < graph.length; i++) {
      // Pull out its un-transposed neighbor list
      int neighbors[] = graph[i];
      // For each of its neighbors,
      for (int j = 0; j < neighbors.length; j++) {
        // Add i as the new neighbor in the next available neighbor slot
        graphT[graph[i][j]][nextNeighbor[graph[i][j]]] = i;
        // Move the pointer to the next neighbor slot one position forward
        nextNeighbor[graph[i][j]]++;
      }
    }

    return graphT;
  }

  /**
   * Run a Depth-First Traversal of the graph in <code>graph</code>. Mark
   * nodes as "seen" in the provided boolean array, and add all of the nodes
   * that were found to the ArrayList called <code>found</code>.
   *
   * @param startNode The beginning node to examine
   * @param graph The adjacency list format graph to traverse
   * @param found The ArrayList in which to put "found" nodes
   * @param seen The array of boolean values to determine if a node has been
   *        seen already
   */
  public static void dfs(int startNode, int[][] graph, ArrayList<Integer> found,
      boolean[] seen) {
    if (seen[startNode])
      return;
    seen[startNode] = true;
    for (int neighbor : graph[startNode]) {
      dfs(neighbor, graph, found, seen);
    }
    found.add(startNode);
  }

  /**
   * Topologically sort the graph.
   *
   * @param graph The graph to sort
   * @return A topologically-sorted ordering of the graph
   */
  public static ArrayList<Integer> topologicalSort(int[][] graph) {
    // The ArrayList of topologically-sorted nodes
    ArrayList<Integer> sorted = new ArrayList<>();
    // Array for seen status of each vertex
    boolean seen[] = new boolean[graph.length];
    // DFS from every node
    for (int i = 0; i < graph.length; i++) {
      if (!seen[i]) {
        ArrayList<Integer> found = new ArrayList<>();
        dfs(i, graph, found, seen);
        for (int j = 0; j < found.size(); j++) {
          sorted.add(found.get(j));
        }
      }
    }

    return sorted;
  }

  /**
   * Identify the strongly-connected components of the directed graph
   * <code>graph</code>.
   *
   * @param graph The graph to analyze
   * @param hparg The transpose of that graph (get it, its backwards)
   * @param sorted A topologically-sorted ordering of the nodes in the graph
   * @return The strongly-connected components of <code>graph</code>
   */
  public static ArrayList<ArrayList<Integer>> scc(int[][] graph, int[][]
      hparg, ArrayList<Integer> sorted){
    // ArrayList of strongly-connected components
    ArrayList<ArrayList<Integer>> components = new ArrayList<>();
    // Array for seen status of each vertex
    boolean seen[] = new boolean[graph.length];

    // Find the components
    for (int i = graph.length - 1; i >= 0 ; i--) {
      int node = sorted.get(i);
      if (!seen[node]) {
        ArrayList<Integer> found = new ArrayList<>();
        dfs(node, hparg, found, seen);
        components.add(found);
      }
    }
    return components;
  }

  /**
   * Identify the strongly-connected components of the directed graph
   * <code>graph</code>, but use the topologically sorted ordering in the
   * other direction from {@link OneWay#scc(int[][], int[][], ArrayList)}.
   *
   * @param graph The graph to analyze
   * @param hparg The transpose of that graph (get it, its backwards)
   * @param sorted A topologically-sorted ordering of the nodes in the graph
   * @return The strongly-connected components of <code>graph</code>
   */
  public static ArrayList<ArrayList<Integer>> sccBackwards(int[][] graph,
      int[][] hparg, ArrayList<Integer> sorted){
    // ArrayList of strongly-connected components
    ArrayList<ArrayList<Integer>> components = new ArrayList<>();
    // Array for seen status of each vertex
    boolean seen[] = new boolean[graph.length];

    // Find the components
    for (int i = 0; i < sorted.size(); i++) {
      int node = sorted.get(i);
      if (!seen[node]) {
        ArrayList<Integer> found = new ArrayList<>();
        dfs(node, hparg, found, seen);
        components.add(found);
      }
    }
    return components;
  }

  /**
   * Using the IDs of the strongly-connected components in
   * <code>components</code>, contract the connected components into single
   * vertices. The resulting graph will be an ArrayList of ArrayLists, where the
   * neighbor lists for nodes that have been contracted out are null.
   * Connected components will be contracted down into the first node in each
   * component.
   *
   * @param graph The graph to contract
   * @param graphTranspose The transpose of the graph
   * @param components The strongly-connected components of <code>graph</code>
   * @return A similar graph where all strongly connected components have
   *         been contracted into single nodes
   * @time O(bad)
   */
  @Deprecated
  public static ArrayList<ArrayList<Integer>> contractGraph(int[][] graph,
      ArrayList<ArrayList<Integer>> components, int[][] graphTranspose) {
    // Create the data structure for the contracted graph
    ArrayList<ArrayList<Integer>> contractedGraph = new
        ArrayList<>(graph.length);

    // Copy the graph into the contracted graph, which we can then modify.
    for (int i = 0; i < graph.length; i++) {
      // Create an ArrayList of neighbors, empty
      ArrayList<Integer> n = new ArrayList<>(graph[i].length);
      // Add that ArrayList to the contracted graph
      contractedGraph.add(n);
      // Add each of the neighbors of node i in the regular graph to the
      // contracted version
      for (int j = 0; j < graph[i].length; j++) {
        n.add(graph[i][j]);
      }
    }

    // Loop over each strongly-connected component
    for (int i = 0; i < components.size(); i++) {
      // Pull out the component
      ArrayList<Integer> component = components.get(i);
      // Pull out the primary node, the node we will be contracting to
      int primary = component.get(0);
      // Loop over the rest of the nodes in the component
      for (int j = 1; j < component.size(); j++) {
        // Pull out the id of the node we're contracting out
        int contractee = component.get(j);
        // Loop over the outgoing neighbors of the contractee node
        ArrayList<Integer> neighbors = contractedGraph.get(contractee);
        for (int k = 0; k < neighbors.size(); k++) {
          int outNeighbor = neighbors.get(k);
          // Pull out the list of neighbors for this outgoing neighbor
          ArrayList<Integer> outNeighborNeighborsList = contractedGraph.get
              (outNeighbor);
          // Loop over the incoming neighbors of the contractee node
          for (int inNeighbor: graphTranspose[contractee]) {
            // Add that incoming neighbor to the outgoing neighbor's neighbor
            // list
            outNeighborNeighborsList.add(inNeighbor);
          }
        }
        // Remove the contracted node from the graph
        contractedGraph.set(contractee, null);
      }
    }
    return contractedGraph;
  }

  /**
   * The main function for OneWay. Reads input from stdin and delegates
   * responsibilities as appropriate.
   *
   * @param args Any arguments to this program from the command-line
   */
  public static void main(String[] args) {
    try (Scanner stdin = new Scanner(System.in)) {
      int n = stdin.nextInt();
      // Skip past the newline after that integer
      stdin.nextLine();
      int adjList[][] = new int[n][];
      for (int i = 0; i < n; i++) {
        String line = stdin.nextLine();
        String lineSplit[] = line.split(" ");
        adjList[i] = new int[lineSplit.length - 1];
        for (int j = 0; j < lineSplit.length - 1; j++) {
          adjList[i][j] = Integer.parseInt(lineSplit[j]) - 1;
        }
      }
      int adjListTranspose[][] = transpose(adjList);
      ArrayList<Integer> sorted = topologicalSort(adjList);
      ArrayList<ArrayList<Integer>> components = scc(adjList,
          adjListTranspose, sorted);
      ArrayList<ArrayList<Integer>> otherComponents = sccBackwards(adjList,
          adjListTranspose, sorted);
      // This doesn't really work, but we couldn't figure out how to fix it.
      if (otherComponents.size() > 1) {
        System.out.println("NO");
      } else {
        System.out.println("YES");
        System.out.println("Nodes unknown :(");
      }
    }
  }
}
