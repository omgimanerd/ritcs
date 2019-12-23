/// simulate.c
/// Main file running the robot simulation
/// Author: Alvin Lin (axl1439)

#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>

#include "display.h"
#include "robot.h"

/// Main function
int main(int argc, char** argv) {

  // Validate the number of arguments
  if (argc != 4 && argc != 5) {
    fprintf(stderr, "usage: ./simulate <size> <robots> <delay> [seed]\n");
    return 1;
  }

  // Validate the arguments given.
  int size = atoi(argv[1]);
  int num_robots = atoi(argv[2]);
  int delay = atoi(argv[3]);
  if (argc == 5) {
    srand((unsigned) atoi(argv[4]));
  }
  if (size < 10) {
    fprintf(stderr, "the simulation grid size must be at least 10\n");
    return 1;
  }
  if (num_robots < 1) {
    fprintf(stderr, "robots must be a number greater than 1\n");
    return 1;
  }

  // Initialize the simulation
  int** grid = malloc(size * sizeof(int*));
  for (int i = 0; i < size; ++i) {
    grid[i] = malloc(size * sizeof(int));
  }
  Robot** robots = malloc(num_robots * sizeof(Robot*));
  pthread_mutex_t update_mutex;
  pthread_mutex_init(&update_mutex, NULL);
  int target_x = rand() % size;
  int target_y = rand() % size;
  grid[target_x][target_y] = 1;

  // Generate a random position for each robot and initialize each robot
  for (int i = 0; i < num_robots; ++i) {
    int x = rand() % size;
    int y = rand() % size;
    while (grid[x][y] == 1) {
      x = rand() % size;
      y = rand() % size;
    }
    robots[i] =
        robot_create(i, x, y, size, delay, &update_mutex, target_x, target_y);
  }
  for (int i = 0; i < num_robots; ++i) {
    robots[i]->num_robots = num_robots;
    robots[i]->robots_list = robots;
  }
  for (int i = 0; i < size; ++i) {
    free(grid[i]);
  }
  free(grid);

  // Initialize a thread for each robot for simulation execution
  pthread_t threads[num_robots];
  void* result;
  for (int i = 0; i < num_robots; ++i) {
    void* robot = (void*) robots[i];
    if (pthread_create(threads + i, NULL, robot_run_pthread, robot)) {
      fprintf(stderr, "Thread creation failed! Exiting...\n");
      return 1;
    }
  }

  // Join the threads for execution
  clear();
  for (int i = 0; i < num_robots; ++i) {
    pthread_join(threads[i], &result);
  }

  // Free all allocated memory at the end
  for (int i = 0; i < num_robots; ++i) {
    robot_destroy(robots[i]);
  }

  return 0;
}
