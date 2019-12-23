#!/usr/bin/env python3
# Principles of Data Mining with Dr. Kinsman: HW 04
# Author: Alvin Lin (axl1439)

import math
import matplotlib.pyplot as plt
import numpy as np


TRAINING_FILE = 'Recipes_For_Release_2181_v202.csv'
INDENT = '    '

BASE_CODE = """#!/usr/bin/env python3
import numpy as np

VALIDATION_FILE = 'Recipes_For_VALIDATION_2181_RELEASED_v202.csv'

def preprocess_training_data_row(row):
    row = row.split(',')
    row[0] = 0 if row[0] == 'Muffin' else 1
    return list(map(float, row))

def main():
    with open(VALIDATION_FILE) as f:
        header = f.readline()
        for row in f:
            data = preprocess_training_data_row(row)
{}
            print(category)

if __name__ == '__main__':
    main()
"""

class DecisionTreeNode():
    def __init__(self):
        self.data = None
        self.left = None
        self.right = None

def preprocess_training_data_row(row):
    """
    Preprocessing for a training data row. Zaps Muffin to 0 and Cupcake to 1
    for numpy processing.
    """
    row = row.split(',')
    row[0] = 0 if row[0] == 'Muffin' else 1
    return list(map(float, row))

def get_training_data():
    """
    Returns the training data in the csv file.
    """
    with open(TRAINING_FILE) as f:
        header = f.readline().strip().split(',')
        types = {}
        for i, label in enumerate(header):
            types[label] = i
        return types, np.array([preprocess_training_data_row(row) for row in f])

def get_mixed_gini_index(lt, gte):
    """
    Calculates the mixed Gini index based on the cupcake/muffin similarity
    given the two splits.
    """
    p_cupcake_lt = np.sum(lt[:,0]) / len(lt)
    p_cupcake_gte = np.sum(gte[:,0]) / len(gte)
    gini_index_lt = 1 - (p_cupcake_lt ** 2) - ((1 - p_cupcake_lt) ** 2)
    gini_index_gte = 1 - (p_cupcake_gte ** 2) - ((1 - p_cupcake_gte) ** 2)
    return (gini_index_lt + gini_index_gte) / 2

def get_majority_class(data):
    """
    Returns the majority class of a given chunk of data.
    """
    muffin = np.sum(data[:,0] == 0)
    if muffin > len(data) - muffin:
        return 'Muffin'
    return 'Cupcake'

def train(types, data, root, depth, max_depth=2):
    """
    Generate a tree of DecisionTreeNode objects on the given data.
    """
    best_gini_index = math.inf
    best_split_type = None
    best_split_value = None
    for t in range(1, 26):
        type_values = data[:,t]
        low = math.floor(min(type_values))
        high = math.ceil(max(type_values) + 1)
        for value in range(low, high):
            lt = data[type_values < value]
            gte = data[type_values >= value]
            if len(lt) == 0 or len(gte) == 0:
                continue
            gini_index = get_mixed_gini_index(lt, gte)
            if gini_index < best_gini_index:
                best_split_type = t
                best_split_value = value
                best_gini_index = gini_index
    split_type_column = data[:,best_split_type]
    lt = data[split_type_column < best_split_value]
    gte = data[split_type_column >= best_split_value]
    root.left = DecisionTreeNode()
    root.right = DecisionTreeNode()
    root.data = 'if data[{}] >= {}:\n'.format(
        best_split_type, best_split_value)
    if depth == max_depth:
        left_majority_class = get_majority_class(lt)
        right_majority_class = 'Cupcake' if (
            left_majority_class == 'Muffin') else 'Muffin'
        root.left.data = 'category = \'{}\'\n'.format(left_majority_class)
        root.right.data = 'category = \'{}\'\n'.format(right_majority_class)
    else:
        train(types, lt, root.left, depth + 1, max_depth)
        train(types, gte, root.right, depth + 1, max_depth)

def tree_to_code(root_node, base_indent=''):
    """
    Generate Python code from a tree of DecisionTreeNode objects.
    """
    def recursive_helper(root, accum, indent=base_indent):
        accum += indent + root.data
        if root.left is not None and root.right is not None:
            accum += recursive_helper(root.right, '', indent + INDENT)
            accum += indent + 'else:\n'
            accum += recursive_helper(root.left, '', indent + INDENT)
        return accum
    return recursive_helper(root_node, '', base_indent)

def main():
    types, data = get_training_data()
    root = DecisionTreeNode()
    train(types, data, root, 0, max_depth=1)
    classifier_code = BASE_CODE.format(tree_to_code(root, INDENT * 3))
    print(classifier_code)
    with open('HW_05_Lin_Alvin_Classifier.py', 'w') as f:
        f.write(classifier_code)

if __name__ == '__main__':
    main()
