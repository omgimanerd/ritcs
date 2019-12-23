/// Main file for a robot.
/// Author: Alvin Lin (axl1439)

#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include "display.h"
#include "queue.h"
#include "robot.h"

#define MOVE_DIRECTIONS   4
#define SEARCH_DIRECTIONS 8

/// Valid offsets a robot may move in from any position
static const int MOVE_OFFSET[4][2] = {
  { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 }
};

/// Valid offsets a robot may search in from any position
static const int SEARCH_OFFSET[8][2] = {
  { 1, 0 }, { 1, -1 }, { 0, -1 }, { -1, -1 },
  { -1, 0 }, { -1, 1}, { 0, 1 }, { 1, 1 }
};

/// Returns the Manhattan distance between two points.
int get_square_euclidean_distance(int x1, int y1, int x2, int y2) {
  return ((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2));
}

/// Creates a Robot for the simulation.
Robot* robot_create(int id, int x, int y, int grid_size, int delay,
                    pthread_mutex_t* update_mutex, int target_x, int target_y) {
  Robot* robot = malloc(sizeof(Robot));
  if (!robot) {
    fprintf(stderr, "error: malloc() failed in robot_create()\n");
    exit(1);
  }
  robot->id = id;
  robot->x = x;
  robot->y = y;
  robot->delay = delay;
  robot->grid_size = grid_size;
  robot->searched = malloc(grid_size * sizeof(int*));
  for (int i = 0; i < grid_size; ++i) {
    robot->searched[i] = malloc(grid_size * sizeof(int));
  }
  robot->done = 0;
  robot->updated = 0;
  robot->update_mutex = update_mutex;
  robot->target_x = NOT_FOUND;
  robot->target_y = NOT_FOUND;
  robot->hidden_target_x = target_x;
  robot->hidden_target_y = target_y;
  return robot;
}

/// The function passed to pthread_create to run a robot.
void* robot_run_pthread(void* robot) {
  robot_run((Robot*) robot);
  return NULL;
}

/// Validates if a given coordinate can be moved to.
int robot_is_valid_move_square(Robot* robot, int x, int y) {
  int grid_size = robot->grid_size;

  // If the coordinate is out of bounds, it is not valid.
  if (x < 0 || y < 0 || x >= grid_size || y >= grid_size) {
    return 0;
  }

  // If the coordinate is on top of the target block, it is not valid.
  if (x == robot->target_x && y == robot->target_y) {
    return 0;
  }

  // If the coordinate is on top of a robot, it is not valid.
  for (int i = 0; i < robot->num_robots; ++i) {
    Robot* other = robot->robots_list[i];
    if (x == other->x && y == other->y) {
      return 0;
    }
  }
  return 1;
}

/// Uses breadth first search to move the robot to the nearest unsearched
/// square on the grid.
void robot_move_unknown_target(Robot* robot) {
  int grid_size = robot->grid_size;

  // Breadth first search for the closest unsearched space.
  // Initialize necessary data areas for breadth first search.
  int predecessors[grid_size][grid_size][2];
  int visited[grid_size][grid_size];
  memset(predecessors, 0, sizeof(int) * grid_size * grid_size * 2);
  memset(visited, 0, sizeof(int) * grid_size * grid_size);
  int move_target_x = robot->x, move_target_y = robot->y;
  Queue* queue = create_queue();

  // Queue the current location of the robot.
  enqueue(queue, create_queue_node(robot->x, robot->y));
  visited[robot->x][robot->y] = 1;

  // Stop the breadth first search when there are no more valid squares, or we
  // have found a valid target square to move to.
  while (size_queue(queue) != 0 &&
      move_target_x == robot->x && move_target_y == robot->y) {
    Node* current = dequeue(queue);
    // Search through the movable directions for the closest unsearched square.
    for (int i = 0; i < MOVE_DIRECTIONS; ++i) {
      int x = current->x + MOVE_OFFSET[i][0];
      int y = current->y + MOVE_OFFSET[i][1];
      // Exclude invalid squares.
      if (robot_is_valid_move_square(robot, x, y) && !visited[x][y]) {
        predecessors[x][y][0] = current->x;
        predecessors[x][y][1] = current->y;
        // If the square was searched already, keeping breadth first searching.
        if (robot->searched[x][y]) {
          enqueue(queue, create_queue_node(x, y));
          visited[x][y] = 1;
        } else {
          // Otherwise, we've found our closest unsearched square.
          move_target_x = x;
          move_target_y = y;
        }
      }
    }
    // Free the memory associated with the node when we are done with it.
    destroy_queue_node(current);
  }
  // Free the memory associated with the queue when we are done searching.
  destroy_queue(queue);

  // After the search, use the predecessors array to find the next spot we
  // should move to and go there.
  while (!(predecessors[move_target_x][move_target_y][0] == robot->x &&
      predecessors[move_target_x][move_target_y][1] == robot->y)) {
    // We need to store the move targets temporary because their new values
    // depend on the old values. I was fucking retarded and didn't spot this
    // bug for like 2 hours.
    int tmp_x = move_target_x, tmp_y = move_target_y;
    move_target_x = predecessors[tmp_x][tmp_y][0];
    move_target_y = predecessors[tmp_x][tmp_y][1];
  }

  // Move the robot one space along the path that would take us to the closest
  // available unsearched space.
  robot->x = move_target_x;
  robot->y = move_target_y;
}

