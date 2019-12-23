#!/usr/bin/env python3
# Principles of Data Mining with Dr. Kinsman: HW 02
# Author: Alvin Lin (axl1439)
# Date: 09/23/2018

import math
import matplotlib.pyplot as plt
import numpy as np

SPEED = 0
CAR_NOT_TRUCK = 1
DISTRACTED = 2
COMMERCIAL = 3
WANT_TO_SPEED = 4

FILENAME = 'vehicle_data_v2181.csv'

def getCsvData():
    """
    Returns the data in the csv file.
    """
    return np.genfromtxt(FILENAME, delimiter=',', skip_header=1)

def visualizeData(data, threshold=None):
    """
    Given the data and an optional threshold, this function plots a histogram
    of the drivers by speed. Also indicates the drivers that intend to speed
    and the ones that do not.

    If threshold is provided, then it will be drawn on the plot as a red line.
    """
    # data[:,WANT_TO_SPEED] uses numpy indexing to select the column
    # corresponding to whether or not the driver intended to speed.
    # data[:,WANT_TO_SPEED] == 1 generates a boolean array using that slice
    # allowing us to select all the rows from the data where the drivers
    # intended to speed.
    dangerous = data[data[:,WANT_TO_SPEED] == 1]
    safe = data[data[:,WANT_TO_SPEED] == 0]
    bins = np.arange(40, 80, 2.5)
    plt.figure(0)
    plt.title('Distribution of drivers by speeds')
    plt.xlabel('Speed (mph)')
    plt.ylabel('Frequency')
    plt.hist([dangerous[:,SPEED], safe[:,SPEED]], bins,
            label=['intended to speed', 'safe'])
    if threshold:
        plt.axvline(threshold, color='r')
    plt.legend()
    plt.xticks(np.arange(40, 81, 5))

def visualizeCostWithThreshold(data, costFns, labels):
    """
    This function plots the cost function as a function of the threshold used
    given an array of cost functions to use and their corresponding labels.
    """
    assert len(costFns) == len(labels)
    thresholds = np.arange(40, 81, 1)
    costs = [
        [fn(*testThreshold(
            data, threshold)) for threshold in thresholds] for fn in costFns
    ]
    plt.figure(1)
    plt.title('Cost as a function of the speed threshold used')
    plt.xlabel('Speed Threshold')
    plt.ylabel('Cost Function')
    for i, cost in enumerate(costs):
        plt.plot(thresholds, cost, label=labels[i])
    plt.legend(loc='upper right')

def visualizeROCWithCost(data, costs, colors):
    """
    This function plots the ROC curve of the thresholds assuming a simple
    binary separation. Also plots given min cost points with labels.
    """
    thresholds = np.arange(40, 81, 5)
    curve = np.array(
        [testThreshold(data, threshold) for threshold in thresholds])
    false_pos = curve[:,0]
    true_pos = curve[:,2]
    plt.figure(2)
    plt.title('ROC curve')
    plt.xlabel('False Alarm Rate (False Positives)')
    plt.ylabel('Correct Hit Rate (True Positives)')
    for label, x, y in zip(thresholds, false_pos, true_pos):
        plt.annotate(label, xy=(x, y + 2))
    plt.plot(false_pos, true_pos, data=thresholds)
    for i, cost in enumerate(costs):
        plt.plot(cost[0], cost[2], colors[i])

def costFn1(false_pos, false_neg, true_pos, true_neg):
    """
    Given the false positives, false negatives, true positives, and true
    negatives, return a cost that weights false positives 3x more than false
    negatives.
    """
    return 3 * false_pos + false_neg

def costFn2(false_pos, false_neg, true_pos, true_neg):
    """
    Given the false positives, false negatives, true positives, and true
    negatives, return a cost that weights false negatives 3x more than false
    positives.
    """
    return 3 * false_neg + false_pos

def costFn3(false_pos, false_neg, true_pos, true_neg):
    """
    Cost function weighting false positives and false negatives equally
    """
    return false_neg + false_pos

