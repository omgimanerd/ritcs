/// Multithreaded and serial bitonic sort.
/// Author: axl1439 (Alvin Lin)

#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>

/// Structure used to contain a section of data to be sorted by the bitonic
/// sort. start (inclusive) and end (exclusive) define the bounds for the
/// section of data to be sorted. For serial sorting, we set start to 0 and end
/// to the last index so that the entire data array is passed into the sorting
/// function. For parallel sorting, we create multiple Chunk structs and
/// perform a merge later.
typedef struct Chunk_Struct {
  int* data;
  int start;
  int end;
  int direction;
} Chunk;

int is_power_of_2(int x);
void compare_swap(int* data, int direction, int i, int j);
void chunk_merge(Chunk* chunk);
void chunk_sort(Chunk* chunk);
void serial_sort(int* data, int n);
void* chunk_sort_pthread(void* chunk);
void* chunk_merge_pthread(void* chunk);
void threaded_sort(int* data, int n, int n_threads);

/// https://stackoverflow.com/questions/3638431/
int is_power_of_2(int x) {
  return x && !(x & (x - 1));
}

/// The compare_swap operation that compares two elements in the array to sort
/// and swaps them if necessary.
void compare_swap(int* data, int direction, int i, int j) {
  if ((direction < 0 && data[i] < data[j]) ||
      (direction > 0 && data[i] > data[j])) {
    int tmp = data[j];
    data[j] = data[i];
    data[i] = tmp;
  }
}

/// Creates and allocates memory for a chunk struct and populates it with the
/// given data.
Chunk* chunk_create(int* data, int start, int end, int direction) {
  Chunk* chunk = malloc(sizeof(Chunk));
  chunk->data = data;
  chunk->start = start;
  chunk->end = end;
  chunk->direction = direction;
  return chunk;
}

/// Performs the bitonic merge operation within the bounds of the given chunk.
void chunk_merge(Chunk* chunk) {
  int* data = chunk->data;
  int start = chunk->start;
  int end = chunk->end;
  int direction = chunk->direction;
  if (end - start <= 1) {
    return;
  }
  int k = (start + end) / 2;

  // Compare swap up the entire chunk.
  for (int i = start, j = k; i < k; ++i, ++j) {
    compare_swap(data, direction, i, j);
  }

  // Create new chunks to encapsulate the two halves.
  Chunk* half1 = chunk_create(data, start, k, direction);
  Chunk* half2 = chunk_create(data, k, end, direction);

  // Recursively merge the two halves.
  chunk_merge(half1);
  chunk_merge(half2);

  // Frees the memory allocated for the two temporary chunk halves.
  free(half1);
  free(half2);
}

/// Performs the bitonic sort operation within the bounds of the given chunks.
void chunk_sort(Chunk* chunk) {
  int* data = chunk->data;
  int start = chunk->start;
  int end = chunk->end;
  if (end - start <= 1) {
    return;
  }
  int k = (start + end) / 2;

  // Create new chunks to encapsulate the two halves.
  Chunk* half1 = chunk_create(data, start, k, 1);
  Chunk* half2 = chunk_create(data, k, end, -1);

  // Recursively sort the two halves in opposite directions to create a
  // bitonic sequence.
  chunk_sort(half1);
  chunk_sort(half2);

  // Merge the bitonic sequence.
  chunk_merge(chunk);

  // Frees the memory associated with the two temporary chunk halves.
  free(half1);
  free(half2);
}

/// Sorts a given data array in place serially.
void serial_sort(int* data, int n) {
  Chunk* chunk = chunk_create(data, 0, n, 1);
  chunk_sort(chunk);
  free(chunk);
}

void* chunk_merge_pthread(void* chunk_ptr) {
  chunk_merge((Chunk*) chunk_ptr);
  return NULL;
}

/// A thin wrapper around the chunk_sort function for use by p_thread
/// invocation.
void* chunk_sort_pthread(void* chunk_ptr) {
  chunk_sort((Chunk*) chunk_ptr);
  return NULL;
}

/// Sorts a given data array in place using the given number of threads.
/// The number of threads must be a power of 2 or the behavior will be
/// undefined.
void threaded_sort(int* data, int n, int n_threads) {
  // Allocate memory for the chunks which will hold the labor distribution
  // information for the sort operation.
  Chunk* sort_chunks[n_threads];
  int d = n / n_threads;
  for (int i = 0, c = 0, dir = 1; i < n_threads; ++i, c += d, dir *= -1) {
    sort_chunks[i] = chunk_create(data, c, c + d, dir);
  }

  // Invoke the chunk sorting function on each chunk using the specified
  // number of threads.
  pthread_t sort_threads[n_threads];
  for (int i = 0; i < n_threads; ++i) {
    void* chunk = (void*) sort_chunks[i];
    if (pthread_create(sort_threads + i, NULL, chunk_sort_pthread, chunk)) {
      fprintf(stderr, "Thread creation failed!\n");
      return;
    }
  }

  // Join the threads.
  void* result;
  for (int i = 0; i < n_threads; ++i) {
    pthread_join(sort_threads[i], &result);
  }

  // Memory cleanup.
  for (int i = 0; i < n_threads; ++i) {
    free(sort_chunks[i]);
  }

  // Perform the merge operation to merge the threads again.
  while (n_threads != 1) {
    int half = n_threads / 2;
    int d = n / half;

    // Allocate memory for the chunks which will hold the labor distribution
    // information for the merge operation.
    Chunk* merge_chunks[half];
    for (int i = 0, c = 0, dir = 1; i < half; ++i, c += d, dir *= -1) {
      merge_chunks[i] = chunk_create(data, c, c + d, dir);
    }

    // Invoke the chunk merging function on each chunk using the threads.
    pthread_t merge_threads[half];
    for (int i = 0; i < half; ++i) {
      void* chunk = (void*) merge_chunks[i];
      if (pthread_create(merge_threads + i, NULL, chunk_merge_pthread, chunk)) {
        fprintf(stderr, "Thread creation failed!\n");
        return;
      }
    }

    // Join the threads.
    for (int i = 0; i < half; ++i) {
      pthread_join(merge_threads[i], &result);
    }

    // Memory cleanup
    for (int i = 0; i < half; ++i) {
      free(merge_chunks[i]);
    }

    // Loop update
    n_threads /= 2;
  }
}

/// The main function.
int main(int argc, char** argv) {
  if (argc != 2) {
    fprintf(stderr, "usage: cat data | bitonic_sort <threads>\n");
    return 1;
  }

  // Read in the number of threads. This must be a power of two or the
  // behavior will be undefined.
  int threads = atoi(argv[1]);
  if (threads <= 0 || !is_power_of_2(threads)) {
    fprintf(stderr, "threads must be a power of 2\n");
    return 1;
  }

  // Read in the number of data elements to sort. This must be a power of two
  // or the behavior will be undefined.
  int n;
  scanf("%d", &n);
  if (n <= 0 || !is_power_of_2(n)) {
    fprintf(stderr, "input size must be a power of 2\n");
    return 1;
  }

  // Read in the data elements to sort.
  int* data = malloc(sizeof(int) * n);
  for (int i = 0; i < n; ++i) {
    scanf("%d", data + i);
  }

  // Execute the sort.
  if (threads == 1) {
    serial_sort(data, n);
  } else {
    threaded_sort(data, n, threads);
  }

  // Since outputting to stdout takes a lot of time, we will not do that during
  // the timing tests.
  // for (int i = 0; i < n; ++i) {
  //   printf("%d ", data[i]);
  // }

  free(data);
  return 0;
}
