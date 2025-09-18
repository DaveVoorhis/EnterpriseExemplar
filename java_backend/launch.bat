set SPRING_DATASOURCE_JDBCURL=jdbc:postgresql://postgres_db:5432/main?user=pguser&password=pass@Word
set SPRING_DATASOURCETWO_JDBCURL=jdbc:postgresql://postgres_db:5432/two?user=pguser&password=pass@Word
set SPRING_DATASOURCETHREE_JDBCURL=jdbc:postgresql://postgres_db:5432/three?user=pguser&password=pass@Word
java -Dspring.profiles.active=mockoauth -jar target\JavaBackendExemplar-1.0.0-SNAPSHOT.jar
