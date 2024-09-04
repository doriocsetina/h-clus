#!/bin/bash

# Function to check if MySQL is running and start it if not
check_mysql() {
    if systemctl is-active --quiet mysql; then
        echo "MySQL is running"
    else 
        echo "MySQL is not running"
        echo "Starting MySQL... (you will be prompted for your password to start the DB service)"
        sudo systemctl start mysql
    fi
}

# Function to execute SQL script to populate the database
execute_sql_script() {
    echo "Executing SQL script to populate database..."
    echo "You will be prompted your mySQL user password to run the script: "
    mysql -u MapUser -p < src/sql/SQLCreateTables.sql
}

# Function to compile the source code
compile_source_code() {
    echo "Compiling source code..."
    # Include all JAR files in the lib directory in the classpath
    CLASSPATH=$(find lib -name "*.jar" | tr '\n' ':')
    echo "Classpath: $CLASSPATH"

    # Compile the source code, specifying the classpath
    javac -cp "$CLASSPATH" -d target/ $(find src -name "*.java")
}

extract_dependencies() {
    echo "extracting dependencies..."
    for jar in lib/*.jar; do
        unzip -o -d target/ "$jar" > /dev/null
    done
}
 
# Function to create fat JAR files
create_fat_jars() {
    echo "Creating MainTest.jar"
    jar --create --file MainTest.jar --main-class=client.cli.MainTest -C target/ .

    echo "Creating GuiClient.jar"
    jar --create --file GuiClient.jar --main-class=client.gui.GuiDriver -C target/ .
    
    echo "Creating MultiServer.jar"
    jar --create --file MultiServer.jar --main-class=server.MultiServer -C target/ .
}

# Function to clean up
clean_up() {
    echo "Cleaning up..."
    rm -rf target/
}

# Main script execution
check_mysql
execute_sql_script
compile_source_code
extract_dependencies
create_fat_jars
clean_up