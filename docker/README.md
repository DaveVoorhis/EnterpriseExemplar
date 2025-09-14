# Docker

This directory contains Docker compositions to support development and local testing.

## IMPORTANT: For frontend logins to work...

You **need** to map hostname `sso-emulator` to `127.0.0.1` in your local DNS resolver.
- MacOS/Unix/Linux: add line `127.0.0.1      sso-emulator` to `/etc/hosts`
- Windows: add line `127.0.0.1      sso-emulator` to `C:\Windows\System32\Drivers\etc\hosts`

This allows the backend
container to reference the IdP/OIDC/SSO server container by hostname `sso-emulator` inside the Docker network
whilst allowing the browser-hosted frontend to reference the IdP/OIDC/SSO server (which is port-mapped to the host) via the same
`sso-emulator` hostname from outside the Docker network.

## To build and run the entire application...

*This builds and runs the entire application from source with nothing more than Docker or Rancher Desktop. 
It's used to quickly deploy a working application for local testing.*

Navigate to the docker directory, then:

- For most platforms:
```shell
docker-compose up
```

- For Apple Silicon:
```shell
docker-compose --env-file .env --env-file m4.env up
```

See below for additional environment options.

## To build and run the application backend...

*This builds and runs the application backend from source. It's used to quickly deploy a working backend to support frontend
development.*

Navigate to the docker directory, then:

- For most platforms:
```shell
docker-compose -f docker-compose-allbackend.yaml up
```

- For Apple Silicon:
```shell
docker-compose -f docker-compose-allbackend.yaml --env-file .env --env-file m4.env up
```

Once the backend is running, navigate to the `react_frontend` directory and run:
```shell
npm install
npm run dev
```

That will launch the frontend in dev mode. It will provide the URL to browse the application.
Then you can make changes to frontend source and they'll deploy and refresh the browser automatically.

See below for additional environment options.

## To build and launch the application and run integration tests...

**NOTE**: This is a work in progress.

Navigate to the docker directory, then:

- For most platforms:
```shell
docker-compose -f docker-compose-integration-tests.yaml up
```

- For Apple Silicon:
```shell
docker-compose -f docker-compose-integration-tests.yaml --env-file .env --env-file m4.env up
```

See below for additional environment options.

## Environment Options

Environment files (`.env` and `*.env`) provide environment-specific settings for different host environments:

- `.env` Default settings.
- `m4.env` Run on Apple Silicon CPUs.
- `nossl.env` Run with reduced SSL certificate checks to permit running on **dev-only** machines with zScaler.

These can be used in combination. For example, to launch all backend services on a development M4 MacOS system with reduced SSL certificate checks:

```shell
docker-compose -f docker-compose-allbackend.yaml --env-file .env --env-file m4.env --env-file nossl.env up
```

Note that the order of `--env-file` specifications is important. The `.env` file must be specified before `m4.env`, and it before `nossl.env`.
