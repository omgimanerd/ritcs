#!/usr/bin/env python3
# Misuse IDPS based on a Neural Network
# Author: Alvin Lin (axl1439)

from keras.models import Sequential, model_from_json
from keras.layers import Dense

import math
import numpy as np
import os
import sys

from constants import *
from base import *

os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'

FEATURES = 41

def diagnostics():
    """
    Returns some diagnostics about the distributions of attack types in the
    training data set.
    """
    data = get_validation_data()
    for i in range(38):
        print(ATTACK_TYPES_RMAP[i], np.sum(data[:,-1] == i))

def train(attack_type, epochs):
    """
    Trains a neural net using the training data to detect a given attack
    type for a given number of epochs
    """
    # Preprocess the training data and separate it into the feature vectors
    # and desired output.
    data = get_training_data()
    input_data = data[:,:-1]
    output_data = data[:,-1]
    attack = output_data == ATTACK_TYPES_MAP[attack_type]
    output_data[attack] = 1
    output_data[~attack] = 0

    # Train the model and write it to disk.
    model = Sequential()
    model.add(Dense(32, input_dim=FEATURES, activation='relu'))
    model.add(Dense(32, activation='relu'))
    model.add(Dense(1, activation='sigmoid'))
    model.compile(loss='mean_squared_error', optimizer='adam',
                  metrics=['accuracy'])
    model.fit(input_data, output_data, epochs=epochs, batch_size=100)
    with open('models/model_{}.json'.format(attack_type), 'w') as f:
        f.write(model.to_json())
    model.save_weights('models/model_{}.h5'.format(attack_type))

def validate(attack_type):
    """
    Loads a neural net model from disk and validates it against a given attack
    type.
    """
    data = get_validation_data()
    input_data = data[:,:-1]
    output_data = data[:,-1]
    attack = output_data == ATTACK_TYPES_MAP[attack_type]
    output_data[attack] = 1
    output_data[~attack] = 0

    with open('models/model_{}.json'.format(attack_type)) as f:
        model = model_from_json(f.read())
        model.load_weights('models/model_{}.h5'.format(attack_type))
        model.compile(loss='mean_squared_error', optimizer='adam',
                     metrics=['accuracy'])
        prediction = model.predict(input_data).flatten().round()
        matches = prediction[prediction == output_data]
        mismatches = prediction[prediction != output_data]
        true_positives = np.sum(matches == 1)
        true_negatives = np.sum(matches == 0)
        false_positives = np.sum(mismatches == 1)
        false_negatives = np.sum(mismatches == 0)
        print('True Positives:\t{}'.format(true_positives))
        print('True Negatives:\t{}'.format(true_negatives))
        print('False Positives:\t{}'.format(false_positives))
        print('False Negatives:\t{}'.format(false_negatives))
        print('Accuracy:\t{}'.format(len(matches) / len(data)))

def main():
    import argparse
    p = argparse.ArgumentParser()
    p.add_argument('action', choices=['train', 'validate'])
    p.add_argument('attack_type')
    p.add_argument('--epochs', type=int, default=10)
    args = p.parse_args()
    if args.action == 'train':
        train(args.attack_type, args.epochs)
    elif args.action == 'validate':
        validate(args.attack_type)

if __name__ == '__main__':
    main()
