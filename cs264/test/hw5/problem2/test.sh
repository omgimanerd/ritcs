#!/bin/sh

classpath="out/production/cs264"
datadir="data/hw5/problem2"

for i in 1 2 3 4 5
do
    echo "\nCase $i:"
    printf "Executing \"java Prerequisites < $datadir/input$i > $datadir/output$i\"...\n"
    java -classpath "$classpath" Prerequisites < "$datadir/input$i" > "$datadir/output$i"
    diff -c "$datadir/answer$i" "$datadir/output$i"
done
