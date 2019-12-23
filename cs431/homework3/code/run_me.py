"""
Homework 3 in Python

"""
import os
from load_images import load_images
from photometric_stereo import photometric_stereo
from get_surface import get_surface
import numpy as np
from plots import plot_albedo, plot_surface, plot_surface_normals


IMG_DIR = 'croppedyale'
SUBJECTS = ['yaleB01', 'yaleB02', 'yaleB05', 'yaleB07']
MAXGRAY = 255. # normalize all values by max
NUM_IMAGES = 64
POPUP = True


IMG_DIR = 'croppedyale'
SUBJECTS = ['yaleB01', 'yaleB02', 'yaleB05', 'yaleB07']


for SUBJECT_NAME in SUBJECTS:
    #1. read in the ambient image, image array and light source directions
    ambient_image, imarray, light_dirs = \
            load_images(IMG_DIR, SUBJECT_NAME, NUM_IMAGES)

    #2. preprocess the data - subtract ambient_image from each image in imarray
    #   set any negative values to zero, rescale the resulting intensities to
    #   between 0 and 1
    imarray -= ambient_image
    imarray[imarray < 0] = 0
    imarray = np.divide(imarray, 255)

    #3. estimate the albedo and surface normals.
    albedo_image, surface_normals = photometric_stereo(imarray, light_dirs)

    #4. reconstruct surface the height map (you need to fill in get_surface for different
    #   integration methods)
    height_map = get_surface(surface_normals)

    #5. plot/display albedo, surface normals and surface
    plot_albedo(albedo_image, MAXGRAY, SUBJECT_NAME, POPUP)
    plot_surface_normals(surface_normals, SUBJECT_NAME, POPUP)
    plot_surface(height_map, MAXGRAY, SUBJECT_NAME, POPUP)
