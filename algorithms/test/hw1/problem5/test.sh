#!/bin/sh

classpath="out/production/cs264:src/problem5/:lib/"
datadir="data/hw1/problem5"
echo "Testing StringPuzzle.java"

for i in 1 2 3 4 5 6
do
    echo "\nCase $i:"
    printf "Executing \"java StringPuzzle < $datadir/input$i > $datadir/output$i\"...\n"
    java -classpath "$classpath" StringPuzzle < "$datadir/input$i" > "$datadir/output$i"
    diff -c "$datadir/answer$i" "$datadir/output$i"
done
