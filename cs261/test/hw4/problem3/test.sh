#!/bin/sh

classpath="out/production/cs264"
datadir="data/hw4/problem3"

for i in 1 2 3 4 5
do
    echo "\nCase $i:"
    printf "Executing \"java DoubleKnapsackSolution < $datadir/input$i > $datadir/output$i\"...\n"
    java -classpath "$classpath" DoubleKnapsackSolution < "$datadir/input$i" > "$datadir/output$i"
    diff -c "$datadir/answer$i" "$datadir/output$i"
done
