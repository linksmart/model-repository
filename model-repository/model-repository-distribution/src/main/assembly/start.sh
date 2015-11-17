#!/bin/bash

CP=""

for jar in lib/*.jar; do
CP+=$jar:
done

java -client -cp $CP eu.linksmart.services.mr.MRApplication "$@";