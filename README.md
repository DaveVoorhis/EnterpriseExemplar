# EnterpriseExemplar

Working illustration of a database-driven enterprise application backend, frontend, integration tests and Docker deployment.

For details see:
- [java_backend/README.md](./java_backend/README.md)
- [react_frontend/README.md](./react_frontend/README.md)
- [selenium_tests/README.md](./selenium_tests/README.md)
- [docker/README.md](./docker/README.md)

## Quickstart

### 1 - Install Docker Desktop or Rancher Desktop.

### 2 - Launch the backend:

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

#### IMPORTANT: For frontend logins to work...

You **need** to map hostname `sso-emulator` to `127.0.0.1` in your system `/etc/hosts` (or equivalent.)

This allows the backend
container to reference the IdP/OIDC/SSO server container by hostname `sso-emulator` inside the Docker network
whilst allowing the browser-hosted frontend to reference the IdP/OIDC/SSO server (which is port-mapped to the host) via the same
`sso-emulator` hostname.

### 3 - Launch the frontend:

Once the backend is running, run:
```shell
cd react_frontend
npm install
npm run dev
```

That will launch the frontend in dev mode. It will provide the URL to browse the application.
Then you can make changes to frontend source and they'll deploy and refresh the browser automatically.

By default, the frontend will prompt for a login when you press the
*Log In* button. The first successful login will automatically be granted 'ADMIN' 
role. The following accounts are provided by the `sso-emulator` container which acts 
as an IdP/OIDC/SSO server:

- alice@example.com
- bob@example.com
- charlie@example.com

All three have password `password`.