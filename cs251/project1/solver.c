/// Gauss-Jordan Elimination Matrix solver.
/// Author: axl1439 (Alvin Lin)

#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/// Structure used by the threads to divide up the Gauss-Jordan column
/// elimination work. Each struct holds the rows and the columns that the
/// threads need to compute.
typedef struct Chunk_Struct {
  float** matrix;
  int i;
  int j_start;
  int j_end;
  int size;
} Chunk;

/// The reduction that the threads will perform.
void* chunk_reduce(void* chunk_ptr) {
  Chunk* chunk = (Chunk*) chunk_ptr;
  int i = chunk->i;
  for (int j = chunk->j_start; j < chunk->j_end; ++j) {
    if (chunk->i != j) {
      // Take the current column and zero out every other column in the matrix
      // to put it in reduced row echelon form. Each thread will be in charge
      // of doing this for some number of rows/
      float c = chunk->matrix[j][i] / chunk->matrix[i][i];
      for (int k = 0; k < chunk->size + 1; ++k) {
        chunk->matrix[j][k] -= c * chunk->matrix[i][k];
      }
    }
  }
  return NULL;
}

/// Solves an augmented matrix using Gauss-Jordan elimination where the work
/// is distributed among a specified number of threads. This function assumes
/// that the number of threads is less than the size of the matrix, since that
/// sanity check is performed in the main function.
void threaded_solve(int size, float** matrix, int n_threads) {
  Chunk* chunks[n_threads];
  // Allocate memory for the chunks which will hold the labor distribution
  // information for the threads.
  for (int t = 0; t < n_threads; ++t) {
    chunks[t] = malloc(sizeof(Chunk));
    chunks[t]->matrix = matrix;
  }

  pthread_t threads[n_threads];
  // Calculate how many rows each thread will compute.
  int rows_per_thread = (size / n_threads) + 1;
  void* result;
  for (int i = 0; i < size; ++i) {
    int start = 0;
    int end = start + rows_per_thread;
    for (int t = 0; t < n_threads; ++t) {
      // Assign those rows to the threads and instantiate the threads.
      chunks[t]->i = i;
      chunks[t]->j_start = start;
      chunks[t]->j_end = end;
      chunks[t]->size = size;
      if (pthread_create(threads + t, NULL, chunk_reduce, (void*) chunks[t])) {
        fprintf(stderr, "Thread creation failed!\n");
        return;
      }
      start = end;
      end = start + rows_per_thread;
      end = (end > size) ? size : end;
    }
    // Join the threads
    for (int t = 0; t < n_threads; ++t) {
      pthread_join(threads[t], &result);
    }
  }
  // Memory cleanup when we're done.
  for (int t = 0; t < n_threads; ++t) {
    free(chunks[t]);
  }
  // Divide the last column by the leading columns along the diagonal to solve
  // for the solution vector.
  for (int i = 0; i < size; ++i) {
    matrix[i][size] /= matrix[i][i];
    matrix[i][i] = 1;
  }
}

/// Solves an augmented matrix using Gauss-Jordan elimination serially.
void serial_solve(int size, float** matrix) {
  for (int i = 0; i < size; ++i) {
    for (int j = 0; j < size; ++j) {
      if (i != j) {
        // Take the current column and zero out every other column in the matrix
        // to put it in reduced row echelon form.
        float c = matrix[j][i] / matrix[i][i];
        for (int k = 0; k < size + 1; ++k) {
          matrix[j][k] -= c * matrix[i][k];
        }
      }
    }
  }
  // Divide the last column by the leading columns along the diagonal to solve
  // for the solution vector.
  for (int i = 0; i < size; ++i) {
    matrix[i][size] /= matrix[i][i];
    matrix[i][i] = 1;
  }
}

/// Outputs the result column of the matrix.
void output(int size, float** matrix) {
  for (int row = 0; row < size; ++row) {
    printf("%.2f\n", matrix[row][size]);
  }
}

int main(int argc, char** argv) {

  if (argc != 3) {
    fprintf(stderr, "usage: cat data | solver <size> <threads>\n");
    return 1;
  }

  int size = atoi(argv[1]);
  int threads = atoi(argv[2]);
  float** matrix = malloc(sizeof(float*) * size);
  for (int i = 0; i < size; ++i) {
    matrix[i] = malloc(sizeof(float) * (size + 1));
  }

  char* line = NULL;
  char* freeptr = NULL;
  size_t n;
  for (int row = 0; row < size; ++row) {
    if (getline(&line, &n, stdin) == -1) {
      fprintf(stderr, "matrix size mismatch!\n");
      exit(0);
    } else {
      char* value;
      freeptr = line;
      int col = 0;
      while ((value = strsep(&line, " "))) {
        matrix[row][col++] = atof(value);
      }
    }
    free(freeptr);
  }

  if (threads == 1 || threads >= size) {
    serial_solve(size, matrix);
    output(size, matrix);
  } else if (threads > 1) {
    threaded_solve(size, matrix, threads);
    output(size, matrix);
  } else {
    fprintf(stderr, "usage: cat data | solver <size> <threads>\n");
  }

  for (int i = 0; i < size; ++i) {
    free(matrix[i]);
  }
  free(matrix);

  return 0;
}
