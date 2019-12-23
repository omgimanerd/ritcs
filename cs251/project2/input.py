#!/usr/bin/env python3

import random
import sys
import os

def is_power2(num):
	return num != 0 and ((num & (num - 1)) == 0)

def get_random_sequence(size):
    if not is_power2(size):
        return "data size must be a power of 2"
    data = list(range(size))
    random.shuffle(data)
    return " ".join(map(str, data))

if __name__ == '__main__':
    os.system('rm -rf data')
    os.mkdir('data')
    for i in range(4, 25, 4):
        size = 2 ** i
        with open('data/input_{}'.format(size), 'w') as f:
            f.write('{}\n{}'.format(size, get_random_sequence(size)))
            print('Wrote data/input_{}'.format(size))
