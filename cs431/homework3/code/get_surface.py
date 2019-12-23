"""
Homework 3 - Complete this file

"""
import numpy as np


def get_surface(surface_normals):
    """ Build up the surface depth.
    :param surface_normals (numpy.ndarray of floats): 3 x h x w components
                    of unit normals
    :return height_map (numpy.ndarray of floats): h x w height map of object
    """
    # Transpose the surface normals matrix back to h x w x 3
    surface_normals = surface_normals.T
    height, width, _ = surface_normals.shape
    # Prefill the height map matrix with zeros
    height_map = np.zeros((height, width), dtype=float)
    # Compute the left column
    for i in range(1, height):
        # Compute q as N_2 / N_3
        q = surface_normals[i,0,1] / surface_normals[i,0,2]
        # Compute the corresponding height map value
        height_map[i,0] = height_map[i - 1,0] + q
    # Compute each row and element
    for i in range(height):
        for j in range(1, width):
            # Compute p as N_1 / N_3
            p = surface_normals[i,j,0] / surface_normals[i,j,2]
            # Compute the corresponding height map value
            height_map[i,j] = height_map[i,j - 1] + p
    return height_map
