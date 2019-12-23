#!/usr/bin/env python3#!/usr/bin/env python3
# Author: Alvin Lin (axl1439)
# CSCI 431 Introduction to Computer Vision
# HW 1 Problem 2

from matplotlib import pyplot as plt

import cv2
import numpy as np

RED = 2
GREEN = 1
BLUE = 0

RELATIVE_LUMINANCE_CONVERSION = np.array([0.114, 0.587, 0.299])

def mapPixels(img, fn):
    """
    Given an image, applies a given transformation to each pixel in the image.
    """
    new = np.copy(img)
    rows, columns, channels = img.shape
    for y in range(rows):
        for x in range(columns):
            new[y, x] = fn(new[y, x])
    return new

def setNoBlue(img):
    """
    Sets the BLUE channel of a BGR opencv image to 0.
    """
    def fn(pixel):
        pixel[BLUE] = 0
        return pixel
    return mapPixels(img, fn)

def grayScaleWeightedMean(img):
    """
    Sets each pixel to an intensity value equal to the mean of its red, green,
    and blue values.
    """
    return mapPixels(img, np.mean)

def getRelativeLuminance(pixel):
    """
    Calculates the relative luminance of a given pixel.
    """
    return np.sum(pixel * RELATIVE_LUMINANCE_CONVERSION)

def grayScaleRelativeLuminance(img):
    """
    Sets each pixel to an intensity value equal to its relative luminance.
    """
    return mapPixels(img, getRelativeLuminance)

def shiftClampIm(img, channel, k, clampMax=255):
    """
    Shifts a channel value for each pixel in an image by a given constant,
    clamping from 0 to the given maximum.
    """
    def fn(pixel):
        pixel[channel] = min(max(0, pixel[channel] + k), clampMax)
        return pixel
    return mapPixels(img, fn)

def main():
    img = cv2.imread('images/Alice.jpg', cv2.IMREAD_COLOR)
    color_swatch = cv2.imread('images/color_swatch.jpg')

    img2 = setNoBlue(img)
    color_swatch_mean_grayscale = grayScaleWeightedMean(color_swatch)
    color_swatch_relative_luminance = grayScaleRelativeLuminance(color_swatch)
    shifted = shiftClampIm(img, RED, 120)
    shifted = shiftClampIm(shifted, GREEN, 120)
    shifted = shiftClampIm(shifted, BLUE, 120)

    hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
    recolored = shiftClampIm(hsv, 1, 25, 255)
    recolored_rgb = cv2.cvtColor(recolored, cv2.COLOR_HSV2BGR)

    cv2.imshow('original', img)
    cv2.imshow('Alice-no-blue', img2)
    cv2.imshow('color_swatch', color_swatch)
    cv2.imshow('color_swatch_grayscale_mean', color_swatch_mean_grayscale)
    cv2.imshow('color_swatch_relative_luminance',
        color_swatch_relative_luminance)
    cv2.imshow('shifted', shifted)
    cv2.imshow('recolored', recolored_rgb)
    cv2.waitKey(0)
    cv2.destroyAllWindows()

if __name__ == '__main__':
    main()
