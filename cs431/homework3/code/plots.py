"""
Homework 3 Helper file in Python
"""


# Plotting utilities.

import matplotlib.pyplot as plt
from PIL import Image
import numpy as np
import os
from mpl_toolkits.mplot3d import Axes3D
from matplotlib._png import read_png


#create a new folder 'figs' to write your output results
IMG_DIR = 'figs'
DPI = 96 # NOTE: varies by monitor, find by running in shell: xrdb -query | grep dpi
# DPI = 192 

curdir = os.path.dirname(os.path.realpath(__file__))

def plot_albedo(albedo_image, maxgray, subject, show=False):
    """Display and save albedo image.

    :param albedo_image (numpy.ndarray of floats): h x w image
    :param maxgray (int): generally 255 (max gray value)
    :param subject (str): one of 4 subjects of photos
    :return: None
    """

    # scale up and recast the type of albedo
    albedo_rescaled = (albedo_image * maxgray).astype(np.uint8)

    img = Image.fromarray(albedo_rescaled.astype(np.uint8), 'L')
    if show:
        img.show()

    fpath = os.path.join(curdir, IMG_DIR, subject,
                         subject + '_albedo.png')
    img.save(fpath, "PNG")

    # save also RGB copy for use in plot_surface():
    # matplotlib requires RGB encoding to wrap an image
    # over 3D surface
    rgbimg = Image.new("RGBA", img.size)
    rgbimg.paste(img)
    rgbimg.save(os.path.join(curdir, IMG_DIR, subject,
                         subject + '_albedoRGB.png'))

def plot_surface_normals(surface_normals, subject, show=False):
    """Estimated normals.
    """

    plt.clf()
    sz = surface_normals.shape
    # print (sz)
    f, axes = plt.subplots(1, 3)
    f.set_dpi(DPI)

    for i, ax in enumerate(axes):
        img = ax.imshow(surface_normals[i], interpolation='none', 
                        vmin=-1, vmax=1, cmap=plt.cm.jet, extent=[0,sz[2], 0, sz[1]])
        ax.axis('off')

    f.subplots_adjust(right=0.8)
    cbar_ax = f.add_axes([0.85, 0.34, 0.02, 0.31])
    f.colorbar(img, cax=cbar_ax)

    fpath = os.path.join(curdir, IMG_DIR, subject,
                         subject + '_normals.png')
    plt.savefig(fpath, dpi=DPI)
    if show:
        plt.show()

def plot_surface(height_map, maxgray, subject, show=False):
    """ plot 3d surface
    """

    plt.clf()
    img = read_png(os.path.join(curdir, IMG_DIR, subject,
                         subject + '_albedoRGB.png'))

    x, y = np.mgrid[0:img.shape[0], 0:img.shape[1]]
    ax = plt.gca(projection='3d')

    ax.plot_surface(x, y, height_map, rstride=1, cstride=1,\
                    facecolors=img, vmin=0, vmax=maxgray)
    orientations = [(190, -360), (-240, -320)]
    for i, (az, el) in enumerate(orientations):
        ax.view_init(azim=az, elev=el)
        fpath = os.path.join(curdir, IMG_DIR, subject,
                            subject + '_surface' + str(i) + '.png')
        plt.savefig(fpath, dpi=DPI)
        if show:
            plt.show()
