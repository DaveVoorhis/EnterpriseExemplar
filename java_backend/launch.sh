#!/bin/sh
export SPRING_DATASOURCE_JDBCURL="jdbc:sqlserver://127.0.0.1:1433;database=main;user=sa;password=pass@Word;encrypt=false"
export SPRING_DATASOURCETWO_JDBCURL="jdbc:sqlserver://127.0.0.1:1433;database=two;user=sa;password=pass@Word;encrypt=false"
export SPRING_DATASOURCETHREE_JDBCURL="jdbc:sqlserver://127.0.0.1:1433;database=three;user=sa;password=pass@Word;encrypt=false"
java -Dspring.profiles.active=mockoauth -jar target/JavaBackendExemplar-1.0.0-SNAPSHOT.jar
