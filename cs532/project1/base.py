#!/usr/bin/env python3
# Library file containing methods to preprocess and standardize the training
# and validation data.
#
# Author: Alvin Lin (axl1439)

from constants import *

import itertools
import numpy as np

TRAINING_DATA_FILE = 'data_training'
VALIDATION_DATA_FILE = 'data_validation'

def preprocess_row(row):
    """
    Given a row in the data files, this method converts it into a Python list
    and converts enumerated constants to their integer ID assigned in
    constants.py.
    """
    integer_fields = [
        DURATION,
        SRC_BYTES,
        DST_BYTES,
        LAND,
        WRONG_FRAGMENT,
        URGENT,
        HOT,
        NUM_FAILED_LOGINS,
        LOGGED_IN,
        NUM_COMPROMISED,
        ROOT_SHELL,
        SU_ATTEMPTED,
        NUM_ROOT,
        NUM_FILE_CREATIONS,
        NUM_SHELLS,
        NUM_ACCESS_FILES,
        NUM_OUTBOUND_CMDS,
        IS_HOST_LOGIN,
        IS_GUEST_LOGIN,
        COUNT,
        SRV_COUNT,
        DST_HOST_COUNT,
        DST_HOST_SRV_COUNT
    ]
    float_scale_fields = [
        SERROR_RATE,
        SRV_SERROR_RATE,
        RERROR_RATE,
        SRV_RERROR_RATE,
        SAME_SRV_RATE,
        DIFF_SRV_RATE,
        SRV_DIFF_HOST_RATE,
        DST_HOST_SAME_SRV_RATE,
        DST_HOST_DIFF_SRV_RATE,
        DST_HOST_SAME_SRC_PORT_RATE,
        DST_HOST_SRV_DIFF_HOST_RATE,
        DST_HOST_SERROR_RATE,
        DST_HOST_SRV_SERROR_RATE,
        DST_HOST_RERROR_RATE,
        DST_HOST_SRV_RERROR_RATE
    ]
    row = row.strip().strip('.').split(',')
    for i, value in enumerate(row):
        if i in integer_fields:
            row[i] = int(value)
        elif i in float_scale_fields:
            row[i] = int(float(value) * 255)
        elif i == PROTOCOL_TYPE:
            row[i] = PROTOCOL_TYPES_MAP[value]
        elif i == SERVICE:
            row[i] = SERVICE_TYPES_MAP[value]
        elif i == FLAG:
            row[i] = FLAG_TYPES_MAP[value]
        elif i == ATTACK:
            row[i] = ATTACK_TYPES_MAP[value]
        else:
            raise ValueError('Unexpected row encountered!')
    return row

def get_human_readable_row(row):
    """
    Given a row as a numpy array converted by preprocessing, this method turns
    the enumerated constants back into strings for human readability.
    """
    if type(row) is np.ndarray:
        row = row.tolist()
    row[PROTOCOL_TYPE] = PROTOCOL_TYPES_RMAP[row[PROTOCOL_TYPE]]
    row[SERVICE] = SERVICE_TYPES_RMAP[row[SERVICE]]
    row[FLAG] = FLAG_TYPES_RMAP[row[FLAG]]
    row[ATTACK] = ATTACK_TYPES_RMAP[row[ATTACK]]
    return row

def get_training_data(slice=None):
    """
    Returns the training data as a numpy array. A optional slice can specified
    to only take that many records from the training data.
    """
    with open(TRAINING_DATA_FILE) as f:
        return np.array(
            [preprocess_row(row) for row in itertools.islice(f, slice)])

def get_validation_data(slice=None):
    """
    Returns the validation data as a numpy array. A optional slice can specified
    to only take that many records from the validation data.
    """
    with open(VALIDATION_DATA_FILE) as f:
        return np.array(
            [preprocess_row(row) for row in itertools.islice(f, slice)])
