# Java Backend

This is an example Java backend, a conventional Java-based Spring Boot application service that exposes a RESTful CRUD API to
**C**reate **R**ead **U**pdate and **D**elete instances of an example Demo entity type.

In addition, it defines user accounts that can work with Single Sign On, and can authorise access
to individual endpoints with a flexible user roles and permissions system. API endpoints are provided to manage
users and their roles and permissions.

It also illustrates how multiple databases can be accessed, as this is a common requirement for real-world projects.

## API Endpoints

### Application RESTful API

In deployed environments all API endpoints will normally be prefixed with the host name and port followed by `/api`. 
Where `{...}` appears below, e.g., `{demoId}`, replace the braces and content with the appropriate literal value.

NOTE: Only partial URL paths are shown below. For example, to get Demo ID 3 from a deployed backend running on localhost on port 8080, the URL must be
http://localhost:8080/api/demo/3

#### Demos

- GET all demos: `/demo`
- GET a demo: `/demo/{demoId}`
- PUT an update to an existing demo: `/demo/{demoId}`
  - Payload is defined in DemoIn.java
- POST a new demo: `/demo`
  - Payload is defined in DemoIn.java
- DELETE an existing demo: `/demo/{demoId}`

#### Users

- GET all users: `/users`
- GET a user for a given user ID: `/users/{userId}`
- GET the current (calling) user based on bearer token information: `/users/current`
  - This endpoint does not require a user to be enabled.
- PUT a user's enabled/disabled status as a JSON boolean for a given user ID: `/users/{userId}`
  - Body is `src/main/java/org/reldb/exemplars/java/backend/api/model/UserSetEnabledIn.java`
- GET whether user has a named permission or not: `/users/permission/{permissionName}`
  - Permission names are from the `enums.org.reldb.exemplars.java.backend.Permissions` enum.
- GET list of all possible permissions: `/users/permissions`
- GET list of all permissions held by the current user: `/users/mypermissions`
- GET all roles: `/users/roles`
- DELETE a role: `/users/roles/{roleId}`
- POST a new role: `/users/roles`
  - Body is `src/main/java/org/reldb/exemplars/java/backend/api/model/RoleIn.java`
- PUT an update to an existing role: `/users/roles/{roleId}`
  - Body is `src/main/java/org/reldb/exemplars/java/backend/api/model/RoleIn.java`
- GET all roles for a given user: `/users/{userId}/roles`
- POST add (grant) a role to a given user: `/users/{userId}/roles/{roleId}`
- DELETE (revoke) a role from a given user: `/users/{userId}/roles/{roleId}`
- GET all permissions for a given role: `/users/role/{roleId}/permissions`
- POST add (grant) a permission to a given role: `/users/role/{roleId}/permissions/{permissionName}`
- DELETE (revoke) a permission from a given role: `/users/role/{roleId}/permissions/{permissionName}`

NOTE: Bearer token (i.e., `Authorization` header with value `Bearer xxxxxx`) must be provided,
but can be any value if profile `mockoauth` is active.

See `*Api.java` classes in the `api` package for API definition.

### Application Metadata API

Via HTTP:

```shell
http://localhost:8080/api/actuator/info
```

#### Healthcheck

Via HTTP:

```shell
http://localhost:8080/api/actuator/health
```

#### Metrics

Via HTTP:

```shell
http://localhost:8080/api/actuator/prometheus
```

#### All Available Actuators

Via HTTP:

````shell
http://localhost:8080/api/actuator
````

For more info, see https://www.baeldung.com/spring-boot-actuators

## Backend Architecture

This backend uses a simple layered architecture where:

- The `service` package represents the core functionality or service layer of the application backend;

- The `persistence` package represents the storage or database interface level of the application backend, and is only accessed by the `service` layer and `mappers` package in the API layer;

- The `api` package represents the exposed API layer of the application backend.
  It defines the mainly RESTful API endpoints and manages its own DTOs, security, API logging with correlation IDs (MDC), and
  service proxy. It directly references only the application `service` layer via
  service proxies named `*ServiceAdapter`.

## System Requirements

### Runtime (minimum)

- Java JRE/JDK 21 or above
- SQL Server DBMS instance with databases named `main`, `two`, `three`. See [./docker/README.md](docker/README.md) for Docker launch instructions.

### Build

- Java JDK 21 or above
- Maven 3.9.8 or above (command-line or integrated into IDE; earlier versions may work)

