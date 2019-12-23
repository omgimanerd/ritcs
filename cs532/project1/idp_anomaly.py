#!/usr/bin/env python3
# Anomaly based IDP pseudocode structure. This anomaly based IDP generates a
# measure of normal traffic by identifying all normal traffic in the training
# data and taking their median. We determine if a connection is anomalous by
# taking its L1 Minkowski distance from the normal and seeing if it is above
# some threshold. The threshold was determined by brute force testing thresholds
# until we found one that minimized the misclassification rate.
#
# Author: Alvin Lin (axl1439)

from constants import *
from base import get_training_data, get_validation_data

from sklearn import decomposition

import math
import numpy as np

BEST_THRESHOLD = 2230

def get_normal(sample=10000):
    """
    Determines the normal traffic measure from the training data. We default to
    taking a 10000 record sample of the training data.
    """
    data = get_training_data(slice=sample)
    normal_traffic = data[data[:,ATTACK] == ATTACK_TYPES_MAP['normal']]
    return np.median(normal_traffic[:,:-1], axis=0)

def is_anomalous(normal, connection):
    """
    Determines whether a connection is anomalous based on its L1 Minkowski
    distance from the norm.
    """
    assert len(connection) == len(normal)
    diff = np.sum(abs(connection) - normal)
    return diff > BEST_THRESHOLD

###
### The methods below were used to generate the ROC curve and determine the
### best threshold to use.
###

def test_threshold(normal, data, threshold):
    true_pos = 0
    false_pos = 0
    true_neg = 0
    false_neg = 0
    for connection in data:
        diff = np.sum(abs(connection[:-1] - normal))
        if diff > threshold:
            if connection[ATTACK] != ATTACK_TYPES_MAP['normal']:
                true_pos += 1
            else:
                false_pos += 1
        else:
            if connection[ATTACK] != ATTACK_TYPES_MAP['normal']:
                false_neg += 1
            else:
                true_neg += 1
    return true_pos, true_neg, false_pos, false_neg

def test_best_threshold():
    """
    Tests all filtering thresholds to determine the one with the lowest
    misclassification rate. All data is printed to stdout.
    """
    normal = get_normal()
    data = get_training_data()

    best_threshold = None
    misclass = math.inf
    for threshold in range(0, 10000, 10):
        true_pos, true_neg, false_pos, false_neg = test_threshold(
            normal, data, threshold)
        if false_pos + false_neg < misclass:
            misclass = false_pos + false_neg
            best_threshold = threshold
        print(threshold, true_pos, true_neg, false_pos, false_neg)
    print('Using a threshold of {} has the lowest misclassification rate')

def main():
    normal = get_normal()
    data = get_validation_data()
    true_pos, true_neg, false_pos, false_neg = test_threshold(
        normal, data, BEST_THRESHOLD)
    data_size = true_pos + true_neg + false_pos + false_neg
    print('True Positives: {}'.format(true_pos))
    print('True Negatives: {}'.format(true_neg))
    print('False Positives: {}'.format(false_pos))
    print('False Negatives: {}'.format(false_neg))
    print('Accuracy: {}'.format((true_pos + true_neg) / data_size))
    print('Misclassification Rate: {}'.format(
        (false_pos + false_neg) / data_size))
    print('True Positive Ratio: {}'.format(true_pos / (true_pos + false_neg)))
    print('False Positive Ratio: {}'.format(false_pos / (false_pos + true_neg)))
    print('Precision: {}'.format(true_pos / (true_pos + false_pos)))

if __name__ == '__main__':
    main()
