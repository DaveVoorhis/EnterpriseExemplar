# Docker

This directory contains Docker compositions to support development.

## docker-compose.yaml

*This builds and runs the entire application from source with nothing more than Docker or Rancher Desktop.*

To build, deploy and run, navigate to the docker directory, then:

- To run on most platforms with default settings including reduced SSL checks to facilitate local dev deployments on machines with zScaler:
```shell
docker-compose up
```

- To build, deploy and run on Apple Silicon without certificate bypasses:
```shell
docker-compose --env-file .envm4 up
```

## docker-compose-allbackend.yaml

*This builds and runs the application backend from source. It's used to quickly deploy a working backend to support frontend
development.*

To build, deploy and run the backend, navigate to the docker directory, then:

- To run on most platforms with default settings including reduced SSL checks to facilitate local dev deployments on machines with zScaler:
```shell
docker-compose -f docker-compose-allbackend.yaml up
```

- To build, deploy and run on Apple Silicon, without certificate bypasses:
```shell
docker-compose -f docker-compose-allbackend.yaml --env-file .envm4 up
```

Once the backend is running, navigate to the `react_frontend` directory and run:
```shell
npm install
npm run dev
```

That will launch the frontend in dev mode. It will provide the URL to browse the application.
Then you can make changes to frontend source and they'll deploy and refresh the browser automatically.

# IMPORTANT: For frontend logins to work...

You **need** to map hostname `sso-emulator` to `127.0.0.1` in your system `/etc/hosts` (or equivalent.) 

This allows the backend
container to reference the IdP/OIDC/SSO server container by hostname `sso-emulator` inside the Docker network
whilst allowing the browser-hosted frontend to reference the IdP/OIDC/SSO server (which is port-mapped to the host) via the same
`sso-emulator` hostname from outside the Docker network.