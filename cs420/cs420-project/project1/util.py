#!/usr/bin/env python3
# Utility module
# Author: Alvin Lin (axl1439)
# Author: Stefan Aleksic

from math import sin, cos, degrees, radians, sqrt, atan2
import re

EPSILON = 2e-5

def parse_latitude(direction, value):
    """
    Given a direction as N or S and the string value of a latitude
    in RMC or GGA format, this function parses and returns the latitude
    as a floating point number with the correct sign.
    """
    matches = re.search('([0-9]{2})([0-9]{2}.[0-9]+)', value)
    degrees = float(matches.group(1))
    minutes = float(matches.group(2)) / 60
    return (-1 if direction == 'S' else 1) * (degrees + minutes)

def parse_longitude(direction, value):
    """
    Given a direction as W or E and the string value of a longitude
    in RMC or GGA format, this function parses and returns the longitude
    as a floating point number with the correct sign.
    """
    matches = re.search('([0-9]{3})([0-9]{2}.[0-9]+)', value)
    degrees = float(matches.group(1))
    minutes = float(matches.group(2)) / 60
    return (-1 if direction == 'W' else 1) * (degrees + minutes)

def get_distance(lat1, lng1, lat2, lng2):
    """
    Calculates the great circle distance between two latitude longitude
    coordinates in kilometers.
    https://en.wikipedia.org/wiki/Great-circle_distance
    https://www.movable-type.co.uk/scripts/latlong.html
    """
    if lat1 == lat2 and lng1 == lng2:
        return 0
    lat1, lng1 = radians(lat1), radians(lng1)
    lat2, lng2 = radians(lat2), radians(lng2)
    abslat = radians(abs(lat2 - lat1))
    abslng = radians(abs(lng2 - lng1))
    a = (sin(abslat / 2) ** 2) + (cos(lat1) * cos(lat2) * (sin(abslng) ** 2))
    c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return c * 6371

def get_bearing(lat1, lng1, lat2, lng2):
    """
    Calculates the bearing between two latitude longitude coordinates in
    degrees.
    https://www.movable-type.co.uk/scripts/latlong.html
    """
    lat1, lng1 = radians(lat1), radians(lng1)
    lat2, lng2 = radians(lat2), radians(lng2)
    y = sin(lng2 - lng1) * cos(lat2)
    x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(lng2 - lng1)
    return degrees(atan2(y, x))

def get_angle_distance(theta1, theta2):
    """
    Returns the signed angle difference between two angles bounded between
    0 and 360 degrees.
    https://gamedev.stackexchange.com/questions/4467
    """
    return (theta2 - theta1 + 540) % 360 - 180
