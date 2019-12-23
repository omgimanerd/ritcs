#!/bin/bash

classpath="out/production/cs264/"
datadir="data/hw3/problem2"

rm -if time.txt
echo "+===========+" >> time.txt
echo "+ RECURSIVE +" >> time.txt
echo "+===========+" >> time.txt

for i in {10..39}
do
    currentinfile=$(printf "$datadir/input%04d" "$i")
    # Get the size of the matrix stored in this input file
    lines=$(wc -w "$currentinfile" | cut -f 1 -d " ")
    q=$((lines - 2))

    # Get the times for a serial run on this input file
    printf "$q\t" >> time.txt
    { time java -classpath "$classpath" LongestIncreasingSubseqRecursive < "$currentinfile" > /dev/null; } 2>&1 | grep "real" | cut -d "m" -f 2 | cut -d "s" -f 1 >> time.txt
    echo "Input $i completed"
done

echo "+===========+" >> time.txt
echo "+  DYNAMIC  +" >> time.txt
echo "+===========+" >> time.txt

for i in {10..39}
do
    currentinfile=$(printf "$datadir/input%04d" "$i")
    # Get the size of the matrix stored in this input file
    lines=$(wc -w "$currentinfile" | cut -f 1 -d " ")
    q=$((lines - 2))

    # Get the times for a serial run on this input file
    printf "$q\t" >> time.txt
    { time java -classpath "$classpath" LongestIncreasingSubseqDP < "$currentinfile" > /dev/null; } 2>&1 | grep "real" | cut -d "m" -f 2 | cut -d "s" -f 1 >> time.txt
    echo "Input $i completed"
done
