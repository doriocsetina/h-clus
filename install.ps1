# Check if MySQL service is running
$mysqlService = Get-Service -Name mysql -ErrorAction SilentlyContinue

if ($mysqlService -and $mysqlService.Status -eq 'Running') {
    Write-Output "MySQL is running"
} else {
    Write-Output "MySQL is not running"
    Write-Output "Starting MySQL... (you will be prompted your password to start the DB service)"
    Start-Process "cmd.exe" -ArgumentList "/c net start MySQL80" -Verb RunAs
}

# Prompt for MySQL root password 

Write-Output "Executing SQL script to populate database..."

Invoke-Expression "mysql -u MapUser -p --execute 'source src/sql/SQLCreateTables.sql' "

Write-Output "Compiling source code..."
$sourceFiles = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
$classpath = "lib/*;."
javac -cp $classpath -d target $sourceFiles

Write-Output "Creating MainTest.jar"
jar cfe MainTest.jar MainTest -C target/ .

Write-Output "Extracting dependencies from driver"

Push-Location -Path target
Get-ChildItem -Path ../lib -Filter *.jar | ForEach-Object {
    jar xf $_.FullName
}
Pop-Location

Write-Output "Creating MultiServer.jar"
jar cfe MultiServer.jar server.MultiServer -C target/ .

Write-Output "Cleaning..."
# Remove-Item -Recurse -Force target/
