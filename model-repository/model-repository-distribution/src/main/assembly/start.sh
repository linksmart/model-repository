#!/bin/bash

CP=""

for jar in lib/*.jar; do
CP+=$jar:
done

java -client -cp $CP de.fraunhofer.e3.dmc.client.DMCApplication "$@";