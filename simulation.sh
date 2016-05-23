#!/bin/bash

echo "This is a simple testfile" >> myfile.test
./run.sh myfile.test
echo "File diff"
diff myfile.test myfile.test-client > test_result
