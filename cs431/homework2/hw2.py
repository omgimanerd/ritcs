#!/usr/bin/env python3
# Author: Alvin Lin (axl1439)
# Intro to Computer Vision: Homework 2

import cv2
import matplotlib.pyplot as plt
import numpy as np
import sklearn.cluster as skcluster

from makeLMfilters import makeLMfilters

CROP_BOUNDARY = 25

def kmeans(k, points, cutoff=0.1):
    """
    Self-implemented kmeans clustering algorithm. Since I have implemented
    this in the past, it is heavily based on:
    https://gist.github.com/omgimanerd/ff0b9a8dd225f2d2e94aa4be76bc91d9
    """
    centroids = points[np.random.choice(points.shape[0], k)]
    shift = cutoff + 1
    while shift > cutoff:
        # Calculate which centroid each point is closest to. This generates
        # an array of indices representing which centroid the point is
        # closest to. Uses Euclidean distance as the distance metric.
        point_closest_centroid = np.array([
            ((centroids - p) ** 2).sum(axis=1).argmin() for p in points
        ])
        # Uses the array of indexes to aggregate the points into k clusters
        clusters = np.array([
            points[point_closest_centroid == i] for i in range(k)])
        # Calculates the centroids of the new clusters.
        new_centroids = np.array([
            cluster.mean(axis=0) for cluster in clusters
        ])
        # Calculate the average amount that each centroid shifted using
        # Euclidean distance. Stop the algorithm when the shift is lower
        # than the given cutoff.
        shift = ((new_centroids - centroids) ** 2).sum(axis=0).mean()
        centroids = new_centroids
    return centroids, clusters

def test_kmeans():
    """
    Demonstrates the self-implemented kmeans algorithm in action clustering
    a few clusters of points generated using Gaussian noise.

    Requires sklearn to generate sample data.
    I recommend running multiple times to see varying clustering results.
    """
    from sklearn.datasets.samples_generator import make_blobs
    centers = [[25, 25], [50, 50], [2, 75]]
    samples, _ = make_blobs(100, centers=centers, cluster_std=4)
    centroids, clusters = kmeans(3, samples, cutoff=0.01)
    style = ['ro', 'go', 'bo']
    plt.figure(0)
    for cluster, style in zip(clusters, style):
        plt.plot(cluster[:,0], cluster[:,1], style)
    plt.plot(centroids[:,0], centroids[:,1], 'y*')
    plt.show()

def segmentImg(img_grayscale, k=16):
    """
    Given a graysacle image and a number of segments to return (for kmeans),
    this function returns a 2d array the same size as the image containing
    indexes of the cluster that each pixel belongs to.
    """
    # Read in the image and its dimensions.
    rows, cols = img_grayscale.shape
    # Get the filter bank, which comes out as 49 x 49 x 48.
    # We need to transpose it to 48 x 49 x 49 so that each first axis
    # element is a different filter. This allows us to simply iterate
    # through the first axis to get each filter.
    filters = makeLMfilters().transpose()
    # Apply all the filters to each image, responses becomes a
    # (48, rows, cols)) sized bank.
    responses = np.array([
        cv2.filter2D(img_grayscale, -1, filter) for filter in filters
    ])
    # We need to transpose it to image_x * image_y * 48 to get a
    # image sized matrix with a 48 dimensional vector for each
    # (x, y) pair.
    transposed_responses = responses.transpose()
    # In order to apply the Kmeans algorithm though, we have to flatten it
    # into a (rows * cols, 48) sized array.
    data_matrix = np.concatenate(transposed_responses, axis=0)
    # Run Kmeans on this data matrix.
    kmeans = skcluster.KMeans(init='k-means++', n_clusters=k, random_state=0)
    # Reshape the result of the kmeans clustering back into a (rows, cols)
    # array and return it. This array is now (rows, cols) shaped and contains
    # indexes at each coordinate of the cluster it belongs to.
    return kmeans.fit_predict(data_matrix).reshape(cols, rows).transpose()

