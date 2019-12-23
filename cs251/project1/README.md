# CSCI 251: Concepts of Parallel and Distributed Systems Project 1

Alvin Lin (axl1439)

## Files included:
  - `Makefile`
  - `input.c`
  - `solver.c`
  - `generate_data.sh`
  - `test.sh`
  - `assets/data.pdf`
  - `assets/project1.pdf`

## How to Run
```
make                                    # compile the C program

./input <n> <random seed>               # Outputs an nxn matrix to stdout
                                        # generated using the given random seed

./solver <n> <threads> < data/input1    # Solves the nxn matrix passed in
                                        # through stdin using the given number
                                        # of threads. Specify 1 thread to run
                                        # the solver serially. The size given
                                        # to the solver must match the size of
                                        # the input matrix.
```
You can autogenerate input files by running `./generate_data.sh`, which will
create 6 input files of increasing size in the `data/` directory. You can also
pipe output from the `input` program directly into the solver. Example:
```
./input 500 1 | ./solver 500 1
./input 500 1 | ./solver 500 2
./input 500 1 | ./solver 500 8
./input 500 1 | ./solver 500 16
```
Or you can create and save your own data files for usage later:
```
./input 2048 1 > data/input8
./input 4096 1 > data/input9
```
