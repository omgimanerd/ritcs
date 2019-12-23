#!/usr/bin/env python3
# Author: Nathan Farrell (naf1602)

import numpy as np
import matplotlib.pyplot as plt
from base import *

def main():
    training_data = get_training_data(1000)

    # Random first weights
    w1 = np.random.randn()
    w2 = np.random.randn()
    w3 = np.random.randn()
    w4 = np.random.randn()
    b = np.random.randn()


    for i in range(1000):
        rand_index = np.random.randint(len(training_data))
        data_in = training_data[i]

        result = NN(data_in[1], data_in[2], data_in[3], data_in[4], w1, w2, w3, w4, b)
        attack = data_in[ATTACK]

        error = np.square(result - attack)

        # Couldn't figure out how to quantify matching the error returned with the adjustment that
        # needed to be made to represent an attack.




def sigmoid(x):
    return 1/(1+np.exp(-x))

def sigmoid_deriv(x):
    return sigmoid(x) * (1- sigmoid(x))

def NN( protocol, service, flag, src_bytes, w1, w2, w3, w4, b):
    z = protocol * w1 + service * w2 + flag * w3 + src_bytes * w4 + b
    return sigmoid(z)





if __name__ == '__main__':
    main()