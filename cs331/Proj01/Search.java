import java.io.*;
import java.util.*;

/**
 * Search class containing code for depth first search, breadth first search,
 * and A* search for CSCI 331: Intro to Intelligent Systems.
 * @author axl1439 (Alvin Lin)
 */
public class Search {

  /**
   * Private inner class used to parse the input file passed in from the command
   * line arguments to determine the start and destination cities.
   */
  private static final class CityTuple {
    public String start;
    public String destination;

    public CityTuple(String start, String destination) {
      this.start = start;
      this.destination = destination;
    }

    public static CityTuple getFromFile(String filename) {
      try {
        Scanner sc = filename.equals("-") ?
          new Scanner(System.in) : new Scanner(new File(filename));
        return new CityTuple(sc.nextLine(), sc.nextLine());
      } catch (FileNotFoundException e) {
        System.err.println("File not found: " + filename);
        System.exit(0);
      }
      throw new RuntimeException("This should never happen!");
    }

    public void validate(Set<String> cities) {
      if (!cities.contains(this.start)) {
        System.err.println(String.format("No such city: %s", this.start));
        System.exit(0);
      }
      if (!cities.contains(this.destination)) {
        System.err.println(String.format("No such city: %s", this.destination));
        System.exit(0);
      }
    }
  }

  public static final String CITY_FILE = "city.dat";
  public static final String EDGE_FILE = "edge.dat";

  /**
   * Reads in data from the hardcoded file and returns a HashMap of all the
   * cities and their respective locations.
   * @return a HashMap of the cities and their coordinate locations.
   */
  private static HashMap<String, Coordinate> getCities() {
    HashMap<String, Coordinate> cities = new HashMap<>();
    try {
      File file = new File(CITY_FILE);
      Scanner sc = new Scanner(file);
      while (sc.hasNextLine()) {
        String[] data = sc.nextLine().split("\\s+");
        if (data.length != 4) {
          continue;
        }
        cities.put(data[0], new Coordinate(
        Double.parseDouble(data[2]), Double.parseDouble(data[3])));
      }
    } catch (FileNotFoundException e) {
      System.err.println(String.format("File not found: %s", CITY_FILE));
      System.exit(0);
    }
    return cities;
  }

  /**
   * Reads in data from the hardcoded file and returns a list of all the
   * cities that are connected.
   * @return an ArrayList containing all connected cities
   */
  private static ArrayList<String[]> getEdges() {
    ArrayList<String[]> edges = new ArrayList<>();
    File file = new File(EDGE_FILE);
    try {
      Scanner sc = new Scanner(file);
      while (sc.hasNextLine()) {
        String[] data = sc.nextLine().split("\\s+");
        if (data.length != 2) {
          continue;
        }
        edges.add(data);
      }
    } catch (FileNotFoundException e) {
      System.err.println(String.format("File not found: %s", EDGE_FILE));
      System.exit(0);
    }
    return edges;
  }

  /**
   * Takes a list of cities and list of connecting edges and returns an
   * adjacency list in the form of a HashMap of HashMaps.
   * @param cities a list of cities and their locations
   * @param edges a list of connections between cities
   * @return an adjacency list representing the resulting graph
   */
  private static HashMap<String, TreeMap<String, Double>> getAdjacencyMatrix(
      HashMap<String, Coordinate> cities, ArrayList<String[]> edges) {
    HashMap<String, TreeMap<String, Double>> adjacencyMatrix = new HashMap<>();
    for (String[] edge : edges) {
      double distance = cities.get(edge[0]).getDistance(cities.get(edge[1]));
      adjacencyMatrix.putIfAbsent(edge[0], new TreeMap<>());
      adjacencyMatrix.putIfAbsent(edge[1], new TreeMap<>());
      adjacencyMatrix.get(edge[0]).put(edge[1], distance);
      adjacencyMatrix.get(edge[1]).put(edge[0], distance);
    }
    return adjacencyMatrix;
  }

