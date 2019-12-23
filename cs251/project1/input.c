/*
 * Input generator.
 * Author: axl1439 (Alvin Lin)
 */

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char** argv) {

  if (argc != 3) {
    fprintf(stderr, "usage: input <size> <random seed>\n");
    return 1;
  }

  int size = atoi(argv[1]);
  int seed = atoi(argv[2]);

  srand((unsigned) seed);

  for (int row = 0; row < size; ++row) {
    for (int col = 0; col < size + 1; ++col) {
      printf("%.2f%s", (float) rand() / 32768.0, (col < size) ? " " : "\n");
    }
  }

  return 0;
}
