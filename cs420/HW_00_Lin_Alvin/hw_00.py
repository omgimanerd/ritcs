#!/usr/bin/env python3

import itertools as it
import matplotlib.pyplot as plt
import numpy as np

def getTask0Time(speed):
    return (22 * 3600) / speed

def main():
    speed = np.arange(1, 81)
    time_sec = np.fromiter(
        [getTask0Time(s) for s in speed], np.dtype('float'))
    plt.figure(1)
    plt.title('Time to Travel 22 miles at various speeds')
    plt.xlabel('Speed (MPH)')
    plt.ylabel('Time to Travel (seconds)')
    plt.plot(speed, time_sec)
    plt.show()

    speed5 = np.arange(5, 81, 5)
    time_sec5 = np.fromiter(
        [getTask0Time(s) for s in speed5], np.dtype('float'))
    time_saved5 = np.fromiter(
        [time_sec5[i - 1] - time_sec5[i] for i in range(1, len(time_sec5))],
        np.dtype('float'))
    time_saved5 = np.insert(time_saved5, 0, None)
    plt.figure(2)
    plt.title('Traveling 22 miles at various speeds')
    plt.xlabel('Speed (MPH)')
    plt.xticks(speed5)
    plt.ylabel('Time (seconds)')
    plt.plot(speed, time_sec, speed5, time_saved5)
    plt.legend(['Time to travel 22 miles at speed',
                'Time saved by driving 5mph faster'])
    plt.show()

    front_gears = [73, 61, 47]
    back_gears = [23, 29, 37, 43, 47, 63, 71]
    front_label = 'ABC'
    back_label = 'abcdefg'
    plt.figure(3)
    plt.title('Gear Ratios for a Bicycle')
    plt.xlabel('Teeth on Back Gear')
    plt.xticks(back_gears)
    plt.ylabel('Teeth on Front Gear')
    plt.yticks(front_gears)
    labels = list(
        map(lambda s: "".join(s), it.product(back_label, front_label)))
    product = list(it.product(back_gears, front_gears))
    x, y = zip(*product)
    plt.scatter(x, y)
    for i, label in enumerate(labels):
        coord = list(product[i])
        plt.annotate(label, (coord[0], coord[1] + 1))
        plt.plot([0, coord[0]], [0, coord[1]])
    plt.show()

if __name__ == '__main__':
    main()
