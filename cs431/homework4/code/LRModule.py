import numpy as np
import matplotlib.pyplot as plt

from sklearn.utils import shuffle
from helper import *

class LRModule(object):
    def __init__(self):
        self.weights = None
        self.biases = None

    def train(self, X, Y, step_size=10e-7, epochs=10000):
        # Validation data set extracted from the training data
        X, Y = shuffle(X, Y)
        Xvalid, Yvalid = X[-1000:], Y[-1000:]
        X, Y = X[:-1000], Y[:-1000]
        N, D = X.shape

        self.W, self.B = init_weight_and_bias(D, 1)
        self.W = self.W.flatten()
        print('X shape', X.shape)
        print('Y shape', Y.shape)
        print('W shape', self.W.shape)
        print('B shape', self.B.shape)

        #HW3_3. For the given number of epochs set, learn the weights
        costs = []
        best_validation_error = 1
        for i in range(epochs):
            # Call the forward function to get the classifier output
            pY = self.forward(X)
            # Calculate the error and perform gradient descent
            self.W -= step_size * np.dot(X.T, pY - Y)
            self.B -= step_size * np.sum(pY - Y)
            # Compute P(Y|X_valid) and append the sigmoid costs
            pYvalid = self.predict(Xvalid)
            costs.append(sigmoid_cost(pYvalid, Yvalid))
            # Track best validation
            error = error_rate(Yvalid, pYvalid)
            print(error)
            best_validation_error = min(error, best_validation_error)
        plt.figure(1)
        plt.plot(np.arange(epochs), costs)

    def forward(self, X):
        return sigmoid(X.dot(self.W) + self.B)

    def predict(self, X):
        return np.round(self.forward(X))

    def score(self, X, Y):
        prediction = self.predict(X)
        return 1 - error_rate(Y, prediction)

def main():
    #HW3_1. Train a LR classifier on the fer13 training dataset
    test_data, test_labels = getBinaryfer13Data('fer3and4test.csv')
    train_data, train_labels = getBinaryfer13Data('fer3and4train.csv')

    classifier = LRModule()
    classifier.train(train_data, train_labels, epochs=100)
    print(classifier.score(test_data, test_labels))

     #HW3_9. After your training errors are sufficiently low,
     #       apply the classifier on the test set,
     #       show the classification accuracy in your final report
    #Add code here....


if __name__ == '__main__':
  main()
