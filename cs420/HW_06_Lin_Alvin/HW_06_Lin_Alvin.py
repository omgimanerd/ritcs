#!/usr/bin/env python3
# Author Alvin Lin (axl1439)

import math
import matplotlib.pyplot as plt
import numpy as np

from scipy.cluster.hierarchy import dendrogram, linkage

FILENAME = 'HW_AG_SHOPPING_CART_v805.csv'

ATTRS = ['ID', 'MILK', 'PETFOOD', 'VEGGIES', 'CEREAL', 'BREAD', 'RICE',
         'MEAT', 'EGGS', 'YOGURT', 'CHIPS', 'COLA', 'FRUIT']

ID = 0
MILK = 1
PETFOOD = 2
VEGGIES = 3
CEREAL = 4
BREAD = 5
RICE = 6
MEAT = 7
EGGS = 8
YOGURT = 9
CHIPS = 10
COLA = 11
FRUIT = 12

IGNORED = [ID, EGGS]

def get_data():
    """
    Returns the data in the csv file.
    """
    return np.genfromtxt(FILENAME, delimiter=',', skip_header=1)

def show_cross_correlations_info(data, latex=False):
    """
    Given the data records, this function computes the cross-correlation
    coefficients of all features with all other features and output them in a
    table. It will also find the feature pairs with the highest absolute value
    coefficients and output them.
    """
    cross_correlations = np.corrcoef(data, rowvar=False)
    if latex:
        print(' & '.join(map(str, [''] + ATTRS)))
        for i, row in enumerate(cross_correlations):
            normed = ['{:.2f}'.format(v) for v in row.tolist()]
            print(' & '.join(map(str, [ATTRS[i]] + normed)) + ' \\\\')
    else:
        print('\t'.join(map(str, [''] + ATTRS)))
        for i, row in enumerate(cross_correlations):
            normed = ['{:.2f}'.format(v) for v in row.tolist()]
            print('\t'.join(map(str, [ATTRS[i]] + normed)))
    # Output highest correlation items.
    values = {}
    for i, row in enumerate(cross_correlations):
        for j, value in enumerate(row[i + 1:]):
            if i == 0:
                continue
            values[(ATTRS[i], ATTRS[i + j + 1])] = value
    cross_correlations_sorted = sorted(
        values.items(), key=lambda item: abs(item[1]))[::-1]
    for v in cross_correlations_sorted[:10]:
        print(v)

def _get_record_euclidean_distance(r1, r2):
    """
    Calculates the squared Euclidean distance between two records, ignoring
    specific fields to prevent incorrect weighting.
    """
    sq_diff = (r1 - r2) ** 2
    sq_diff[IGNORED] = 0
    return np.sum(sq_diff)

def _get_cluster_center(cluster):
    """
    Given a cluster of points, this function computes the center of mass of the
    cluster.
    """
    return np.mean(cluster, axis=0)

def cluster_agglomerate(data, output_threshold=10):
    """
    Performs agglomerative clustering on the given data set.

    The cluster ID is recalculated according to the scipy dendrogram spec.
    """
    # Assign each data point a unique cluster ID. This array will be parallel
    # to the data array, with each value containing the cluster ID of the data
    # point at the corresponding index in the data array.
    data_cluster_ids = np.arange(0, len(data))
    # Key value pairs where the key is the cluster ID and the value is a numpy
    # array containing the cluster centroid.
    centroids = {id:item for id,item in enumerate(data)}
    # We will perform the agglomeration enough times to merge the data into a
    # single cluster.
    n_clusters = len(centroids)
    # In order to properly generate IDs for the dendrogram, a cluster generated
    # during merge step n will have ID equal to data_length + n.
    iteration = 0
    # We will also need to store merge data during each iteration for generating
    # a dendrogram.
    dendrogram_data = []
    while n_clusters != 1:
        min_distance = math.inf
        min_cluster_ids = None
        # Get a list of all current cluster IDs
        cluster_ids = np.unique(data_cluster_ids)
        # Find the minimum distance between each cluster center, while storing
        # the minimum distance found and the cluster ids of the cluster centers
        # between which the minimum distance occurs.
        for i, id1 in enumerate(cluster_ids):
            for id2 in cluster_ids[i + 1:]:
                cluster1 = centroids[id1]
                cluster2 = centroids[id2]
                distance = _get_record_euclidean_distance(cluster1, cluster2)
                if distance < min_distance:
                    min_distance = distance
                    min_cluster_ids = (id1, id2)
        # Get the cluster IDs of the two clusters being merged. For diagnostic
        # purposes, we will store the size of the two clusters being merged.
        id1, id2 = min_cluster_ids
        cluster1_size = np.sum(data_cluster_ids == id1)
        cluster2_size = np.sum(data_cluster_ids == id2)
        # Merge the two clusters and set the new cluster ID equal to
        # data_length + iteration count. This necessary for generating the
        # dendrogram since scipy's dendrogram generation cannot have identical
        # cluster IDs during later merge steps.
        new_cluster_id = len(data) + iteration
        iteration += 1
        data_cluster_ids[data_cluster_ids == id1] = new_cluster_id
        data_cluster_ids[data_cluster_ids == id2] = new_cluster_id
        # Recompute the centroids for each cluster ID and update the loop
        # condition by recounting the number of clusters.
        cluster_ids = np.unique(data_cluster_ids)
        n_clusters = len(cluster_ids)
        centroids = {id:_get_cluster_center(
            data[data_cluster_ids == id]) for id in cluster_ids}
        # Add the resulting merge information into the dendrogram data
        dendrogram_data.append(
            [id1, id2, math.sqrt(distance), cluster1_size + cluster2_size])
        # For the last 10 (default) merges, output diagnostic information.
        if n_clusters < output_threshold:
            print('-------------------------')
            print('Merged cluster sizes: {} and {}'.format(
                cluster1_size, cluster2_size))
            print('\t'.join(ATTRS))
            for center in centroids.values():
                print('\t'.join(map(lambda e: '{:0.1f}'.format(e), center)))
    return np.array(dendrogram_data)

def plot_dendrogram(data):
    """
    Plots a dendrogram using the data returned from the agglomeration
    algorithm.

    Note: I could not figure out a way to get the dendrogram data returned
    from my agglomeration algorithm to be displayed properly by scipy, so I used
    scipy's linkage method to generate the dendrogram from the input file.
    """
    plt.figure(0)
    data = linkage(data[:,1:], 'centroid')
    dendrogram(data, p=80, truncate_mode='lastp')
    plt.show()

def main():
    np.set_printoptions(threshold=99999, linewidth=190, precision=3)
    data = get_data()
    show_cross_correlations_info(data, latex=False)
    # dendrogram_data = cluster_agglomerate(data)
    # plot_dendrogram(dendrogram_data)
    plot_dendrogram(data)

if __name__ == '__main__':
    main()
