#!/bin/sh

classpath="out/production/cs264"
datadir="data/hw3/problem1"

for i in 1 2 3 4 5
do
    echo "\nCase $i:"
    printf "Executing \"java IntervalsBreaks < $datadir/input$i > $datadir/output$i\"...\n"
    java -classpath "$classpath" IntervalsBreaks < "$datadir/input$i" > "$datadir/output$i"
    diff -c "$datadir/answer$i" "$datadir/output$i"
done
