# CSCI 251: Concepts of Parallel and Distributed Systems Project 1

Alvin Lin (axl1439)

## Files included:
  - `Makefile`
  - `bitonic_sort.c`
  - `input.py`
  - `assets/data.pdf`
  - `assets/project1.pdf`

## How to Run
```
make                                        # compile the C program

./input.py                                  # creates and populates the data
                                            # directory

./bitonic_sort.out <threads> < data/input1  # Sorts the data in data/input1
```
Running `input.py` will create a `data` directory in your current working
directory and populate it with 6 data files with 2^4, 2^8, 2^12, 2^16, 2^20,
and 2^24 data elements, respectively. You can also create your own data files
to pipe into the bitonic sorter, but it must have the number of elements (power
of 2) on the first line and the elements (space-separated) on the second line.
When invoking the bitonic sort executable, the number of threads to use must be
a power of 2.
