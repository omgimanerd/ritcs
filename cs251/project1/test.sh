#!/bin/bash

rm -if time.txt
for i in {1..6}
do
    # Get the size of the matrix stored in this input file
    lines=$(wc -l data/input$i | cut -f 1 -d " ")

    # Get the times for a serial run on this input file
    echo "N = $lines; P = 1"
    echo "N = $lines; P = 1" >> time.txt
    for j in {1..10}
    do
        { time ./solver $lines 1 < data/input$i > /dev/null; } 2>&1 | grep "real" | cut -d "m" -f 2 | cut -d "s" -f 1 >> time.txt
        echo "Trial $j completed"
    done

    # Get the times for a threaded run on this input file with 8 threads
    echo "N = $lines; P = 8"
    echo "N = $lines; P = 8" >> time.txt
    for j in {1..20}
    do
        { time ./solver $lines 8 < data/input$i > /dev/null; } 2>&1 | grep "real" | cut -d "m" -f 2 | cut -d "s" -f 1 >> time.txt
        echo "Trial $j completed"
    done

    # Get the times for a threaded run on this input file with 8 threads
    echo "N = $lines; P = 16"
    echo "N = $lines; P = 16" >> time.txt
    for j in {1..20}
    do
        { time ./solver $lines 16 < data/input$i > /dev/null; } 2>&1 | grep "real" | cut -d "m" -f 2 | cut -d "s" -f 1 >> time.txt
        echo "Trial $j completed"
    done

done
