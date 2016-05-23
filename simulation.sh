#!/bin/bash

echo "This is a simple testfile" >> myfile.test
./run.sh myfile.test
echo ""
echo "---- File diff ----"
diff myfile.test myfile.test-clientfile > simulation_diff
