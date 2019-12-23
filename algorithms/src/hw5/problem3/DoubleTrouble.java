import java.util.Scanner;

/**
 * DoubleTrouble: Help Thing One (Zero) and Thing Two (One) escape from the
 * house they're trapped in, given their Seussian movement rules.
 *
 * @author William Leuschner wel2138@rit.edu
 * @author Alvin Lin axl1439@rit.edu
 */
public class DoubleTrouble {

  // Valid directions for the Things to move
  private static final int[][] directions = {{1, 0}, {0, -1}, {0, 1}, {-1, 0}};
  public static final char WALL = 'x';
  public static final char FLOOR = '.';
  public static final char THING_0 = '1';
  public static final char THING_1 = '2';

  /**
   * A node in this tree of configurations
   */
  private static class ConfigNode {

    // The locations of Thing 0 and Thing 1
    private final int[][] config;
    // This node's children
    private final ConfigNode[] children;
    // The index where the next child should be inserted. Badly named.
    private int numChildren;

    public ConfigNode(int[][] config) {
      this.config = config;
      // Configurations can only ever have 4 children because there are only
      // 4 directions that the Things can move
      this.children = new ConfigNode[4];
      this.numChildren = -1;
    }

    /**
     * Compare two ConfigNodes for equality. They are considered equal when
     * their configurations are the same.
     *
     * @param o The other object to compare this to
     * @return A boolean
     */
    @Override
    public boolean equals(Object o) {
      if (o instanceof ConfigNode) {
        ConfigNode other = (ConfigNode)o;
        int[][] ocfg = other.getConfig();
        // Other thing 0 row
        int ot0r = ocfg[0][0];
        // Other thing 1 column
        int ot0c = ocfg[0][1];
        // etc.
        int ot1r = ocfg[1][0];
        int ot1c = ocfg[1][1];

        int[][] mcfg = this.getConfig();
        // My thing 0 row
        int mt0r = ocfg[0][0];
        // My thing 1 column
        int mt0c = ocfg[0][1];
        // etc.
        int mt1r = ocfg[1][0];
        int mt1c = ocfg[1][1];

        return (ot0r == mt0r && ot0c == mt0c && ot1r == mt1r && ot1c == mt1c);
      } else
        return false;
    }

    /**
     * Get this node's configuration
     * @return This node's configuration
     */
    public int[][] getConfig() {
      return config;
    }

    /**
     * Get this node's array of children.
     * @return An array of ConfigNodes containing up to 4 non-null elements
     */
    public ConfigNode[] getChildren() {
      return children;
    }

    /**
     * Get the index of the next child
     * @return The index into the Children array where the next child will go
     */
    public int howManyChildren() {
      return numChildren;
    }

    /**
     * Add a new child, skipping if it's already there
     * @param newChild The new child to add
     */
    public void addChild(ConfigNode newChild) {
      for (int i = 0; i < 4; i++) {
        if (newChild.equals(children[i]))
          return;
      }
      if (numChildren < 4) {
        children[++numChildren] = newChild;
      } else {
        throw new ArrayIndexOutOfBoundsException("No room for more kids!");
      }
    }

    /**
     * Is this node's configuration a winning one?
     *
     * @return True if yes, false if no.
     */
    public boolean isWinning(int vertMax, int horizMax) {
      // if (this.isCharlieSheen)
      //   return true;
      int[] maybet0 = this.config[0];
      int[] maybet1 = this.config[1];
      // A winning configuration is one where both Things are in the same
      // row or same column, and that row/column is the first or last one.
      if (maybet0[0] == maybet1[0] && (maybet0[0] == 0 || maybet0[0] ==
          vertMax))
        return true;
      if (maybet0[1] == maybet1[1] && (maybet0[1] == 0 || maybet0[1] ==
          horizMax))
        return true;
      return false;
    }

//    /**
//     * Is this configuration valid? (Can the Things actually be standing there?)
//     *
//     * @param matrix The matrix of values
//     * @return True if the Things are not in a wall, false otherwise.
//     */
//    public boolean isValid(char[][] matrix) {
//      int[] t0 = config[0];
//      int[] t1 = config[1];
//
//      if (this.isOutside(matrix.length - 1, matrix[0].length - 1)) {
//        return true;
//      }
//      return (matrix[t0[0]][t0[1]] != WALL && matrix[t1[0]][t1[1]] != WALL);
//    }

