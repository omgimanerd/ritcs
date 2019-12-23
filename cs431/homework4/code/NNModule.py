import numpy as np
import matplotlib.pyplot as plt
from sklearn.utils import shuffle
from helper import y2indicator, getBinaryfer13Data, sigmoid, sigmoid_cost, error_rate, softmax


class NNModule(object):
    def __init__(self):
        pass
    
    def train(self, X, Y, step_size=10e-7, epochs=10000):
        # Validation data set extracted from the training data
        X, Y = shuffle(X, Y)
        Xvalid, Yvalid = X[-1000:], Y[-1000:]
        X, Y = X[:-1000], Y[:-1000]   
        K = len(set(Y))
        
        # Convert outputs of the NN to an indicator matrix
        Ytrain_ind = y2indicator(Y, K)
        Yvalid_ind = y2indicator(Yvalid, K)
        M, D = X.shape
       

        #HW3_2. Randomly initialize all the hidden weights W's and biases b's 
        #Add code here....
        
        #HW3_3. For the given number of epochs set, implement backpropagation to learn the
        #       weights and append computed costs in the 2 cost arrays
        train_costs = []
        valid_costs = []
        best_validation_error = 1
        for i in xrange(epochs):
            #HW3_4. Run forward propagation twice; once to calculate P(Ytrain|X) 
            #       and Ztrain (activations at hidden layer); second to calculate
            #       P(Yvalid|Xvalid) and Zvalid
            #Add code here....

            #HW3_5. Now we do back propagation by first performing 
            #       gradient descent using equations (3) and (4) from the HW text
            #Add code here....
            
            #HW3_5b. Now we do back propagation
            #Add code here....
  
            #HW3_6. Compute the training and validation errors using cross_entropy cost
            #       function; once on the training predictions and once on validation predictions
            #       append errors to appropriate error array 
            #Add code here....
            
        #HW3_7. Include the best validation error and training and validation classification 
        #       rates in your final report
        #Add code here....
        
        #HW3_8. Display the training and validation cost graphs in your final report
        #Add code here....


    # Implement the forward algorithm
    def forward(self, X):
        #Add code here....

    # Implement the prediction algorithm
    def predict(self, P_Y_given_X):
        #Add code here....

    # Implement a method to compute accuracy or classification rate (DONE!)
    def classification_rate(self, Y, P):
        return np.mean(Y == P)

    def cross_entropy(self, T, pY):
        #Add code here....


def main():
    #HW3_1. Train a NN classifier on the fer13 training dataset 
    #Add code here....
    
    #HW3_9. After your training errors are sufficiently low, 
    #       apply the classifier on the test set, 
    #       show the classification accuracy in your final report
    #Add code here....
        
    
if __name__ == '__main__':
    main()
   