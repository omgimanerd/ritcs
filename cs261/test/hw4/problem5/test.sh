#!/bin/sh

classpath="out/production/cs264"
datadir="data/hw4/problem5"

for i in 1 2 3
do
    echo "\nCase $i:"
    printf "Executing \"java Triangulate < $datadir/input$i > $datadir/output$i\"...\n"
    java -classpath "$classpath" Triangulate < "$datadir/input$i" > "$datadir/output$i"
    diff -c "$datadir/answer$i" "$datadir/output$i"
done
