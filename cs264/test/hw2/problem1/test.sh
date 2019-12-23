#!/bin/sh

classpath="out/production/cs264/"
datadir="data/hw2/problem1"

for i in 1 2 3
do
    echo "\nCase $i:"
    printf "Executing \"java Reservoir < $datadir/input$i > $datadir/output$i\"...\n"
    java -classpath "$classpath" Reservoir < "$datadir/input$i" > "$datadir/output$i"
    diff -c "$datadir/answer$i" "$datadir/output$i"
done
