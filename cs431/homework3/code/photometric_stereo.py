"""
Homework 3 - Complete this file

"""
import numpy as np

def photometric_stereo(imarray, light_dirs):
    """
    :param imarray (numpy.ndarray of floats): Nimages x h x w array of images
    :param light_dirs

    :return albedo_image (numpy.ndarray of floats): array of size h x w
    :return surface_normals (numpy.ndarray of floats): array of size 3 x h x w
                of surface normals' xyz components
    """

    num_images, height, width = imarray.shape

    # solve for [rho * N(x,y)] using linear least squares:
    # Rearrange the dimensions of the image matrix to (192 x 168 x 64)
    i = np.moveaxis(imarray, 0, -1)
    # Tranpose the light direction matrix to (64 x 3)
    V = np.array(light_dirs).T
    # Solve the system of linear equations to obtain g as (192 x 168 x 3)
    g = np.array([
        [np.linalg.lstsq(V, i[row,col], rcond=None)[0] for col in range(width)]
         for row in range(height)])

    # calculate albedo rho
    albedo_image = np.linalg.norm(g, axis=2)

    # calculate surface normals
    surface_normals = np.copy(g)
    for row in range(height):
        for col in range(width):
            surface_normals[row,col] /= albedo_image[row,col]

    return albedo_image, surface_normals.T