def visualize_segments(segments):
    """
    Visualize the segmentation of the image.
    """
    multiplier = 255 / np.max(segments)
    segments = (segments * multiplier).astype(np.uint8)
    cv2.imshow('Segmentation', segments)

def visualize_segments_separate(segments):
    """
    For each separate segment present in the segmentation clusters,
    create an image showing only those segments and display it.
    """
    for index in np.arange(np.max(segments)):
        mask = (segments == index).astype(np.uint8) * 200
        cv2.imshow('Segment ID {}'.format(index), mask)

def transferImg(foreground_indices, segment_indices, source, target,
                offset=(0, 0)):
    # Crop the segmented image (containing the cluster indexes) and the
    # source image.
    rows, cols = segment_indices.shape
    # segment_indices = segment_indices[
    #     CROP_BOUNDARY:rows - CROP_BOUNDARY,
    #     CROP_BOUNDARY:cols - CROP_BOUNDARY]
    # source = source[
    #     CROP_BOUNDARY:rows - CROP_BOUNDARY,
    #     CROP_BOUNDARY:cols - CROP_BOUNDARY]
    # c_rows, c_cols = segment_indices.shape
    # Resize them to 0.5 times their original size.
    # Due to a bug in my opencv version, resizing the image errored out:
    # cv2.error: OpenCV(3.4.3-dev)
    # /home/omgimanerd/opencv/modules/imgproc/src/resize.cpp:4102: error:
    # (-215:Assertion failed) func != 0 in function 'resize'
    # The resizing code has been commented out.
    # resized_segment_indices = cv2.resize(cropped_segment_indices,
    #                                      (rows // 2, cols // 2))
    # resized_source = cv2.resize(cropped_source,
    #                             (rows // 2, cols // 2),
    #                             interpolation=cv2.INTER_AREA)
    # Transfer over the source to the target.
    masks = np.array(
        [segment_indices == index for index in foreground_indices])
    selected_region = np.any(masks, axis=0)
    for x in range(rows):
        for y in range(cols):
            if selected_region[x][y]:
                target[x + offset[0], y + offset[1]] = source[x, y]
    return target

def main():
    bg_color = cv2.imread('images/bg.jpg', cv2.IMREAD_COLOR)
    bg2_color = cv2.imread('images/bg2.jpg', cv2.IMREAD_COLOR)

    cheetah_gray = cv2.imread('images/cheetah.jpg', cv2.IMREAD_GRAYSCALE)
    cheetah_color = cv2.imread('images/cheetah.jpg', cv2.IMREAD_COLOR)
    cheetah_segments = segmentImg(cheetah_gray, k=40)
    cheetah_transposed = transferImg(
    [37, 35, 33, 30, 26, 25, 24, 23, 19, 17, 16, 11, 7, 6, 5, 4, 0],
    cheetah_segments, cheetah_color, bg_color, offset=(120, 60))
    visualize_segments(cheetah_segments)
    cv2.imshow('cheetah transposed', cheetah_transposed)

    gecko_gray = cv2.imread('images/gecko.jpg', cv2.IMREAD_GRAYSCALE)
    gecko_color = cv2.imread('images/gecko.jpg', cv2.IMREAD_COLOR)
    gecko_segments = segmentImg(gecko_gray, k=40)
    visualize_segments(gecko_segments)
    gecko_transposed = transferImg(
        [38, 37, 35, 33, 32, 31, 29, 28, 27, 25, 24, 22, 21,
         20, 18, 17, 16, 15, 14, 13, 12, 11, 10, 7, 6, 4, 2, 1],
        gecko_segments, gecko_color, bg2_color, offset=(120, 60))
    cv2.imshow('gecko transposed', gecko_transposed)

    cv2.waitKey(0)
    cv2.destroyAllWindows()

if __name__ == '__main__':
    main()
