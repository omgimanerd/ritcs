#!/bin/sh

classpath="out/production/cs264/:src/problem4/:lib/"
datadir="data/hw1/problem4"

for i in 1 2 3 4 5 6
do
    echo "\nCase $i:"
    printf "Executing \"java FindMaxDiffPairs < $datadir/input$i > $datadir/output$i\"...\n"
    java -classpath "$classpath" FindMaxDiffPairs < "$datadir/input$i" > "$datadir/output$i"
    diff -c "$datadir/answer$i" "$datadir/output$i"
done
