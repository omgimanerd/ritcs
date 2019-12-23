#!/usr/bin/env python3
# Author Alvin Lin (axl1439)

import math
import matplotlib.pyplot as plt
import numpy as np

from sklearn.cluster import KMeans

np.set_printoptions(threshold=99999, linewidth=190, precision=3)

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

def get_data():
    """
    Returns the data in the csv file.
    """
    return np.genfromtxt(FILENAME, delimiter=',', skip_header=1)

def plot_eigenvalue_cumsum(eigenvalues):
    """
    Normalizes the eigenvalues by dividing by the sum of their absolute values
    and plots their cumulative sum.

    Arguments:
        eigenvalues - n_eigenvalues x dimensions matrix, each row of this
                      nparray should be a separate eigenvalue.
    """
    normalized = eigenvalues / np.sum(abs(eigenvalues))
    cumsum = np.cumsum(normalized)
    plt.figure(0)
    plt.xlabel('Eigenvalue')
    plt.ylabel('Cumulative sum')
    plt.title('Eigenvalue cumulative sum')
    plt.plot(np.arange(len(cumsum)), cumsum)

def plot_transformed_data(transformed_data):
    """
    Plots the transformed data after it has been projected onto the selected
    eigenvectors.
    """
    plt.figure(1)
    plt.title('Transformed data')
    plt.xlabel('Eigenvector 1')
    plt.ylabel('Eigenvector 2')
    plt.plot(*transformed_data.T, 'o')

def plot_clustered_transformed_data(transformed_data, labels):
    plt.figure(2)
    plt.title('Transformed data (clustered)')
    plt.xlabel('Eigenvector 1')
    plt.ylabel('Eigenvector 2')
    styles = ['ro', 'go', 'bo']
    cluster_labels = np.unique(labels)
    assert len(cluster_labels) == len(styles)
    for i, label in enumerate(cluster_labels):
        plt.plot(*transformed_data[labels == label].T, styles[i])

def main():
    # Get the data from the records file, trimming the ID field
    data = get_data()[:,1:]
    # Compute the cross correlation matrix
    cross_correlations = np.corrcoef(data, rowvar=False)
    assert cross_correlations.shape == (12,12)
    # Compute the eigenvalues and eigenvectors of the cross correlation matrix.
    eigenvalues, eigenvectors = np.linalg.eig(cross_correlations)
    # Sort the eigenvalues and corresponding eigenvectors from high to low by
    # the absolute values of the eigenvalues.
    eigs = sorted(zip(eigenvalues, eigenvectors.T),
                  key=lambda pair: abs(pair[0]),
                  reverse=True)
    eigenvalues, eigenvectors = map(np.array, zip(*eigs))

    # Plot the cumulative sum of the normalized eigenvalues.
    plot_eigenvalue_cumsum(eigenvalues)

    # Print out the two eigenvectors corresponding to the two largest
    # eigenvalues.
    print('First two eigenvectors:')
    selected_eigenvectors = eigenvectors[:2]
    print(selected_eigenvectors)

    # Project the data onto the two selected eigenvectors and plot it.
    transformed_data = selected_eigenvectors.dot(data.T).T
    plot_transformed_data(transformed_data)

    # Cluster the projected data using KMeans clustering.
    kmeans = KMeans(n_clusters=3, random_state=0).fit(transformed_data)
    # Plot the clustered data.
    plot_clustered_transformed_data(transformed_data, kmeans.labels_)
    # Print out the cluster centers.
    centers = kmeans.cluster_centers_
    print('Cluster Centers:')
    print(centers)
    # Calculate the cluster prototypes in the original space
    prototypes = centers.dot(selected_eigenvectors)
    print('Cluster Prototypes:')
    print('\t'.join(ATTRS[1:]))
    for prototype in prototypes:
        # prototype += abs(min(prototype))
        # print(' & '.join(['{:.2f}'.format(e) for e in prototype]))
        print('\t'.join(['{:.2f}'.format(e) for e in prototype]))
    plt.show()

if __name__ == '__main__':
    main()