/// Uses breadth first search to move a robot to the closest position possible
/// to the known target block.
int robot_move_known_target(Robot* robot) {
  int grid_size = robot->grid_size;
  int target_x = robot->target_x;
  int target_y = robot->target_y;

  // Breadth first search for the target.
  // Initialize necessary data areas for breadth first search.
  int predecessors[grid_size][grid_size][2];
  int visited[grid_size][grid_size];
  memset(predecessors, 0, sizeof(int) * grid_size * grid_size * 2);
  memset(visited, 0, sizeof(int) * grid_size * grid_size);
  int move_target_x = robot->x, move_target_y = robot->y;
  int move_target_distance = get_square_euclidean_distance(
      move_target_x, move_target_y, target_x, target_y);
  Queue* queue = create_queue();

  // Queue the current location of the robot.
  enqueue(queue, create_queue_node(robot->x, robot->y));
  visited[robot->x][robot->y] = 1;

  // Stop the breadth first search when there are no more valid squares or we
  // have found a valid target square.
  while (size_queue(queue) != 0) {
    Node* current = dequeue(queue);
    // Search through the movable directions for the closest unsearched square.
    for (int i = 0; i < MOVE_DIRECTIONS; ++i) {
      int x = current->x + MOVE_OFFSET[i][0];
      int y = current->y + MOVE_OFFSET[i][1];
      // Exclude invalid squares.
      if (robot_is_valid_move_square(robot, x, y) && !visited[x][y]) {
        predecessors[x][y][0] = current->x;
        predecessors[x][y][1] = current->y;
        enqueue(queue, create_queue_node(x, y));
        visited[x][y] = 1;
        // If this square brings us closer to the target, set this as our move
        // target. We will use Manhattan distance to determine this.
        int distance = get_square_euclidean_distance(x, y, target_x, target_y);
        if (distance < move_target_distance) {
          move_target_x = x;
          move_target_y = y;
          move_target_distance = distance;
        }
      }
    }
    // Free the memory associated with the node when we are done with it.
    destroy_queue_node(current);
  }
  // Free the memory associated with the queue when we are done searching.
  destroy_queue(queue);

  // If the move target is our current location, we are done.
  if (move_target_x == robot->x && move_target_y == robot->y) {
    return 1;
  }

  // Otherwise, we need to move the robot to the calculated closest point.
  // Use the predecessors array to find the next spot we should move to.
  while (!(predecessors[move_target_x][move_target_y][0] == robot->x &&
      predecessors[move_target_x][move_target_y][1] == robot->y)) {
    // We need to store the move targets temporary because their new values
    // depend on the old values. I was fucking retarded and didn't spot this
    // bug for like 2 hours.
    int tmp_x = move_target_x, tmp_y = move_target_y;
    move_target_x = predecessors[tmp_x][tmp_y][0];
    move_target_y = predecessors[tmp_x][tmp_y][1];
  }

  // Move the robot one space along the path.
  robot->x = move_target_x;
  robot->y = move_target_y;

  // Return 0 to indicate that we aren't done moving towards the target.
  return 0;
}

