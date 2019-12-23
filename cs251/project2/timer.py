#!/usr/bin/env python3

import timeit
import os

def get_times(threads, size, tries):
    return timeit.repeat(
        'os.system("./bitonic_sort.out {} < data/input_{}")'.format(
            threads, size),
        number=1,
        repeat=tries,
        setup='import os')

def main():
    for threads in [1, 2, 4, 8]:
        for i in range(4, 25, 4):
            print('n = 2^{}; p = {}'.format(i, threads))
            print("\n".join(map(str, get_times(threads, 2 ** i, 20))))

if __name__ == '__main__':
    main()
