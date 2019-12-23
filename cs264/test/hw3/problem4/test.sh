#!/bin/sh

classpath="out/production/cs264"
datadir="data/hw3/problem4"

for i in 1 2 8 9 10 11 12 13
do
    echo "\nCase $i:"
    printf "Executing \"java CountIntervals < $datadir/input$i > $datadir/output$i\"...\n"
    java -classpath "$classpath" CountIntervals < "$datadir/input$i" > "$datadir/output$i"
    diff -c "$datadir/answer$i" "$datadir/output$i"
done
