package org.reldb.exemplars.java.backend;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(classes = {Application.class})
@TestPropertySource(locations = {"/application-test.properties"})
@ActiveProfiles("mockoauth")
@Sql("/test.sql")
public class ApplicationTestBase {
}
