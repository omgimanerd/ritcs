"""
Homework 3 helper code

"""
import numpy as np

def sph2cart(azimuth,elevation,r):
    c_ele = np.cos(elevation)
    s_ele = np.sin(elevation)
    c_azi = np.cos(azimuth)
    s_azi = np.sin(azimuth)
    
    x = r * c_ele * c_azi
    y = r * c_ele * s_azi
    z = r * s_ele
    
    return y, z, x
