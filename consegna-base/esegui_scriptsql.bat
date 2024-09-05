@echo off

powershell -Command "Invoke-Expression 'mysql -u MapUser -p --execute \"source ../src/sql/SQLCreateTables.sql\"'"

if %ERRORLEVEL% EQU 0 (
    echo Tabelle popolate con successo.
) else (
    echo Lo script si è imbattuto in un errore.
)

pause