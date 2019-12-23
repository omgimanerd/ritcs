#!/usr/bin/env python3
# Generates a ROC curve for the data generated from running the anomaly
# based IDP on the validation data.
# Author: Alvin Lin (axl1439)

import matplotlib.pyplot as plt
import numpy as np

THRESHOLD = 0
TRUE_POS = 1
TRUE_NEG = 2
FALSE_POS = 3
FALSE_NEG = 4

FP_TN = 19136
TP_FN = 80864

def main():
    data = np.loadtxt('roc')
    false_positives = data[:,FALSE_POS]
    false_positives_rate = false_positives / FP_TN
    true_positives = data[:,TRUE_POS]
    true_positives_rate = true_positives / TP_FN

    misclass = data[:,FALSE_POS] + data[:,FALSE_NEG]
    best_threshold = data[np.argmin(misclass)]
    best_threshold_x = best_threshold[FALSE_POS] / FP_TN
    best_threshold_y = best_threshold[TRUE_POS] / TP_FN
    print(best_threshold[THRESHOLD])
    print(best_threshold_x, best_threshold_y)

    plt.figure(0)
    plt.title('ROC curve using various thresholds for anomaly based IDP')
    plt.xlabel('False Positive Rate')
    plt.ylabel('True Positive Rate')
    plt.xlim(0, 1)
    plt.ylim(0, 1)
    plt.plot(false_positives_rate, true_positives_rate)
    plt.plot(best_threshold_x, best_threshold_y, 'ro')
    plt.show()

if __name__ == '__main__':
    main()
