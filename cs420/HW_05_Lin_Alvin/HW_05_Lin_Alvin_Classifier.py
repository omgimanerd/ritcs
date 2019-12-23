#!/usr/bin/env python3
import numpy as np

TRAINING_FILE = 'Recipes_For_Release_2181_v202.csv'
VALIDATION_FILE = 'Recipes_For_VALIDATION_2181_RELEASED_v202.csv'

def preprocess_training_data_row(row):
    row = row.split(',')
    row[0] = 0 if row[0] == 'Muffin' else 1
    return list(map(float, row))

def main():
    with open(TRAINING_FILE) as f:
        header = f.readline()
        for row in f:
            data = preprocess_training_data_row(row)
            if data[3] >= 25:
                if data[1] >= 30:
                    category = 'Muffin'
                else:
                    category = 'Cupcake'
            else:
                if data[4] >= 18:
                    category = 'Cupcake'
                else:
                    category = 'Muffin'

            print(category)

if __name__ == '__main__':
    main()
