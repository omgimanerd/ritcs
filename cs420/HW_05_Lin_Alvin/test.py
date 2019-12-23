#!/usr/bin/env python3

import numpy as np

from HW_04_Lin_Alvin_Trainer import get_training_data

def main():
  labels, training_data = get_training_data()
  training_data = ['Muffin' if row[0] == 0 else 'Cupcake' for row in training_data]
  with open('HW_04_Lin_Alvin_MyClassifications.csv') as f:
    output = f.read().strip().split('\n')
  assert len(training_data) == len(output)
  tp_tn = 0
  fp_fn = 0
  for i in range(len(training_data)):
    if training_data[i] == output[i]:
      tp_tn += 1
    else:
      fp_fn += 1
    print(training_data[i], output[i])
  print(tp_tn, fp_fn)
  print('Accuracy: {}', tp_tn / len(output))
  

if __name__ == '__main__':
  main()