## Build the Backend

1. Copy this distribution to a local directory.
1. Go into your local directory and run `mvn clean install` to clean, compile, unit test, and generate a runnable backend jar in the `target` folder.

## Deploy the Backend

### 1. Setup the Databases

The application expects to access three pre-existing databases, as follows:
- `main` contains tables for managing users;
- `two` contains a 'demo' table;
- `three` contains nothing, as it illustrates access to additional databases.

The databases are identified by connection strings specified in environment variables, for example:

```shell
SPRING_DATASOURCE_JDBCURL="jdbc:sqlserver://my-sql-server:1433;database=main;user=dave;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
SPRING_DATASOURCETWO_JDBCURL="jdbc:sqlserver://my-sql-server:1433;database=two;user=dave;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
SPRING_DATASOURCETHREE_JDBCURL="jdbc:sqlserver://my-sql-server:1433;database=three;user=dave;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
```

Note that the passwords have been elided in the above, so you'll need to obtain them and replace `{your_password_here}` with the real passwords and
probably use a different user account from `dave`.

The Liquibase database migrator is configured to manage database schema changes for the `main` and `two` databases.

### 2. Setup Authentication

Frontend clients are expected to obtain a valid bearer token if they have means of retrieving one through a login flow,
and pass it as a valid `Authorization` header value in every RESTful endpoint call to the backend API. The backend will validate the bearer token
unless explicitly bypassed as described below under *Local Development*.

Note: Authentication is not required by the actuator (metadata/healthcheck/metrics/etc.) API (see below) under the assumption
that access to it (if it is required) will be controlled by upstream firewall rules or other external access mechanisms.

### 3. Setup User Access

Normally, users will authenticate via SSO in the frontend, which will result in the frontend sending
the bearer token in an `Authorization: Bearer xxxxx` header in each backend call. The backend will attempt
to parse and validate the bearer token.

If it is invalid, an authentication error will be returned.

If it is valid, the backend will look for the authenticated username
(obtained from the parsed bearer token) in the `app_user` table of the `main` database, and then:

- If a matching row is found, the backend call will proceed if the `enabled` attribute is non-zero or `true`,
  otherwise it will fail with an authentication error.
- If a matching row is not found, a row will be added to the database with the `enabled` attribute set to the 
  value of the `new-user-is-enabled-by-default` configuration setting. If it's `false`, the backend call 
  will fail with an authentication error.

#### Configuring the Database - First User

The relevant `main`, `two` and `three` databases are assumed to already exist.

When a new user logs into the system, the account details will be automatically added to the `app_user` table
of the `main` database.

There are two configuration settings that determine how the new user will be configured:
- `new-user-is-enabled-by-default`: If this is set to `true`, the new user will have `enabled` set to `true`,
  which means the user can perform any authorised operations. If set to false, the new user will have
  `enabled` set to false, which means the user may not perform any operations.
- `first-user-is-admin`: If this is set to `true` and the new user is the first user to log in then the user
  will be granted both `USER` and `ADMIN` privilege. Otherwise, the user will only be granted `USER` privilege. 

It is recommended that for production deployment, `first-user-is-admin` be set to `false` and the first user
be manually granted `ADMIN` privilege as described below.

If `new-user-is-enabled-by-default` is set to `false`, the first user will need to be manually enabled
in the `main` database via SQL query. E.g.:
```sql
UPDATE app_users SET enabled = 1 WHERE email = '<username>';
```

If `first-user-is-admin` is set to `false`, a user will need to be designated as administrator and 
granted the `ADMIN` role which allows all access:
```sql
INSERT INTO user_roles (role_id, user_id) SELECT 1, user_id FROM app_users WHERE email = '<username>';
```
Replace `<username>` in the above with the user's username, which is usually their email address.

All newly-added users will automatically be
granted the `USER` role which will give them access to functionality defined by the
permissions given to the `USER` role. Until the `USER` role is assigned permissions (via SQL query or 
by an `ADMIN` role user) the `USER` role has **no** permissions.

For further information on roles and permissions, see the *Manage User Roles and Permissions* section below.

## Run the Backend

### Run the Backend for Local Development

Assuming you're in your local directory, there are multiple ways to start the application locally:

1. Command-line (Maven): `mvn spring-boot:run -Dspring-boot.run.profiles=mockoauth`

2. Command-line (Java): `java -jar target/JavaBackendExemplar-0.0.1.jar`

