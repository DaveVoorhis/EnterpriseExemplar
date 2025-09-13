# EnterpriseExemplar

Working and work-in-progress illustration of a database-driven enterprise application backend (written in Java using Spring Boot), 
frontend (written in JavaScript/HTML/CSS using React), integration tests (written in Java using Selenium) and Docker deployment using Docker Compose.

The running application demonstrates 'Demo' records that can be created, read, updated, deleted and searched. User
roles and permissions can be edited and defined.

NOTE: Since this is work in progress, at any time there may be broken, partly working or parts-all-over-the-workbench components. 
Currently, the `selenium_tests` subproject is mostly parts-on-the-workbench.

For details see:
- [java_backend/README.md](./java_backend/README.md)
- [react_frontend/README.md](./react_frontend/README.md)
- [selenium_tests/README.md](./selenium_tests/README.md)
- [docker/README.md](./docker/README.md)

## Quickstart

### 1 - Install
- Docker Desktop or Rancher Desktop.
- Node.js version 22 or above.

### 2 - Map hostname `sso-emulator` to `127.0.0.1`
- MacOS/Unix/Linux: add line `127.0.0.1      sso-emulator` to `/etc/hosts`
- Windows: add line `127.0.0.1      sso-emulator` to `C:\Windows\System32\Drivers\etc\hosts`

Short reason: it allows single-sign-on to work.

Longer reason: This allows the backend container to reference the local IdP/OIDC/SSO server container by hostname `sso-emulator` inside the Docker network
whilst allowing the browser-hosted frontend to reference the IdP/OIDC/SSO server (which is port-mapped to the host) via the same
`sso-emulator` hostname outside the Docker network.

### 3 - Launch the backend

- To run on most platforms but not Apple Silicon, with default settings including reduced SSL checks to facilitate local dev deployments on machines with zScaler:
```shell
cd docker
docker-compose -f docker-compose-allbackend.yaml up
```

- To build, deploy and run on Apple Silicon, without certificate bypasses:
```shell
cd docker
docker-compose -f docker-compose-allbackend.yaml --env-file .envm4 up
```

Once started, leave it running in its own window/shell/session/etc.

### 4 - Launch the frontend

Once the backend is running, run:
```shell
cd react_frontend
npm install
npm run dev
```

That will launch the frontend in dev mode. It will provide the URL to browse the application.
Changes to frontend source will deploy and refresh the browser automatically.

### 5 - Try the application

By default, the frontend will prompt for a login when you press the
*Log In* button. The following accounts are provided by the `sso-emulator` container which acts
as an IdP/OIDC/SSO server:

- `alice@example.com`
- `bob@example.com`
- `charlie@example.com`

All three have password `password`.

The first successful login will automatically be granted 'ADMIN' role. Subsequent logins will be granted 'User' role. Users will
not appear until they have logged in at least once. After users have logged in at least once, use the
first successfully logged-in account (which has 'ADMIN' role) to grant permissions to roles and grant roles to users.
