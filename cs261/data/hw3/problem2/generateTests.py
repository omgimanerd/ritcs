#!/usr/bin/env python3

namecount = 10
i = 5
while i < 35:
    print("Generating data for n=%d..." % i)
    dataset = list(range(0, i+1))
    with open("input{:04d}".format(namecount), "w") as f:
        f.write(str(i))
        f.write("\n")
        for j in range(0, len(dataset)):
            f.write(str(dataset[j]))
            if j != len(dataset) - 1:
                f.write(" ")
        namecount += 1
    i += 1