/// Updates a robot, run perpetually by the pthread.
void robot_run(Robot* robot) {
  int grid_size = robot->grid_size;

  while (!robot->done) {
    pthread_mutex_lock(robot->update_mutex);
    // This robot will perform its update if it has not done so this cycle.
    if (!robot->updated) {
      if (robot->target_x == NOT_FOUND && robot->target_y == NOT_FOUND) {
        // If the target location is unknown, then the robot will mark the
        // surrounding squares as unsearched.
        robot->searched[robot->x][robot->y] = 1;
        for (int i = 0; i < SEARCH_DIRECTIONS; ++i) {
          int x = robot->x + SEARCH_OFFSET[i][0];
          int y = robot->y + SEARCH_OFFSET[i][1];
          if (x >= 0 && x < grid_size && y >= 0 && y < grid_size) {
            robot->searched[x][y] = 1;
            // If we found the target, then store the target location.
            if (x == robot->hidden_target_x && y == robot->hidden_target_y) {
              robot->target_x = robot->hidden_target_x;
              robot->target_y = robot->hidden_target_y;
            }
          }
        }

        // At the end, the robot will broadcast the squares that it searched to
        // all the other robots so that they won't search those squares. It
        // will also broadcast the location of the target if it was found.
        robot_broadcast(robot);

        // Breadth first search for the nearest unsearched square and move to
        // it.
        robot_move_unknown_target(robot);
      } else {
        // If the target location is known, then the robot will breadth first
        // search for the closest square to the target and attempt to move
        // there.
        robot->done = robot_move_known_target(robot);
      }

      // The last robot to update is delegated the task of redrawing the grid
      // and resetting every other robot to the un-updated state.
      robot->updated = 1;
      int cycle_done = 1;
      for (int i = 0; i < robot->num_robots; ++i) {
        if (i == robot->id) {
          continue;
        }
        Robot* current = robot->robots_list[i];
        // We count a robot as updated if it is "done" moving.
        if (!current->done && !current->updated) {
          cycle_done = 0;
        }
      }

      // cycle_done means that all robots have been updated, at which point we
      // should redraw the grid and reset all robots to un-updated.
      if (cycle_done) {
        for (int i = 0; i < robot->num_robots; ++i) {
          Robot* current = robot->robots_list[i];
          current->updated = 0;
        }
        robot_draw(robot);
        usleep(robot->delay * 1000);
      }
    }
    pthread_mutex_unlock(robot->update_mutex);
  }
}

/// Broadcasts a robot's searched array and target location (if found) to the
/// other robots.
void robot_broadcast(Robot* robot) {
  for (int i = 0; i < robot->num_robots; ++i) {
    if (i != robot->id) {
      robot_receive(robot->robots_list[i], robot->searched,
          robot->target_x, robot->target_y);
    }
  }
}

/// Handles the updating a robot's state when a broadcast is received.
void robot_receive(Robot* robot, int** searched, int target_x, int target_y) {
  int grid_size = robot->grid_size;
  for (int x = 0; x < grid_size; ++x) {
    for (int y = 0; y < grid_size; ++y) {
      robot->searched[x][y] |= searched[x][y];
    }
  }
  if (target_x != NOT_FOUND && target_y != NOT_FOUND) {
    robot->target_x = target_x;
    robot->target_y = target_y;
  }
}

/// Draws the state of the grid to the console, delegated to the last robot to
/// update during the update cycle.
void robot_draw(Robot* robot) {
  int grid_size = robot->grid_size;
  int num_robots = robot->num_robots;
  int** searched = robot->searched;
  Robot** robots = robot->robots_list;
  for (int i = 0; i < grid_size + 2; ++i) {
    if (i < grid_size) {
      set_cur_pos(i + 2, 1);
      put('|');
      set_cur_pos(i + 2, grid_size + 2);
      put('|');
    }
    set_cur_pos(1, i + 1);
    put('=');
    set_cur_pos(grid_size + 2, i + 1);
    put('=');
  }
  for (int x = 0; x < grid_size; ++x) {
    for (int y = 0; y < grid_size; ++y) {
      set_cur_pos(y + 2, x + 2);
      if (searched[x][y]) {
        put('-');
      } else {
        put(' ');
      }
    }
  }
  for (int i = 0; i < num_robots; ++i) {
    Robot* current_robot = robots[i];
    set_cur_pos(current_robot->y + 2, current_robot->x + 2);
    put(current_robot->id + 'A');
    set_cur_pos(current_robot->hidden_target_y + 2,
        current_robot->hidden_target_x + 2);
    put('#');
  }
  set_cur_pos(grid_size + 3, 1);
  put(' ');
}

/// Frees all the memory allocated to a Robot struct.
void robot_destroy(Robot* robot) {
  for (int i = 0; i < robot->grid_size; ++i) {
    free(robot->searched[i]);
  }
  free(robot->searched);
  free(robot);
}
