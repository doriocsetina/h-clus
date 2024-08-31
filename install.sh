#!/bin/bash

if systemctl is-active --quiet mysql
then
    echo "MySQL is running"

else 
    echo "MySQL is not running"
    echo "Starting MySQL... (you will be prompted your password to start the DB service)"

    sudo systemctl start mysql

fi

echo "Executing SQL script to poulate database..."
mysql -u MapUser -p$ < src/sql/SQLCreateTables.sql

echo "compiling source code..."
javac -cp lib/* -d target/ $(find src -name "*.java")

echo "creating MainTest.jar"
jar cfe MainTest.jar MainTest -C target/ .

echo "extracting dependencies from driver"
unzip -o -d target/ lib/mysql-connector-j-9.0.0.jar > /dev/null

echo "creating MultiServer.jar"
jar cfe MultiServer.jar server.MultiServer -C target/ .

echo "cleaning..."
rm -rf target/

