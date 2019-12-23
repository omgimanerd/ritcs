GCC = gcc -ggdb -Wall -Wextra -pedantic -pthread -lpthread -lrt
VALGRIND = valgrind --leak-check=full --show-leak-kinds=all --track-origins=yes

all: input solver

input: input.c
	$(GCC) input.c -o input

solver: solver.c
	$(GCC) solver.c -o solver

run:
	./input 5 5