3. IDE: Run `Application`.

NOTE: To run you need to specify environment connection strings to real databases, as described above in *1. Setup the Databases*.

NOTE: If you're setting up a real deployment with frontend and backend and a fresh set of databases being used for the first time, 
you will need to enable the first user in the `main` database, as described above in *"3. Setup User Access | Configuring the First User"*.

To simplify application launch, you may wish to create a shell (MacOS) or CMD (Windows) script.

E.g., for Windows create a `launch.cmd` file within the backend project root:

```shell
set SPRING_DATASOURCE_JDBCURL=jdbc:sqlserver://my-sql-server:1433;database=main;user=dave;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;
set SPRING_DATASOURCETWO_JDBCURL=jdbc:sqlserver://my-sql-server:1433;database=two;user=dave;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;
set SPRING_DATASOURCETHREE_JDBCURL=jdbc:sqlserver://my-sql-server:1433;database=three;user=dave;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;
java -jar target\JavaBackendExemplar-0.0.1.jar
``` 

For MacOS or Linux, create a `launch.sh` file within the backend project root:

```shell
#!/bin/sh
export SPRING_DATASOURCE_JDBCURL="jdbc:sqlserver://my-sql-server:1433;database=main;user=dave;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
export SPRING_DATASOURCETWO_JDBCURL="jdbc:sqlserver://my-sql-server:1433;database=two;user=dave;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
export SPRING_DATASOURCETHREE_JDBCURL="jdbc:sqlserver://my-sql-server:1433;database=three;user=dave;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
java -jar target/JavaBackendExemplar-0.0.1.jar
```

**NOTE**: Replace `{your_password_here}` and `dave` with the appropriate credentials for your environment.

You can verify that the databases are accessible from the application console log. If you see entries like the following for all
three databases, it's connecting to real databases:
```
... Added connection ConnectionID:1 ClientConnectionId: 7b0410a0-1ac1-40df-abda-f9b6a92240df
```
If you see any log entries like the following for any of the three databases, it's failed to read the connection string from an environment variable:
```
... Added connection conn0: url=jdbc:h2:mem:testdb user=SA
```
It may also fail to launch at all, and will show errors in the log related to failed JDBC connections like `java.lang.RuntimeException: Failed to get driver instance for jdbcUrl=jdbc:h2:mem:testdb;user=sa;MODE=MSSQLServer;`

**WARNING: Set your SCM configuration to ***not*** upload your launch script, to avoid uploading passwords to the code repository.**

**Note:** the environment variable names have been constructed from property values defined in `application.yaml` per the Spring documentation at https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.typesafe-configuration-properties.relaxed-binding.environment-variables
I.e., uppercase, dots to underscores, dashes to nothing.

### Special Configuration Profiles

#### Bypassing authentication for local development
If you start the application with the Spring profile 'mockoauth' (`-Dspring.profiles.active=mockoauth`) as shown above,
it will treat all bearer tokens as valid, i.e., you can call the application with any bearer token (e.g., an `Authorization` header containing `Bearer blahblahblah`) and it will ignore the contents.
E.g., to launch the .jar under Windows with the `mockoauth` profile:
```shell
java -jar -Dspring.profiles.active=mockoauth target\JavaBackendExamplar-0.0.1.jar
```

This profile should not be used for production or test deployments, only local development.

#### Console Logging Mode

When running with no Spring profiles specified, console logging is exception-oriented and will show startup
configuration, settings, warnings, exceptions and errors. Activity logging, such as background
processes, API calls etc. is suppressed.

When running with the `mockoauth` profile, console logging is activity-oriented and
will show startup configuration, settings, warnings, exceptions and errors, plus background
processes, API calls, and so on, to aid development and debugging.

Activity-oriented logging can also be enabled by starting the application with the Spring profile 'verbose'
(`-Dspring.profiles.active=verbose`), e.g.:
```shell
java -jar -Dspring.profiles.active=verbose target\JavaBackendExemplar-0.0.1.jar
```

### Run the Demo Backend for Test/QA/Prod Deployment

1. Set database and other environment variables as required.
2. Launch via ```java -jar target\JavaBackendExemplar-0.0.1.jar```

