#!/bin/sh

classpath="out/production/cs264"
datadir="data/hw5/problem1"

for i in 1 2 3 4 5 6 7
do
    echo "\nCase $i:"
    printf "Executing \"java NumPaths < $datadir/input$i > $datadir/output$i\"...\n"
    java -classpath "$classpath" NumPaths < "$datadir/input$i" > "$datadir/output$i"
    diff -c "$datadir/answer$i" "$datadir/output$i"
done
