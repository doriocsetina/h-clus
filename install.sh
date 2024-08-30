#!/bin/bash

javac -cp lib/* -d target/ $(find src -name "*.java")

jar cfe MainTest.jar MainTest -C target/ .

unzip -o -d target/ lib/mysql-connector-j-9.0.0.jar

jar cfe MultiServer.jar server.Multiserver -C target/ .

rm -rf target/