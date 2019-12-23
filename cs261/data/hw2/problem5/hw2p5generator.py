import sys
import random

if len(sys.argv) < 3:
    print("hw2p5generator.py: must specify n and output filename")

n = int(sys.argv[1])
with open(sys.argv[2], "w") as f:
    f.write(str(n) + '\n')
    upperBound = (n*n) - 1
    for i in range(0, n):
        f.write(str(random.randrange(0, upperBound)) + " ")
    f.write('\n')
