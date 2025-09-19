# Docker

This directory contains Docker compositions to support development and local testing.

## IMPORTANT: For frontend logins to work...

You **need** to map hostname `sso-emulator` to `127.0.0.1` in your local DNS resolver.
- MacOS/Unix/Linux: add the following line to `/etc/hosts`:
   ```
   127.0.0.1      sso-emulator
  ```
- Windows: add the following line to `C:\Windows\System32\Drivers\etc\hosts`:
   ```
   127.0.0.1      sso-emulator
   ```

This allows the backend
container to reference the IdP/OIDC/SSO server container by hostname `sso-emulator` inside the Docker network
whilst allowing the browser-hosted frontend to reference the IdP/OIDC/SSO server (which is port-mapped to the host) via the same
`sso-emulator` hostname from outside the Docker network.

## To build and run the entire application...

*This builds and runs the entire application from source with nothing more than Docker or Rancher Desktop. 
It's used to quickly deploy a working application for local testing.*

Navigate to the docker directory, then:
```shell
docker-compose up
```

The Docker log will show the build and deployment activity, which ends by launching the `announce_ready`
container to indicate the application is ready. You can then access the application from your browser via 
http://localhost. 

Subsequent Docker log entries show application activity.

See below for additional options.

## To build and run the application backend...

*This builds and runs the application backend from source. It's used to quickly deploy a working backend to support frontend
development.*

Navigate to the docker directory, then:
```shell
docker-compose -f docker-compose-allbackend.yaml up
```

The Docker log will show the build and deployment activity, which ends by launching the `announce_ready`
container to indicate the backend is ready. You can then access the application backend API from the frontend
by proceeding with the frontend launch described below.

Subsequent Docker log entries show detailed application activity.

Once the backend is running, navigate to the `react_frontend` directory and run:
```shell
npm install
npm run dev
```

That will launch the frontend in dev mode. It will provide the URL to browse the application.
Then you can make changes to frontend source and they'll deploy and refresh the browser automatically.

See below for additional options.

## To build and launch the application and run integration tests...

*This builds and runs the application stack, and then runs the integration tests on it. 
It uses a distinct database volume to keep it separate from the dev database.*

Navigate to the docker directory, then:
```shell
docker-compose -f docker-compose-integration-tests.yaml up
```

Once the docker containers have launched and the `test-runner` container is running, 
you can go to http://localhost:4444 to access the Selenium console and view browser activity. 
Use `secret` as the VNC password when prompted if you wish to view an individual browser.

The test results will be stored in the `target/reports/test-results` subdirectory of the `selenium_tests` project.

Note that the application containers will remain running after the tests complete (i.e., the `test-runner` container
exits) so that you can launch a local frontend to access the integration test backend as follows:
```shell
cd react_frontend
npm install
npm run dev
```
Then access the frontend via the displayed URL.

Note the application frontend running in Docker at http://localhost:80 is not accessible on the host outside the Docker network.
This is intentional, because the Dockerised frontend will not allow logins when configured to run integration tests via Docker.

See below for additional options.

## To launch the SQL database to support running the backend locally...

*This launches just the SQL DBMS in a container. It's useful when developing the backend.*

Navigate to the docker directory, then:
```shell
docker-compose -f docker-compose-sqldb.yaml up
```

Use the following connection strings:
```
SPRING_DATASOURCE_JDBCURL: "jdbc:postgresql://localhost:5432/main?user=pguser&password=${sqlpass}"
SPRING_DATASOURCETWO_JDBCURL: "jdbc:postgresql://localhost:5432/two?user=pguser&password=${sqlpass}"
SPRING_DATASOURCETHREE_JDBCURL: "jdbc:postgresql://localhost:5432/three?user=pguser&password=${sqlpass}"
```
Replace `${sqlpass}` with the password defined in the `.env` file.

## Environment Options

Environment files (`.env` and `*.env`) provide environment-specific settings for different host environments:

- `.env` Default settings.
- `nossl.env` Run with reduced SSL certificate checks to permit running on **dev-only** machines with zScaler.

These must be used in combination. For example, to launch all backend services with reduced SSL certificate checks:

```shell
docker-compose -f docker-compose-allbackend.yaml --env-file .env --env-file nossl.env up
```

Note that the order of `--env-file` specifications is important. The `.env` file must be specified first.
