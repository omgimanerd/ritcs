"""
Homework 3 helper code

"""
import numpy as np
import cv2


def getpgmraw(filename):
  """Input: image file name (str). 
     Returns (pic) a numpy.ndarray. 

  """
  pic = cv2.imread(filename, -1)
  return pic


if __name__ == "__main__":
  """Testing..."""

  import os
  from PIL import Image

  dir_path = os.path.dirname(os.path.realpath(__file__))
  IMG_DIR = 'croppedyale'
  SUBJECT_NAME = 'yaleB01'
  IMG_FILE = 'yaleB01_P00A-005E-10.pgm'
  full_path = os.path.join(dir_path, IMG_DIR, SUBJECT_NAME, IMG_FILE)

  pic = getpgmraw(full_path)
  img = Image.fromarray(pic)
  img.show()
