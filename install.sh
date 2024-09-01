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
mysql -u MapUser -p < src/sql/SQLCreateTables.sql

echo "compiling source code..."
CLASSPATH=$(find lib -name "*.jar" | tr '\n' ':')

javac -cp "$CLASSPATH" -d target/ $(find src -name "*.java")

echo "creating MainTest.jar"
jar cfe MainTest.jar MainTest -C target/ .

# echo "extracting dependencies from driver"
# unzip -o -d target/ lib/* > /dev/null

echo "Extracting dependencies from driver"
for jar in lib/*.jar; do
    unzip -o -d target/ $jar > /dev/null
done


echo "creating MultiServer.jar"
jar cfe MultiServer.jar server.MultiServer -C target/ .

echo "cleaning..."
# rm -rf target/

