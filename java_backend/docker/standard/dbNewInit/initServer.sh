#!/bin/bash

databases=( \
   "main" \
   "two" \
   "three"
)

sqlcmd=/opt/mssql-tools/bin/sqlcmd

user=SA
pwd=pass@Word

echo ">>>>>>>>>>>> initServer: launching <<<<<<<<<<<<<"

is_up=1
while [ $is_up -ne 0 ]; do
   echo ">>>>>>>>>>>> initServer: waiting for SQL Server to become available... <<<<<<<<<<<<<"
   $sqlcmd -l 30 -S sqlserver_db -h-1 -V1 -U $user -P $pwd -Q "SET NOCOUNT ON SELECT 'Check Availability', @@servername"
   is_up=$?
   sleep 5
done

echo ">>>>>>>>>>>> initServer: SQL Server is available <<<<<<<<<<<<<"
echo ">>>>>>>>>>>> initServer: creating databases <<<<<<<<<<<<<"
# The -l 60 sets a login timeout of 60 seconds.
for dbName in ${databases[@]}; do
    echo ">>>>>>>>>>>> initServer: creating database $dbName <<<<<<<<<<<<<"
    $sqlcmd -l 60 -S sqlserver_db -U $user -P $pwd -Q "CREATE DATABASE $dbName"
done

echo ">>>>>>>>>>>> initServer: done <<<<<<<<<<<<<"
