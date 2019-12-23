#!/bin/sh

classpath="out/production/cs264"
datadir="data/hw3/problem2"

for i in 1 2 3 4 5
do
    echo "\nCase $i:"
    printf "Executing \"java LongestIncreasingSubseqRecursive < $datadir/input$i > $datadir/output$i\"...\n"
    java -classpath "$classpath" LongestIncreasingSubseqRecursive < "$datadir/input$i" > "$datadir/output$i"
    diff -c "$datadir/answer$i" "$datadir/output$i"
done

for i in 1 2 3 4 5
do
    echo "\nCase $i:"
    printf "Executing \"java LongestIncreasingSubseqDP < $datadir/input$i > $datadir/output$i\"...\n"
    java -classpath "$classpath" LongestIncreasingSubseqDP < "$datadir/input$i" > "$datadir/output$i"
    diff -c "$datadir/answer$i" "$datadir/output$i"
done
