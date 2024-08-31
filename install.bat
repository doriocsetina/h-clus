@echo off

REM Check if MySQL is running
sc query MySQL80 | findstr /i "RUNNING" >nul 2>&1
if %errorlevel% == 0 (
    echo MySQL is running
) else (
    echo MySQL is not running
    echo Starting MySQL... (you may be prompted for your password to start the DB service)

    set /p password=Enter the root password for MySQL: 
    echo Executing SQL script to populate database...
    mysql -u MapUser -p%password% < src\sql\SQLCreateTables.sql
)

echo Compiling source code...
javac -cp lib\* -d target\ src\*.java

echo Creating MainTest.jar
jar cfe MainTest.jar MainTest -C target .

echo Extracting dependencies from driver
powershell -command "Expand-Archive -Path lib\mysql-connector-j-9.0.0.jar -DestinationPath target\"

echo Creating MultiServer.jar
jar cfe MultiServer.jar server.MultiServer -C target .

echo Cleaning...
rmdir /s /q target

echo Done!
pause
