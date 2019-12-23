#!/bin/sh

if [ -z "$1" ]; then
    echo "runtest.sh: must specify which homework to run tests for"
    exit 1
fi

if [ -z "$2" ]; then
    echo "runtest.sh: must specify which problem to run tests for"
    exit 1
fi

exec "./test/hw$1/problem$2/test.sh"
