#!/usr/bin/env python3
# Author: Alvin Lin (axl1439)
# CSCI 431 Introduction to Computer Vision
# HW 1 Problem 1

from matplotlib import pyplot as plt

import cv2
import numpy as np

def showHistogram(img):
    """
    Shows a histogram of the intensity distributions in the image.
    """
    plt.hist(img.ravel(), 256, [0, 256])
    plt.title('Image histogram')
    plt.xlabel('Intensity')
    plt.ylabel('Frequency')
    plt.show()

def applyStaticThreshold(img, threshold):
    """
    Applies a non-adaptive threshold filter to the image.
    """
    new = np.copy(img)
    for y, row in enumerate(new):
        for x, val in enumerate(row):
            if val > threshold:
                new[y, x] = 255
            else:
                new[y, x] = 0
    return new

def maxMinNeighborhood(neighborhood):
    """
    A function to calculate the new pixel value using the max and min values in
    the neighborhood.
    """
    return (np.max(neighborhood) - np.min(neighborhood)) / 2

def applyAdaptiveThreshold(img, n, c, neighborhood_fn):
    """
    Applies an adaptive thresholding filter to the image given a neighborhood
    size, a constant offset, and a function to calculate the new pixel value
    given the neighborhood values.

    Args:
        n - the neighborhood will be a 2n * 1 box around the current pixel
        c - a constant to add to the new calculated pixel value
        neighborhood_fn - a callback that is given the neighborhood to compute
                          the new pixel value
    """
    new = np.copy(img)
    rows, columns = img.shape
    for y in range(n, rows - n):
        for x in range(n, columns - n):
            neighborhood = img[y - n: y + n + 1, x - n: x + n + 1]
            new[y, x] = np.clip(neighborhood_fn(neighborhood) + c, 0, 255)
    return new

def main():
    img = cv2.imread('images/sonnet.png', cv2.IMREAD_GRAYSCALE)
    img2 = applyStaticThreshold(img, 150)
    img3 = applyAdaptiveThreshold(img, 1, 30, np.mean)
    img4 = applyAdaptiveThreshold(img, 1, 30, np.median)
    img5 = applyAdaptiveThreshold(img, 1, 100, maxMinNeighborhood)

    showHistogram(img)
    cv2.imshow('original', img)
    cv2.imshow('constant threshold', img2)
    cv2.imshow('mean adaptive threshold', img3)
    cv2.imshow('median adaptive threshold', img4)
    cv2.imshow('max-min adaptive threshold', img5)
    cv2.waitKey(0)
    cv2.destroyAllWindows()

if __name__ == '__main__':
    main()
