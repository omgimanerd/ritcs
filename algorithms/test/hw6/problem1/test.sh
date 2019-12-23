#!/bin/sh

classpath="out/production/cs264"
datadir="data/hw6/problem1"

for i in 1 2 3 4 5 7
do
    echo "\nCase $i:"
    printf "Executing \"java NegativeCycle < $datadir/input$i > $datadir/output$i\"...\n"
    java -classpath "$classpath" NegativeCycle < "$datadir/input$i" > "$datadir/output$i"
    diff -c "$datadir/answer$i" "$datadir/output$i"
done