E.g.:
```shell
#!/bin/sh
export SPRING_DATASOURCE_JDBCURL="jdbc:sqlserver://my-sql-server.database.windows.net:1433;database=main;user=dave;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
export SPRING_DATASOURCETWO_JDBCURL="jdbc:sqlserver://my-sql-server.database.windows.net:1433;database=two;user=dave;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
export SPRING_DATASOURCETHREE_JDBCURL="jdbc:sqlserver://my-sql-server.database.windows.net:1433;database=three;user=dave;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
java -jar target/JavaBackendExemplar-0.0.1.jar
```

## Additional Build Features

In addition to compiling and building the Java source code into a runnable application backend .jar file,
the POM.xml Maven build file supports some additional features:

### Linting

```shell
mvn spotless:check
```

Spotless can fix formatting issues it reported. To apply changes:

````shell
mvn spotless:apply
````

Skipping spotless checks in code can be done as follows:

```
// spotless:off
@NativeQuery(value = """
        SELECT roles.*
          FROM user_roles, roles
         WHERE user_roles.user_id = :userId
           AND user_roles.role_id = roles.role_id
        """)
// spotless:on
```

### OWASP Dependency Vulnerability Testing

````shell
mvn dependency-check:check
````

Note that without an API key, the initial run of OWASP tests will be slow.

### Display Dependency Updates

````shell
mvn versions:display-property-updates
````

### Unit Test Coverage

Unit test coverage using the Jacoco plugin is automatically run by build operations that run unit tests, e.g.:

```shell
mvn clean install
```

Or:

```shell
mvn clean test
```

The test coverage report is found in `target/site/jacoco/` and its `index.html` file 
allows it to be viewed with any Web browser.

## Manage User Roles and Permissions

This section provides details on how the roles/permissions system works and how it can be managed externally via
SQL queries against the `main` database.

The following `main` database tables specify the roles and permissions:
- `app_users` identifies all users;
- `roles` identifies all possible roles;
- `user_roles` identifies roles granted to each user;
- `role_permissions` identifies permissions granted by a role.

The permissions are defined in the `Permissions` enumerated type. Almost every
backend endpoint specifies a corresponding permission required to access it using the `@Permit(...)` annotation.
Endpoints without a `@Permit(...)` annotation may be accessed by any logged-in user.

On every backend startup, two system roles -- Role ID 1 (ADMIN) and Role ID 2 (USER) -- will be automatically
created in the `roles` table if they don't already exist and all permissions will be granted to Role ID 1 (ADMIN).

The 'ADMIN' role (ID 1) should be manually assigned to at least one user, as described above. That user will have
permission to perform every backend operation.

No permissions are automatically assigned to the USER (ID 2) role. New users will be automatically given the
USER role. It is assumed that an administrator (user with ADMIN role) will either assign permissions to the USER (ID 2) role,
or will define other roles and not use the USER (ID 2) role and will assign new users
to roles as appropriate.

User roles can be externally managed using the following queries. In each, replace
`dave.voorhis@reldb.org` with the relevant user's email address.

```sql
-- Revoke all roles for user dave.voorhis@reldb.org
DELETE FROM user_roles
      WHERE user_id IN (
          SELECT user_id
            FROM app_users
          WHERE email = 'dave.voorhis@reldb.org');
```
```sql
-- Revoke ADMIN role for user dave.voorhis@reldb.org
DELETE FROM user_roles
      WHERE role_id = 1
        AND user_id IN (
          SELECT user_id
            FROM app_users
           WHERE email = 'dave.voorhis@reldb.org');
```
```sql
-- Revoke USER role for user dave.voorhis@reldb.org
DELETE FROM user_roles
      WHERE role_id = 2
        AND user_id IN (
          SELECT user_id
            FROM app_users
           WHERE email = 'dave.voorhis@reldb.org');
```
```sql
-- Grant ADMIN role for user dave.voorhis@reldb.org
INSERT INTO user_roles (user_id, role_id)
   SELECT user_id, 1
     FROM app_users
    WHERE user_id NOT IN (
        SELECT user_id 
          FROM user_roles
         WHERE user_roles.user_id=app_users.user_id AND role_id=1)
      AND email='dave.voorhis@reldb.org';
```
```sql
-- Grant USER role for user dave.voorhis@reldb.org
INSERT INTO user_roles (user_id, role_id)
   SELECT user_id, 2
     FROM app_users
    WHERE user_id NOT IN (
        SELECT user_id 
          FROM user_roles 
          WHERE user_roles.user_id=app_users.user_id AND role_id=2)
      AND email='dave.voorhis@reldb.org';
```
