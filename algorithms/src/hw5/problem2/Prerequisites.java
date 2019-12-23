import java.util.Scanner;

/**
 * Prerequisites: Given a graph of courses and their prerequisites, identify the
 * longest chain of prerequisites.
 *
 * @author William Leuschner wel2138@rit.edu
 * @author Alvin Lin axl1439@rit.edu
 */
public class Prerequisites {

  /**
   * Given a boolean array representing nodes (index) that have been visited,
   * this method returns the first unvisited node for the topological sort.
   *
   * @param visited The boolean array of visited/unvisited nodes
   * @return The first unvisited node
   */
  private static int getUnvisitedNode(boolean[] visited) {
    for (int i = 0; i < visited.length; ++i) {
      if (!visited[i]) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Returns a topological sort of n nodes in a graph given the graph's
   * representation as an adjacency list. It is assumed that for n nodes, the
   * nodes are identified by their index in the adjacency list.
   *
   * @param n The number of courses
   * @param adjacencyList An adjacency list representation of a course
   * prerequisites graph
   * @return The length of the longest chain in the graph
   */
  private static int[] sortTopological(int n, int[][] adjacencyList) {
    // This deque will function as a linked list to which we store the
    // topologically sorted list of courses.
    Deque<Integer> sorted = new Deque<>();

    // This will hold the state of whether or not we have visited a node in
    // our depth first traversal.
    boolean[] visited = new boolean[n];

    // Select an unvisited node and depth first search from it, repeating
    // until we have visited all the nodes.
    int unvisitedNode = getUnvisitedNode(visited);
    while (unvisitedNode != -1) {
      sortTopologicalVisit(unvisitedNode, adjacencyList, sorted, visited);
      unvisitedNode = getUnvisitedNode(visited);
    }

    // Convert the linked list of topologically sorted courses into an array
    // of course IDs.
    int[] result = new int[n];
    int resultIndex = 0;
    while (!sorted.isEmpty()) {
      result[resultIndex++] = sorted.popFirst();
    }
    return result;
  }

  /**
   * Recursive depth first search that is used by the topological sort.
   */
  private static void sortTopologicalVisit(int node, int[][] adjacencyList,
      Deque<Integer> sorted, boolean[] visited) {
    if (visited[node]) {
      return;
    }
    for (int neighbor : adjacencyList[node]) {
      sortTopologicalVisit(neighbor, adjacencyList, sorted, visited);
    }
    visited[node] = true;
    sorted.pushFirst(node);
  }

  /**
   * Given an adjacency list for a directed graph of courses and their
   * prerequisites, this method calculates the longest chain of prerequisites.
   * @param n The number of courses (Each node in the graph is identified by
   * an integer from 0 to n-1).
   * @param adjacencyList The adjacency list representing the directed graph
   * of course prerequisites.
   * @return The maximum length of all the prerequisite chains.
   */
  private static int prerequisites(int n, int[][] adjacencyList) {
    int[] topSorted = sortTopological(n, adjacencyList);
    int[] prerequisites = new int[n];

    // Using the topologically sorted array of courses, we start from a course
    // that has no upstream dependencies and propagate it to all its
    // prerequisites. We keep moving through the topologically sorted array of
    // courses and propagate each course and its requirement count to all of
    // their prerequisites. This essentially becomes a very dynamic
    // programm-y solution where the solution (S) is defined for S[i] as the
    // longest prerequisite chain starting with course i. S[i] is calculated
    // by taking the max of S[i] and S[upstream] + 1 for each upstream course
    // that requires course i.
    for (int node : topSorted) {
      for (int upstream : adjacencyList[node]) {
        prerequisites[upstream] = Math.max(
            prerequisites[upstream],
            prerequisites[node] + 1);
      }
    }

    // After calculating the solution array, the longest prerequisite chain
    // is simply the largest element in it.
    int maxLength = 0;
    for (int node : topSorted) {
      if (prerequisites[node] > maxLength) {
        maxLength = prerequisites[node];
      }
    }
    return maxLength + 1;
  }

  /**
   * The main function for Prerequisites. Reads input from stdin and delegates
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
        String lineSplit[] = stdin.nextLine().split(" ");
        adjList[i] = new int[lineSplit.length - 1];
        for (int j = 0; j < lineSplit.length - 1; j++) {
          adjList[i][j] = Integer.parseInt(lineSplit[j]) - 1;
        }
      }
      System.out.println(prerequisites(n, adjList));
    }
  }
}
