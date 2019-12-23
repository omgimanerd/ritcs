#!/bin/sh

classpath="out/production/cs264/:src/problem3/:lib/"
datadir="data/hw1/problem3"

for i in 1 2 3 4 5 6 7
do
    echo "\nCase $i:"
    printf "Executing \"java DiffT < $datadir/input$i > $datadir/output$i\"...\n"
    java -classpath "$classpath" DiffT < "$datadir/input$i" > "$datadir/output$i"
    diff -c "$datadir/answer$i" "$datadir/output$i"
done
