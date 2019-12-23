#!/bin/sh

classpath="out/production/cs264"
datadir="data/hw6/problem3"

for i in 1 2 3 4 5 6
do
    echo "\nCase $i:"
    printf "Executing \"java NetworkConnect < $datadir/input$i > $datadir/output$i\"...\n"
    java -classpath "$classpath" NetworkConnect < "$datadir/input$i" > "$datadir/output$i"
    diff -c "$datadir/answer$i" "$datadir/output$i"
done
