#!/usr/bin/env python3
# Main executable project file.
# Author: Alvin Lin (axl1439)
# Author: Stefan Aleksic

import fastkml.kml as kml
import numpy as np

from shapely.geometry import LineString, Point

import math

from util import *

FILENAME = 'ZI8D_JUM_2018_08_13_1930_Circumnavigate_RIT_with_Squiggles.txt'
KML_NAMESPACE = '{http://www.opengis.net/kml/2.2}'

TIME = 0
LATITUDE = 1
LONGITUDE = 2
SPEED = 3
TRACKING_ANGLE = 4
FIX = 5
SATELLITES = 6
DILUTION_OF_PRECISION = 7
ALTITUDE = 8

LEFT_TURN_WINDOW_SIZE = 15

def preprocess_data_row(row):
    """
    Given a row in the input data file, this function processes the row
    and standardizes it for processing.
    """
    row = row.strip().split(',')
    if row[0] == '$GPRMC':
        return [
            float(row[1]),                   # TIME
            parse_latitude(row[4], row[3]),  # LATITUDE
            parse_longitude(row[6], row[5]), # LONGITUDE
            float(row[7]),                   # SPEED
            float(row[8]),                   # TRACKING_ANGLE
            -1,                              # FIX
            -1,                              # SATELLITES
            -1,                              # DILUTION_OF_PRECISION
            -1                               # ALTITUDE
        ]
    elif row[0] == '$GPGGA':
        return [
            float(row[1]),                   # TIME
            parse_latitude(row[3], row[2]),  # LATITUDE
            parse_longitude(row[5], row[4]), # LONGITUDE
            -1,                              # SPEED
            -1,                              # TRACKING_ANGLE
            float(row[6]),                   # FIX
            float(row[7]),                   # SATELLITES
            float(row[8]),                   # DILUTION_OF_PRECISION
            float(row[9])                    # ALTITUDE
        ]
    else:
        raise TypeError('Unknown data row encountered')

def is_valid_row(row):
    """
    Given a row in the input data file, this function validates the row
    and returns True if it is a valid entry.
    """
    return row.startswith('$GP')

def get_data():
    """
    Fetches the data from the input file and preprocesses it, returning
    it as a standardized numpy array.
    """
    with open(FILENAME) as f:
        return np.array(
            [preprocess_data_row(row) for row in f if is_valid_row(row)])

def prune_redundant_points(data):
    """
    Prunes away points that are too close to neighboring points to remove
    redundancy.
    """
    distances = [math.inf]
    for i in range(1, len(data)):
        lat1, lng1 = data[i, LATITUDE], data[i, LONGITUDE]
        lat2, lng2 = data[i - 1, LATITUDE], data[i - 1, LONGITUDE]
        distances.append(get_distance(lat1, lng1, lat2, lng2))
    lt_threshold = np.array(distances) > EPSILON
    return data[lt_threshold]

def get_left_turns(data):
    """
    Gets the location for each left turn given the array of coordinates.
    The location of each left turn is given as (longitude, latitude)
    for display as a KML formatted coordinate.
    """
    bearings = [0]
    for i in range(1, len(data)):
        lat1, lng1 = data[i, LATITUDE], data[i, LONGITUDE]
        lat2, lng2 = data[i - 1, LATITUDE], data[i - 1, LONGITUDE]
        bearings.append(get_bearing(lat1, lng1, lat2, lng2))
    left_turns = []
    i = 0
    while i < len(data) - LEFT_TURN_WINDOW_SIZE:
        bearing_window = bearings[i:i + LEFT_TURN_WINDOW_SIZE]
        delta = get_angle_distance(bearing_window[-1], bearing_window[0])
        if delta > 60:
            location = data[i + LEFT_TURN_WINDOW_SIZE // 2]
            left_turns.append((location[LONGITUDE], location[LATITUDE]))
            i += LEFT_TURN_WINDOW_SIZE
        else:
            i += 1
    return left_turns

def generate_kml(data, left_turns=None):
    """
    Generates KML from the standardized list of data points.
    """
    pairs = [(row[LONGITUDE], row[LATITUDE]) for row in data]
    root = kml.KML()

    document = kml.Document(KML_NAMESPACE, name='CS420')
    root.append(document)

    path_folder = kml.Folder(KML_NAMESPACE, name='Path')
    document.append(path_folder)

    # Code to mark each point with a pin (debugging only)
    # for i, point in enumerate(pairs):
    #     p = kml.Placemark(KML_NAMESPACE, name=str(i))
    #     p.geometry = Point(point)
    #     path_folder.append(p)

    path = kml.Placemark(KML_NAMESPACE, name='Path')
    path.geometry = LineString(pairs)
    path_folder.append(path)
    start = kml.Placemark(KML_NAMESPACE, name='Start')
    start.geometry = Point(pairs[0])
    path_folder.append(start)
    end = kml.Placemark(KML_NAMESPACE, name='End')
    end.geometry = Point(pairs[-1])
    path_folder.append(end)

    if left_turns is not None:
        left_turns_folder = kml.Folder(KML_NAMESPACE, name='Left Turns')
        document.append(left_turns_folder)
        for location in left_turns:
            placemark = kml.Placemark(KML_NAMESPACE, name='Left Turn')
            placemark.geometry = Point(location)
            left_turns_folder.append(placemark)

    return root.to_string(prettyprint=True)

def main():
    data = prune_redundant_points(get_data())
    left_turns = get_left_turns(data)
    print(generate_kml(data, left_turns=left_turns))
    # for i in range(1, len(data)):
    #     lat1, lng1 = data[i, LATITUDE], data[i, LONGITUDE]
    #     lat2, lng2 = data[i - 1, LATITUDE], data[i - 1, LONGITUDE]
    #     print(get_distance(lat1, lng1, lat2, lng2) * 6000)
    # print(len(data))

if __name__ == '__main__':
    main()
