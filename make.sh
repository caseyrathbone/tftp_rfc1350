#!/bin/bash

mkdir -p ./bin
javac -sourcepath ./src/tftp -d ./bin ./src/tftp/*.java
