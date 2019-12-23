#!/usr/bin/env python3
# Principles of Data Mining with Dr. Kinsman: HW 03
# Author: Alvin Lin (axl1439)

import math
import matplotlib.pyplot as plt
import numpy as np

SPEED = 0
CAR_NOT_TRUCK = 1
DISTRACTED = 2
COMMERCIAL = 3
WANT_TO_SPEED = 4

FILENAME = 'HW03_Vehicle_Data__v25.csv'

def get_csv_data():
    """
    Returns the data in the csv file.
    """
    return np.genfromtxt(FILENAME, delimiter=',', skip_header=1)

def get_p_want_to_speed(data, threshold):
    """
    Given the data and a threshold, compute the probability that cars
    traveling at speeds less than this threshold are speeding.
    """
    lt = data[data[:,SPEED] < threshold]
    n_want_to_speed = np.sum(lt[:,WANT_TO_SPEED])
    return n_want_to_speed / len(lt)

def get_misclassification_rate(data, threshold):
    """
    Given the data and a threshold, compute the misclassification rate at
    that threshold.
    """
    error = get_p_want_to_speed(data, threshold)
    return 1 - error if error > 0.5 else error

def get_gini_index(data, threshold):
    """
    Given the data and a threshold, compute the Gini index at that threshold.
    """
    c0 = get_p_want_to_speed(data, threshold)
    return 1 - (c0 ** 2) - ((1 - c0) ** 2)

def get_entropy(data, threshold):
    """
    Given the data and a threshold, compute the entropy at that threshold.
    """
    c0 = get_p_want_to_speed(data, threshold)
    if c0 == 0 or c0 == 1:
        return 0
    c1 = 1 - c0
    return -((c0 * math.log(c0, 2)) + (c1 * math.log(c1, 2)))

def plot_cost_functions(data):
    """
    Plots the misclassification rate, Gini indices, and entropies for
    thresholds 41 to 85 for the given data.
    """
    thresholds = np.arange(41, 86)
    misclassification_rates = [
        get_misclassification_rate(
            data, threshold) for threshold in thresholds]
    gini_indices = [
        get_gini_index(data, threshold) for threshold in thresholds]
    entropies = [
        get_entropy(data, threshold) for threshold in thresholds]
    plt.figure(0)
    plt.title('Misclassification rates, Gini indices, and entropy')
    plt.xlabel('Speed Threshold')
    plt.plot(thresholds, misclassification_rates,
             label='Misclassification Rate')
    plt.plot(thresholds, gini_indices,
             label='Gini Indicies')
    plt.plot(thresholds, entropies,
             label='Entropies')
    plt.legend(loc='upper right')

##########################
## MIXED COST FUNCTIONS CODE ####
##########################

def get_mixed_p_want_to_speed(data, threshold):
    """
    Given the data and a threshold, compute the probability that cars
    traveling at speeds less than, and greater than or equal to the
    threshold are speeding.
    """
    lt = data[data[:,SPEED] < threshold]
    gte = data[data[:,SPEED] >= threshold]
    n_want_to_speed_lt = np.sum(lt[:,WANT_TO_SPEED])
    n_want_to_speed_gte = np.sum(gte[:,WANT_TO_SPEED])
    return (n_want_to_speed_lt / len(lt), n_want_to_speed_gte / len(gte))

def get_mixed_misclassification_rate(data, threshold):
    """
    Given the data and a threshold, compute the mixed misclassification
    rate at that threshold.
    """
    lt, gte = get_mixed_p_want_to_speed(data, threshold)
    lt = 1 - lt if lt > 0.5 else lt
    gte = 1 - gte if gte > 0.5 else gt
    return (lt + gte) / 2

def get_mixed_gini_index(data, threshold):
    """
    Given the data and a threshold, compute the mixed Gini index at that
    threshold.
    """
    lt, gte = get_mixed_p_want_to_speed(data, threshold)
    lt_gini_index = 1 - (lt ** 2) - ((1 - lt) ** 2)
    gte_gini_index = 1 - (gte ** 2) - ((1 - gte) ** 2)
    return (lt_gini_index + gte_gini_index) / 2

def get_mixed_entropy(data, threshold):
    """
    Given the data and a threshold, compute the mixed entropy at that
    threshold.
    """
    lt, gte = get_mixed_p_want_to_speed(data, threshold)
    log2 = lambda x: 0 if x == 0 else math.log(x, 2)
    lt_entropy = -((lt * log2(lt)) + ((1 - lt) * log2(1 - lt)))
    gte_entropy = -((gte * log2(gte)) + ((1 - gte) * log2(1 - gte)))
    return (lt_entropy + gte_entropy) / 2

def plot_mixed_cost_functions(data):
    """
    Plots the mixed badness measures for thresholds 41 to 85 for the given
    data.
    """
    thresholds = np.arange(41, 86)
    misclassification_rates = [
        get_mixed_misclassification_rate(
            data, threshold) for threshold in thresholds]
    gini_indices = [
        get_mixed_gini_index(data, threshold) for threshold in thresholds]
    entropies = [get_mixed_entropy(
        data, threshold) for threshold in thresholds]
    plt.figure(1)
    plt.title(
        'BONUS: Mixed misclassification rate, Gini indices, and entropy')
    plt.xlabel('Speed Threshold')
    plt.plot(thresholds, misclassification_rates,
             label='Mixed Misclassification Rate')
    plt.plot(thresholds, gini_indices,
             label='Mixed Gini Indices')
    plt.plot(thresholds, entropies,
             label='Mixed Entropies')
    plt.legend(loc='upper right')

def main():
    data = get_csv_data()
    plot_cost_functions(data)
    plot_mixed_cost_functions(data)
    plt.show()

if __name__ == '__main__':
    main()
