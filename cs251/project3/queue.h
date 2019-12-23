/// Queue for holding coordinates (used in robot searching).
/// Author: Alvin Lin (axl1439)

#ifndef _QUEUE_H
#define _QUEUE_H

/// The Node_S struct represents a node in a queue of void pointers
typedef struct Node_S {
  int x;
  int y;
  struct Node_S* next;
} Node;

/// The Queue_S struct represents a queue.
typedef struct Queue_S {
  Node* head;
  Node* tail;
  int size;
} Queue;

/// Creates a node for insertion into a queue.
/// @param x the x coordinate to hold in this node
/// @param y the y coordinate to hold in this node
/// @return a pointer
Node* create_queue_node(int x, int y);

/// Destroys a queue node and frees all allocated memory.
/// @param node the node to destory
void destroy_queue_node(Node* node);

/// Creates an empty queue
/// @return the new queue
Queue* create_queue();

/// Inserts a node into an existing queue.
/// @param queue the queue to insert into
/// @param node the pointer to the node to insert into the queue
void enqueue(Queue* queue, Node* node);

/// Removes an item from a queue.
/// @param queue the queue to remove from
/// @return the removed node or NULL if the queue is empty
Node* dequeue(Queue* queue);

/// Returns the size of a queue.
/// @param queue the queue to get the size of
/// @return the size of the queue
int size_queue(Queue* queue);

/// Destroys a queue and frees all allocated memory.
/// @param the queue to destroy
void destroy_queue(Queue* queue);

#endif
