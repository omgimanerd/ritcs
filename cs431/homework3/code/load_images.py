"""
Homework 3 - Complete this file

"""
from read_pgm import getpgmraw
from sph2cart import sph2cart
import os
import numpy as np
import re

REGEX = re.compile('^[a-zA-Z0-9_]+A([-+0-9]{4})E([-+0-9]{3})\.pgm$')

def parse_azimuth_elevation(filename):
    """
    Uses a regex to parse the azimuth and elevation data from the given file
    name.

    Returns (azimuth, elevation) in degrees.
    """
    match = REGEX.match(filename)
    return int(match.group(1)), int(match.group(2))

def load_images(img_dir, subject_name, num_images):
    """Input: image directory, subdir, total number of images.

    Returns (amb_image, img_array, light_dirs).

    amb_image (numpy.ndarray): (height x width) 2D array of grayscale
                                8-bit ints for ambient image
    img_array (numpy.ndarray): (num_images x height x width) 3D array of
                                grayscale ints for num_images
    light_dirs (numpy.ndarray): (num_images x 3) 2D array of light source
                                    vector components (floats)
    """
    dir_path = os.path.dirname(os.path.realpath(__file__))
    dir_path = os.path.join(dir_path, img_dir, subject_name)

    # get ambient image
    amb_image_name = '{}_P00_Ambient.pgm'.format(subject_name)
    amb_image = getpgmraw(os.path.join(dir_path, amb_image_name))

    # create array of images
    # I ended up not using the num_images parameter since I read the
    # contents of the directory and filtered out the requisite image paths.
    img_array_names = os.listdir(dir_path)
    img_array_names = filter(
        lambda x: amb_image_name not in x, img_array_names)
    img_array_names = sorted(filter(
        lambda x: x.endswith('.pgm'), img_array_names))
    img_array = np.array([
        getpgmraw(os.path.join(dir_path, name)) for name in img_array_names])

    # convert lighting source from spherical to cartesian coords and
    # store light_dirs
    azimuth, elevation = zip(
        *[parse_azimuth_elevation(name) for name in img_array_names])
    azimuth, elevation = np.radians(azimuth), np.radians(elevation)
    light_dirs = sph2cart(azimuth, elevation, 1.0)

    return amb_image, img_array, light_dirs

if __name__ == '__main__':
    load_images('croppedyale', 'yaleB01', 64)
