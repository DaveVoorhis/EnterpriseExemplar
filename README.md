# EnterpriseExemplar

This is a working work-in-progress illustration of a database-driven enterprise application consisting of:
- A SQL database-driven backend that exposes a RESTful API authenticated against an identity provider, written in Java using Spring Boot.  [README.md](./java_backend/README.md)
- A Browser-hosted frontend that uses the backend API and supports sign-on via an identity provider, written in JavaScript/HTML/CSS using React.  [README.md](./react_frontend/README.md)
- Browser-driving end-to-end integration tests written in Java using Selenium.  [README.md](./selenium_tests/README.md)
- Docker deployment using Docker Compose.  [README.md](./docker/README.md)

The running application demonstrates 'Demo' records that can be created, read, updated, deleted and searched. User
roles and permissions can be edited and defined.

## Quickstart

### 1 - Install
- Docker Desktop or Rancher Desktop.

### 2 - Map hostname `sso-emulator` to `127.0.0.1`
- MacOS/Unix/Linux: add the following line to `/etc/hosts`:
   ```
   127.0.0.1      sso-emulator
  ```
- Windows: add the following line to `C:\Windows\System32\Drivers\etc\hosts`:
   ```
   127.0.0.1      sso-emulator
   ```

Short reason: it allows user/password logins to work.

Longer reason: This allows the backend container to reference the local IdP/OIDC/SSO server container by hostname `sso-emulator` inside the Docker network
whilst allowing the browser-hosted frontend to reference the IdP/OIDC/SSO server (which is port-mapped to the host) via the same
`sso-emulator` hostname outside the Docker network.

### 3 - Build and launch the application stack

- To build, deploy and run on most platforms:
```shell
cd docker
docker-compose up
```

See the Docker [README.md](./docker/README.md) for additional options and information.

Once started, leave the Docker containers running in their own window/shell/session/etc.

### 4 - Try the application

Point your browser at http://localhost

By default, the frontend will prompt for a login when you press the
*Log In* button. The following accounts are provided by the `sso-emulator` Docker container which acts
as an IdP/OIDC/SSO server:

- `alice@example.com`
- `bob@example.com`
- `charlie@example.com`

All three have password `pwd`.

The first successful login will automatically be granted 'ADMIN' role. Subsequent logins will be granted 'USER' role. Users will
not appear in the application until they have logged in at least once. After users have logged in at least once, use the
first successfully logged-in account (which has 'ADMIN' role) to grant permissions to roles and grant roles to users.
