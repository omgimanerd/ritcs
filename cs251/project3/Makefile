GCC = gcc -ggdb -Wall -Wextra -pedantic -pthread -lpthread -lrt
VALGRIND = valgrind --leak-check=full --show-leak-kinds=all --track-origins=yes

all: simulate

robot.o: robot.h robot.c
	$(GCC) -c robot.c

display.o: display.h display.c
	$(GCC) -c display.c

queue.o: queue.h queue.c
	$(GCC) -c queue.c

simulate: simulate.c display.o robot.o queue.o
	$(GCC) simulate.c display.o robot.o queue.o -o simulate

run:
	./simulate 20 2 50 1

run1:
	./simulate 30 4 50 2

run2:
	./simulate 40 8 50 3
