#!/bin/sh
export SPRING_DATASOURCE_JDBCURL="jdbc:postgresql://localhost:5432/main?user=pguser&password=passWord"
export SPRING_DATASOURCETWO_JDBCURL="jdbc:postgresql://localhost:5432/two?user=pguser&password=passWord"
export SPRING_DATASOURCETHREE_JDBCURL="jdbc:postgresql://localhost:5432/three?user=pguser&password=passWord"
java -Dspring.profiles.active=noauth -jar target/JavaBackendExemplar-1.0.0-SNAPSHOT.jar
