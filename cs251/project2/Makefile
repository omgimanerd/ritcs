GCC = gcc -ggdb -Wall -Wextra -pedantic -pthread -lpthread -lrt
VALGRIND = valgrind --leak-check=full --show-leak-kinds=all --track-origins=yes

all: bitonic_sort

bitonic_sort: bitonic_sort.c
	$(GCC) bitonic_sort.c -o bitonic_sort.out

run1:
	cat data/input1 | ./bitonic_sort.out 1
