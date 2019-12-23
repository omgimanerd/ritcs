#!/bin/sh

for file in $(ls ./inputs/*.kld)
do
    EXPECTED=$(cat ./expect/$(basename $file).txt)
    ACTUAL=$(python ../parser.py $file)
    if test ! "$ACTUAL" = "$EXPECTED"
    then
        echo Test $file failed:
        echo "    " Expected: $EXPECTED
        echo "    " Actual: $ACTUAL
    fi
done
