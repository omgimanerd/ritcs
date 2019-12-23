import sys

infile = open(sys.argv[1], "r")
outfile = open(sys.argv[2], "w")

outfile.write("digraph G {\n")

n = int(infile.readline())

for i in range(1, n+1):
    line = infile.readline()
    linestrip = line[:-3]
    if linestrip != "":
        outfile.write("\t" + str(i) + " -> {")
        outfile.write(linestrip + "}\n")

outfile.write("}\n")

outfile.close()
infile.close()
