#!/bin/sh

classpath="out/production/cs264"
datadir="data/hw4/problem1"

for i in 1 2 3 4 5
do
    echo "\nCase $i:"
    printf "Executing \"java LongestConvexSubseq < $datadir/input$i > $datadir/output$i\"...\n"
    java -classpath "$classpath" LongestConvexSubseq < "$datadir/input$i" > "$datadir/output$i"
    diff -c "$datadir/answer$i" "$datadir/output$i"
done
