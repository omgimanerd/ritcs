/// Queue for holding coordinates (used in robot searching)
/// @author axl1439 (Alvin Lin)

#include <stdio.h>
#include <stdlib.h>

#include "queue.h"

/// Creates a node for insertion into the queue.
Node* create_queue_node(int x, int y) {
  Node* node = (Node*) malloc(sizeof(Node));
  if (!node) {
    fprintf(stderr, "error: malloc() failed in create_queue_node()\n");
    exit(1);
  }
  node->x = x;
  node->y = y;
  node->next = NULL;
  return node;
}

/// Destroys a queue node and frees all allocated memory.
void destroy_queue_node(Node* node) {
  if (node) {
    free(node);
  }
}

/// Creates an empty queue
Queue* create_queue() {
  Queue* queue = (Queue*) malloc(sizeof(Queue));
  if (!queue) {
    fprintf(stderr, "error: malloc() failed in create_queue()\n");
    exit(1);
  }
  queue->head = NULL;
  queue->tail = NULL;
  queue->size = 0;
  return queue;
}

/// Inserts a node into an existing queue.
void enqueue(Queue* queue, Node* node) {
  if (queue->size++ > 0) {
    queue->tail->next = node;
    queue->tail = node;
  } else {
    queue->head = queue->tail = node;
  }
}

/// Removes an item from a queue.
Node* dequeue(Queue* queue) {
  Node* temp = queue->head;
  if (temp) {
    queue->head = temp->next;
    queue->size--;
    return temp;
  }
  return NULL;
}

/// Returns the size of a queue.
int size_queue(Queue* queue) {
  return queue->size;
}

/// Destroys a queue and frees all allocated memory.
void destroy_queue(Queue* queue) {
  Node* node = dequeue(queue);
  while (node) {
    destroy_queue_node(node);
    node = dequeue(queue);
  }
  free(queue);
}
