/// Header file for robot.h
/// Author: Alvin Lin (axl1439)

#ifndef ROBOT_H
#define ROBOT_H

#define NOT_FOUND -1

/// The Robot struct contains all the information needed to process a single
/// robot.
typedef struct Robot_Struct Robot;
struct Robot_Struct {
  // The unique id for this robot
  int id;

  // The location of this robot
  int x;
  int y;

  // The size of the grid
  int grid_size;

  // The delay before redrawing the grid
  int delay;

  // Internal grid keeping track of what has been searched, this is updated via
  // multicast messages
  int** searched;

  // Each robot holds an array of pointers to the other robots for communication
  int num_robots;
  Robot** robots_list;

  // During each update cycle, all the robots will update in parallel. The
  // update state ensures all robots update during each cycle and the mutex
  // prevents race conditions from occurring. If done is set to 1 (true), then
  // we will no longer update this robot and treated it as perpetually updated.
  int done;
  int updated;
  pthread_mutex_t* update_mutex;

  // The location of the target to surround as known to this robot.
  // This will be communicated via multicast.
  int target_x;
  int target_y;

  // The actual location of the target (unknown to the robot). This number is
  // stored and checked against when the robot "searches".
  int hidden_target_x;
  int hidden_target_y;
};

/// Returns the squared Euclidean distance between two points.
/// @param x1 the x coordinate of the first point
/// @param y1 the y coordinate of the first point
/// @param x2 the x coordinate of the second point
/// @param y2 the y coordinate of the second point
int get_square_euclidean_distance(int x1, int y1, int x2, int y2);

/// Creates a Robot for the simulation.
/// @param id the unique ID of this robot
/// @param x the starting x coordinate of this robot
/// @param y the starting y coordinate of this robot
/// @param grid_size the size of the grid
/// @param delay the drawing delay in milliseconds
/// @param update_mutex a pointer to the mutex the robots will use for updating
/// @param target_x the x coordinate of the target to find
/// @param target_y the y coordinate of the target to find
/// @return a pointer to the new Robot struct
Robot* robot_create(int id, int x, int y, int grid_size, int delay,
                    pthread_mutex_t* update_mutex, int target_x, int target_y);

/// The function passed to pthread_create to run a robot.
/// @param robot a pointer to the Robot struct to run
void* robot_run_pthread(void* robot);

/// Validates if a given coordinate can be moved to.
/// @param robot a pointer to the Robot struct to validate with
/// @param x the x coordinate to validate
/// @param y the y coordinate to validate
/// @return 1 if the move is valid, 0 otherwise
int robot_is_valid_move_square(Robot* robot, int x, int y);

/// Uses breadth first search to move a robot to the nearest unsearched
/// square on the grid.
/// @param robot a pointer to the Robot struct to move
void robot_move_unknown_target(Robot* robot);

/// Uses breadth first search to move a robot to the closest position possible
/// to the known target block. If the robot can move no closer, then this
/// function returns 1 (true), otherwise it returns 0 (false).
/// @param robot a pointer to the Robot struct to move
/// @return 0 if the robot is "done", 0 otherwise.
int robot_move_known_target(Robot* robot);

/// Updates a robot, run perpetually by the pthread.
/// @param robot a pointer to the Robot struct to run
void robot_run(Robot* robot);

/// Broadcasts a robot's searched array and target location (if found) to the
/// other robots.
/// @param robot a pointer to the Robot struct to broadcast
void robot_broadcast(Robot* robot);

/// Handles the updating a robot's state when a broadcast is received.
/// @param robot a pointer to the Robot struct to update
/// @param searched a pointer to the searched array to update against
/// @param target_x the x coordinate of the target block location (if found)
/// @param target_y the y coordinate of the target block location (if found)
void robot_receive(Robot* robot, int** searched, int target_x, int target_y);

/// Draws the state of the grid to the console, delegated to the last robot to
/// update during the update cycle.
/// @param robot a pointer to the Robot struct who will do the drawing
void robot_draw(Robot* robot);

/// Frees all the memory allocated to a Robot struct.
/// @param robot a pointer to the Robot struct to destroy
void robot_destroy(Robot* robot);

#endif