  /**
   * Given a start city, destination city, and a predecessor list, this method
   * reconstructs the solution path from the start city to the destination
   * city.
   * @param predecessors the map of predecessors
   * @param start the start city
   * @param destination the destination
   * @return an ArrayList of cities forming a path from start to destination
   */
  private static ArrayList<String> reconstructSolution(
      HashMap<String, String> predecessors,
      String start, String destination) {
    ArrayList<String> path = new ArrayList<>();
    String current = destination;
    while (current != start) {
      path.add(0, current);
      current = predecessors.get(current);
    }
    path.add(0, current);
    return path;
  }

  /**
   * Given a graph as an adjacency matrix, a start city, and a destination
   * city, this method returns a path of cities from start to destination using
   * breadth first search.
   * @param adjacencyMatrix the graph as an adjacency matrix
   * @param start the start city
   * @param destination the destination city
   * @return an ArrayList of cities forming a path from start to destination
   */
  public static ArrayList<String> bfs(
      HashMap<String, TreeMap<String, Double>> adjacencyMatrix,
      String start, String destination) {
    Deque<String> queue = new Deque<>();
    HashMap<String, Boolean> visited = new HashMap<>();
    HashMap<String, String> predecessors = new HashMap<>();

    queue.pushFirst(start);
    visited.put(start, true);
    while (!queue.isEmpty()) {
      String current = queue.popLast();
      ArrayList<String> neighbors = new ArrayList<String>(
        adjacencyMatrix.get(current).keySet());
      for (String neighbor : neighbors) {
        if (!visited.getOrDefault(neighbor, false)) {
          predecessors.put(neighbor, current);
          queue.pushFirst(neighbor);
          visited.put(neighbor, true);
        }
        if (neighbor.equals(destination)) {
          queue.clear();
          break;
        }
      }
    }
    return reconstructSolution(predecessors, start, destination);
  }

  /**
   * Given a graph as an adjacency matrix, a start city, and a destination
   * city, this method returns a path of cities from start to destination using
   * depth first search.
   * @param adjacencyMatrix the graph as an adjacency matrix
   * @param start the start city
   * @param destination the destination city
   * @return an ArrayList of cities forming a path from start to destination
   */
  public static ArrayList<String> dfs(
      HashMap<String, TreeMap<String, Double>> adjacencyMatrix,
      String start, String destination) {
    Deque<String> stack = new Deque<>();
    HashMap<String, Boolean> visited = new HashMap<>();
    HashMap<String, String> predecessors = new HashMap<>();

    stack.pushFirst(start);
    visited.put(start, true);
    while (!stack.isEmpty()) {
      String current = stack.popFirst();
      ArrayList<String> neighbors = new ArrayList<String>(
        adjacencyMatrix.get(current).keySet());
      // Reverse the ArrayList of neighbors here because we want the cities
      // in descending name order according to the project spec.
      Collections.reverse(neighbors);
      for (String neighbor : neighbors) {
        if (!visited.getOrDefault(neighbor, false)) {
          predecessors.put(neighbor, current);
          stack.pushFirst(neighbor);
          visited.put(neighbor, true);
        }
        if (neighbor.equals(destination)) {
          stack.clear();
          break;
        }
      }
    }
    return reconstructSolution(predecessors, start, destination);
  }

