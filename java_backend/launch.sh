#!/bin/sh
export SPRING_DATASOURCE_JDBCURL="jdbc:postgresql://localhost:5432/user?user=pguser&password=passWord"
export SPRING_DATASOURCEDEMO_JDBCURL="jdbc:postgresql://localhost:5432/demo?user=pguser&password=passWord"
export SPRING_DATASOURCEEXTRA_JDBCURL="jdbc:postgresql://localhost:5432/extra?user=pguser&password=passWord"
java -Dspring.profiles.active=noauth -jar target/JavaBackendExemplar-1.0.0-SNAPSHOT.jar