    /**
     * String-ify this configuration as a pair of cartesian coordinates
     * @return (thing0row, thing0col)(thing1row, thing1col)
     */
    public String toString() {
      return String.format("(%d, %d)(%d, %d)", config[0][0],
          config[0][1], config[1][0], config[1][1]);
    }

    /**
     * Return a string representation of this configuration where the Things
     * are put onto the board
     *
     * @param matrix The board
     * @return The board, with the Things in the right spots
     */
    public String toString(char[][] matrix) {
      int[] t0 = config[0];
      int[] t1 = config[1];

      matrix[t0[0]][t0[1]] = THING_0;
      matrix[t1[0]][t1[1]] = THING_1;
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[0].length; j++) {
          sb.append(matrix[i][j]);
        }
        sb.append('\n');
      }
      sb.append('\n');

      matrix[t0[0]][t0[1]] = FLOOR;
      matrix[t1[0]][t1[1]] = FLOOR;

      return sb.toString();
    }
  }

  /**
   * If a configuration node has been generated before, return that one.
   * Otherwise, return the one that was given.
   *
   * @param newNode The node to find, if it's already been created once
   * @param seen The seen matrix to look in
   * @return Either the previously-found node or newNode
   */
  private static ConfigNode getSetSeen(ConfigNode newNode, ConfigNode[][][][]
      seen) {
    int[][] config = newNode.getConfig();
    int t0r = config[0][0];
    int t0c = config[0][1];
    int t1r = config[1][0];
    int t1c = config[1][1];

    ConfigNode existing = seen[t0r][t0c][t1r][t1c];
    if (existing != null) {
      return existing;
    } else {
      seen[t0r][t0c][t1r][t1c] = newNode;
      return newNode;
    }
  }

  /**
   * Read a Visited array
   *
   * @param aNode The node to visit
   * @param visited A matrix of nodes that have/have not been visited
   * @return True if this node has been visited before
   */
  private static boolean getVisited(ConfigNode aNode, boolean[][][][] visited) {
    int[][] config = aNode.getConfig();
    int t0r = config[0][0];
    int t0c = config[0][1];
    int t1r = config[1][0];
    int t1c = config[1][1];

    return visited[t0r][t0c][t1r][t1c];
  }

  /**
   * Modify a Visited array
   *
   * @param aNode The node to visit
   * @param visited A matrix of nodes that have/have not been visited
   */
  private static void setVisited(ConfigNode aNode, boolean[][][][] visited) {
    int[][] config = aNode.getConfig();
    int t0r = config[0][0];
    int t0c = config[0][1];
    int t1r = config[1][0];
    int t1c = config[1][1];

    visited[t0r][t0c][t1r][t1c] = true;
  }

  /**
   * Generate all of the legitimate next-step configs given the matrix and the
   * positions of things 0 and 1.
   *
   * @param current The current ConfigNode
   * @param matrix The matrix/house that they're in
   * @param seen A matrix of ConfigNodes. Used to look up existing nodes when
   * multiple paths generate the same position
   * @return An ArrayList of all of the valid next moves, where each move is an
   * int array with two elements, each a pair of coordinates for the Thing with
   * the same number as that element's index.
   * @pre {@param matrix} is rectangular.
   */
  private static ArrayList<ConfigNode> generateConfigs(ConfigNode current,
      char[][] matrix, ConfigNode[][][][] seen) {
    // Throughout this method, 0 is row (y) and 1 is col (x).
    ArrayList<ConfigNode> configs = new ArrayList<>();

    int thing0[] = current.getConfig()[0];
    int thing1[] = current.getConfig()[1];

    int vertMax = matrix.length - 1;
    int horizMax = matrix[0].length - 1;

    for (int[] direction : directions) {
      int maybet0[] = new int[2];
      int maybet1[] = new int[2];

      // Calculate the potential new coordinates for the things
      maybet0[0] = thing0[0] + direction[0];
      maybet0[1] = thing0[1] + direction[1];
      maybet1[0] = thing1[0] + direction[0];
      maybet1[1] = thing1[1] + direction[1];

      // Revision: no longer generates configurations that are outside the
      // grid; just stops when they're both at an edge

      // Conditions to be handled

      // Conditions which do not generate a config:
      // * Both Things are vertically outside the grid
      // * Thing 0 is vertically outside the grid and Thing 1 is not
      // * Thing 1 is vertically outside the grid and Thing 0 is not
      if (maybet0[0] < 0 || maybet1[0] < 0 || maybet0[0] > vertMax ||
          maybet1[0] > vertMax) {
        continue;
      }
      // * Both Things are horizontally outside the grid
      // * Thing 0 is horizontally outside the grid and Thing 1 is not
      // * Thing 1 is horizontally outside the grid and Thing 0 is not
      if (maybet0[1] < 0 || maybet1[1] < 0 || maybet0[1] > horizMax ||
          maybet1[1] > horizMax) {
        continue;
      }

      // (pull out the current symbols at those locations, for convenience,
      // now that we know the things are inside the grid)
      char maybet0Sym = matrix[maybet0[0]][maybet0[1]];
      char maybet1Sym = matrix[maybet1[0]][maybet1[1]];

      // * Both Things are in a wall
      if (maybet0Sym == WALL && maybet1Sym == WALL) {
        continue;
      }
      // * Thing 0 is in a wall and Thing 1 is where Thing 0 just was
      if (maybet0Sym == WALL && maybet1[0] == thing0[0] && maybet1[1] ==
          thing0[1]) {
        continue;
      }
      // * Thing 1 is in a wall and Thing 0 is where Thing 1 just was
      if (maybet1Sym == WALL && maybet0[0] == thing1[0] && maybet0[1] ==
          thing1[1]) {
        continue;
      }

      // Conditions which always generate a config:
      // * Neither Thing 0 nor Thing 1 is in a wall
      if (maybet0Sym != WALL && maybet1Sym != WALL) {
        int[][] config = {maybet0, maybet1};
        ConfigNode newNode = new ConfigNode(config);
        ConfigNode retNode = getSetSeen(newNode, seen);
        configs.add(retNode);
      } else if (maybet0Sym == WALL) {
        // * Thing 0 is in a wall, but Thing 1 is not
        int[][] config = {thing0, maybet1};
        ConfigNode newNode = new ConfigNode(config);
        ConfigNode retNode = getSetSeen(newNode, seen);
        configs.add(retNode);
      } else {
        // * Thing 1 is in a wall, but Thing 0 is not
        int[][] config = {maybet0, thing1};
        ConfigNode newNode = new ConfigNode(config);
        ConfigNode retNode = getSetSeen(newNode, seen);
        configs.add(retNode);
      }
      // What is a winning configuration? One where both Things are on the
      // same edge of the board.  Don't forget to add 1.
    }

    return configs;
  }

  /**
   * Find <code>symbol</code> in <code>matrix</code>, as long as it only ever
   * appears once.
   *
   * @param symbol The symbol to find
   * @param matrix The matrix in which to find it
   * @return A 2-element int array with the coordinates of the symbol, or (-1,
   * -1) if not found
   */
  private static int[] find(char symbol, char[][] matrix) {
    int[] answer = {-1, -1};
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        if (matrix[i][j] == symbol) {
          answer[0] = i;
          answer[1] = j;
          return answer;
        }
      }
    }
    return answer;
  }

  /**
   * Solve the puzzle.
   *
   * @param house The house in which Things Zero and One are trapped
   * @return The smallest possible number of moves for the things to free
   * themselves, or -1 if they are stuck
   */
  private static int solve(char[][] house) {
    int[] notfound = {-1, -1};
    // Find the Things in the matrix
    int[] thing0 = find(THING_0, house);
    int[] thing1 = find(THING_1, house);
    // Remove them
    house[thing0[0]][thing0[1]] = FLOOR;
    house[thing1[0]][thing1[1]] = FLOOR;
    // Make a config for the root node
    int[][] rootConfig = {thing0, thing1};
    ConfigNode root = new ConfigNode(rootConfig);
    // Create the seen and visited matrices, which are different. Seen is for
    // when the tree is being generated, visited is for when it's being searched
    ConfigNode[][][][] seen = new ConfigNode[house.length][house[0].length]
        [house.length][house[0].length];
    boolean[][][][] visited = new boolean[house.length][house[0]
        .length][house.length][house[0].length];

    // Skip everything if the input is malformed. It shouldn't be, but why
    // not include this check anyway?
    if (thing0 == notfound || thing1 == notfound) {
      return -1;
    }

    //
    // Generate the tree of configurations
    //
    Deque<ConfigNode> nextMoves = new Deque<>();
    ArrayList<ConfigNode> configs;
    // Initialize the deque
    nextMoves.add(root);

    // While there are more moves to process
    while (!nextMoves.isEmpty()) {
      // Pop one off
      ConfigNode parent = nextMoves.pop();
      // If it has kids already, do nothing, since we've already generated it
      if (parent.howManyChildren() == -1) {
        // Otherwise, generate the kids
        configs = generateConfigs(parent, house, seen);
        // And add them
        for (int i = 0; i < configs.size(); i++) {
          ConfigNode newChild = configs.get(i);
          parent.addChild(newChild);
          nextMoves.add(newChild);
        }
      }
    }

    //
    // Search through the tree for the closest configuration where the Things
    // have left the grid
    //
    nextMoves.clear();
    nextMoves.add(root);

    // This looks bad, but it's really still O(n) because the outer for loop is
    // just for chunking the next moves.
    int level = 0;
    ConfigNode parent;
    // While there are more moves to process
    while (!nextMoves.isEmpty()) {
      // Take one "layer" of them at a time
      int numMoves = nextMoves.size();
      for (int i = 0; i < numMoves; i++) {
        // and for each one,
        parent = nextMoves.pop();
        // check to see if it's been visited before.
        if (!getVisited(parent, visited)) {
          // If not, set it as visited,
          setVisited(parent, visited);
          // and if it's a winning configuration (at an edge)
          if (parent.isWinning(house.length - 1, house[0].length - 1)) {
            // return the current level of the search (what horizontal line
            // of this tree are we on?)
            return level;
          } else {
            // If it's not a winning config, take its children and add them
            // to the nextMoves queue
            ConfigNode children[] = parent.getChildren();
            for (int j = 0; j <= parent.howManyChildren(); j++) {
              nextMoves.push(children[j]);
            }
          }
        }
      }
      // Increment the level. This is why the while loop is batched---this
      // wouldn't work if it just ran iteratively
      level++;
    }

    // If we didn't find a winning configuration, they're stuck, so return -1
    return -1;
  }

  /**
   * The main function for DoubleTrouble. Reads input from stdin and delegates
   * responsibilities as appropriate.
   *
   * @param args Any arguments to this program from the command-line
   */
  public static void main(String[] args) {
    try (Scanner stdin = new Scanner(System.in)) {
      int a = stdin.nextInt();
      int b = stdin.nextInt();
      stdin.nextLine();
      char[][] house = new char[a][b];
      for (int i = 0; i < a; ++i) {
        house[i] = stdin.nextLine().toCharArray();
      }

      int result = solve(house);
      if (result > 0) {
        System.out.println(result);
      } else {
        System.out.println("STUCK");
      }
    }
  }
}