  /**
   * Given a graph as an adjacency matrix, the coordinates of each city, a
   * start city, and a destination city, this method returns a path of cities
   * from start to destination using A* search. This search uses the heuristic
   * f(n) = distance traveled + distance to destination
   * @param adjacencyMatrix the graph as an adjacency matrix
   * @param cities a HashMap of city names to their coordinates
   * @param start the start city
   * @param destination the destination city
   * @return an ArrayList of cities forming a path from start to destination
   */
  public static ArrayList<String> aStar(
      HashMap<String, TreeMap<String, Double>> adjacencyMatrix,
      HashMap<String, Coordinate> cities,
      String start, String destination) {

    /**
     * Local inner class encapsulating a node and its distance heuristic for
     * the A* search algorithm.
     */
    class AStarNode implements Comparable<AStarNode> {
      public String city;
      public double f;

      public AStarNode(String city, double f) {
        this.city = city;
        this.f = f;
      }

      public int compareTo(AStarNode other) {
        return (int) (this.f - other.f);
      }
    }

    Heap<AStarNode> heap = new Heap<>(Heap.Type.MIN);
    HashMap<String, Boolean> visited = new HashMap<>();
    HashMap<String, String> predecessors = new HashMap<>();
    Coordinate destinationCoord = cities.get(destination);

    heap.push(new AStarNode(start, 0));
    visited.put(start, true);
    while (!heap.isEmpty()) {
      AStarNode current = heap.pop();
      ArrayList<String> neighbors = new ArrayList<String>(
        adjacencyMatrix.get(current.city).keySet());
      for (String neighbor : neighbors) {
        if (!visited.getOrDefault(neighbor, false)) {
          predecessors.put(neighbor, current.city);
          double distance = cities.get(neighbor).getDistance(destinationCoord);
          heap.push(new AStarNode(neighbor, current.f + distance));
          visited.put(neighbor, true);
        }
        if (neighbor.equals(destination)) {
          heap.clear();
          break;
        }
      }
    }
    return reconstructSolution(predecessors, start, destination);
  }

  /**
   * Generates the output according to the project spec from the solution path.
   * @param solution the solution path of cities
   * @param adjacencyMatrix the adjacency matrix for distance calculation
   * @return the output according to the project spec
   */
  public static String getSolutionOutput(
      ArrayList<String> solution,
      HashMap<String, TreeMap<String, Double>> adjacencyMatrix) {
    double totalDistance = 0;
    String last = null;
    StringBuilder output = new StringBuilder();
    for (String city : solution) {
      output.append(city);
      output.append("\n");
      if (last != null) {
        totalDistance += adjacencyMatrix.get(last).get(city);
      }
      last = city;
    }
    output.append(
      String.format("That took %d hops to find.\n", solution.size() - 1));
    output.append(
      String.format("Total distance = %d miles.", Math.round(totalDistance)));
    return output.toString();
  }

  /**
   * Main function
   */
  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Usage: java Search inputFile outputFile");
      System.exit(0);
    }

    // Fetch the input and build the graph representing the problem.
    CityTuple parameters = CityTuple.getFromFile(args[0]);
    HashMap<String, Coordinate> cities = getCities();
    parameters.validate(cities.keySet());
    String start = parameters.start;
    String destination = parameters.destination;
    ArrayList<String[]> edges = getEdges();
    HashMap<String, TreeMap<String, Double>> adjacencyMatrix =
      getAdjacencyMatrix(cities, edges);
    StringBuilder output = new StringBuilder();

    // Execute BFS on the graph
    ArrayList<String> solution = bfs(adjacencyMatrix, start, destination);
    output.append("\nBreadth-First Search Results: \n");
    output.append(getSolutionOutput(solution, adjacencyMatrix));
    output.append("\n\n");

    // Execute DFS on the graph
    solution = dfs(adjacencyMatrix, start, destination);
    output.append("\nDepth-First Search Results: \n");
    output.append(getSolutionOutput(solution, adjacencyMatrix));
    output.append("\n\n");

    // Execute A* on the graph
    solution = aStar(adjacencyMatrix, cities, start, destination);
    output.append("\nA* Search Results: \n");
    output.append(getSolutionOutput(solution, adjacencyMatrix));
    output.append("\n");

    String filename = args[1];
    try {
      PrintWriter pw = filename.equals("-") ?
        new PrintWriter(System.out) : new PrintWriter(new File(filename));
      pw.println(output.toString());
      pw.close();
    } catch (FileNotFoundException e) {
      System.err.println(String.format("Could not write to %s", filename));
      System.exit(0);
    }
  }
}