def testThreshold(data, threshold):
    """
    Given the data and a speed threshold past which we will pull people over,
    this function returns the amount of false positives, false negatives,
    true positives, and true negatives that will result from using that speed
    threshold.
    """
    # Separate out the speeders from non-speeders using our threshold and
    # numpy boolean indexing
    speeding = data[data[:,SPEED] > threshold]
    not_speeding = data[data[:,SPEED] <= threshold]
    # Check how many of the speeders intended to speed and sum up the array to
    # get a count of the true positives.
    true_pos = np.sum(speeding[:,WANT_TO_SPEED] == 1)
    # Subtract the amount of true positives from the speeders to get a count of
    # false positives.
    false_pos = len(speeding) - true_pos
    # Get a count of people who were not speeding to did not intend to speed to
    # get a count of true negatives.
    true_neg = np.sum(not_speeding[:,WANT_TO_SPEED] == 0)
    # Subtract the count of true negatives from the number of people not
    # speeding to get a count of people who intended to speed but were not
    # actually speeding (false negatives). Should we really pull these people
    # over?
    false_neg = len(not_speeding) - true_neg
    return false_pos, false_neg, true_pos, true_neg

def getThresholdWithOtsus(data):
    """
    This function implements Otsu's method for one dimensional clustering
    and calculates the best threshold to split the given input data.
    It assumes the input data is a 1-d nparray.

    Copied from HW 01
    """
    best_mixed_variance = math.inf
    best_threshold = None
    total_points = len(data)
    for threshold in data:
        # data < threshold returns a boolean array where the True elements
        # in the array represent the elements in the data array less than
        # the threshold. We can use this as an index to select all the
        # elements in the array less than the threshold and greater than
        # the threshold, which we use for Otsu's Method.
        under_threshold = data[data <= threshold]
        over_threshold = data[data > threshold]
        if len(under_threshold) == 0 or len(over_threshold) == 0:
            continue
        weight_under = len(under_threshold) / total_points
        weight_over = 1 - weight_under
        var_under = np.var(under_threshold)
        var_over = np.var(over_threshold)
        mixed_variance = weight_under * var_under + weight_over * var_over
        if mixed_variance < best_mixed_variance:
            best_mixed_variance = mixed_variance
            best_threshold = threshold
    return int(round(best_threshold))

def getThresholdWithCost(data, cost_fn):
    """
    Given the data and a cost function, this function exhaustively tries each
    data point as a threshold to pull people over and finds a threshold that
    minimizes the cost function. The given cost function should be a callback
    that takes the false positives, false negatives, true positives, and true
    negatives and returns a value that takes each of those values into account.
    """
    best_cost = math.inf
    best_cost_data = None
    best_threshold = 0
    for threshold in np.round(data[:,SPEED]):
        cost_data = testThreshold(data, threshold)
        # Calculate the cost of using this threshold using the given cost fn
        cost = cost_fn(*cost_data)
        if cost < best_cost:
            best_cost = cost
            best_cost_data = cost_data
            best_threshold = threshold
    return int(best_threshold), best_cost_data

def main():
    data = getCsvData()
    t1, cost_data1 = getThresholdWithCost(data, costFn1)
    t2, cost_data2 = getThresholdWithCost(data, costFn2)
    t3 = getThresholdWithOtsus(data[:,SPEED])
    print('Data size: {}'.format(len(data)))
    print('Threshold with higher false positive cost: {}'.format(t1))
    print('False positives: {}'.format(cost_data1[0]))
    print('False negatives: {}'.format(cost_data1[1]))
    print('Threshold with higher false negative cost: {}'.format(t2))
    print('False positives: {}'.format(cost_data2[0]))
    print('False negatives: {}'.format(cost_data2[1]))
    print('Threshold from Otsus: {}'.format(t3))
    cost_data3 = testThreshold(data, t3)
    print('False positives: {}'.format(cost_data3[0]))
    print('False negatives: {}'.format(cost_data3[1]))
    visualizeData(data, threshold=t1)
    visualizeCostWithThreshold(
        data,
        [costFn1, costFn2, costFn3],
        [
            'Weighting false positives more',
            'Weighting false negatives more',
            'Weighting false negatives and positives equally'
        ]
    )
    visualizeROCWithCost(data, costs=[cost_data1, cost_data2], colors=[
        'ro', 'go'
    ])
    plt.show()

if __name__ == '__main__':
    main()
