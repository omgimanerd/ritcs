#!/usr/bin/env python3
# Author: Alvin Lin (axl1439)

import math
import matplotlib.pyplot as plt
import numpy as np

FILENAME = 'HW_01_Unknown_Data.csv'

ID = 0
STOP_DURATION = 1

def getCsvData():
  """
  Returns the data in the csv file.
  """
  return np.genfromtxt(FILENAME, delimiter=',', skip_header=1)

def otsus(data):
  """
  This function implements Otsu's method for one dimensional clustering
  and calculates the best threshold to split the given input data.
  It assumes the input data is a 1-d nparray.
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
  return best_threshold

def visualizeClusters(clusters):
  plt.figure(1)
  plt.title('Time Stopped During A Stop')
  plt.xlabel('Time Stopped (seconds)')
  plt.ylabel('Gaussian Noise')
  colors = ['ro', 'go', 'bo', 'yo']
  for i, c in enumerate(clusters):
    plt.plot(c, np.random.normal(loc=0, scale=1, size=len(c)), colors[i])
  plt.show()

def analyzeClusters(clusters):
  """
  Reports an analysis of the clusters determined by Otsu's method.
  """
  for i, cluster in enumerate(sorted(clusters, key=np.mean)):
    print('Cluster {}'.format(i))
    print('Num points: {}'.format(len(cluster)))
    print('Average value: {}'.format(np.mean(cluster)))
    print('Standard deviation: {}'.format(np.std(cluster)))
    print('Min: {}'.format(min(cluster)))
    print('Max: {}'.format(max(cluster)))
    print('-' * 20)
  
def main():
  data = getCsvData()
  """
  data[:,1] > 0.5 selects the first column of the array (durations)
  and creates a boolean vector array corresponding to the entries
  where the duration is greater than 0.5. Using that as an index
  selects all the rows in data that satisfy the duration condition.
  This is a preliminary filter which removes all time durations less
  than 0.5 because stops for that small amount of time are considered
  noise and irrelevant to our analysis.
  """
  data = data[data[:,1] > 0.5]
  durations = data[:,1]
  clusters = []
  thresholds = []
  for i in range(3):
    threshold = otsus(durations)
    thresholds.append(threshold)
    low_half = durations[durations <= threshold]
    high_half = durations[durations > threshold]
    if i == 2:
      clusters.append(low_half)
      clusters.append(high_half)
      break
    if len(low_half) > len(high_half):
      clusters.append(high_half)
      durations = low_half
    else:
      clusters.append(low_half)
      durations = high_half
  analyzeClusters(clusters)
  print('Thresholds')
  for threshold in thresholds:
    print(threshold)
  visualizeClusters(clusters)

if __name__ == '__main__':
  main()
