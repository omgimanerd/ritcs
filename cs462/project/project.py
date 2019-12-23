#!/usr/bin/env python3
#
# Introduction to Cryptography Project
# Author: Alvin Lin (axl1439)

import numpy as np

PRIME = 127

def encrypt(plaintext, password):
    n = len(plaintext)
    r = 0
    for i in range(n):
        if r == 0:
            pass
        else:
            pass

def decrypt(ciphertext, password):
    n = len(ciphertext)
    if n % 2 == 0:
        r = 0
    pass

if __name__ == '__main__':
    a = np.array([
        [1, 1, 1, 1],
        [2, 3, 4, 5],
        [4, 6, 2, 3]
    ])
    a[1] += np.array([1, 1, 1, 1])
    print(a)
