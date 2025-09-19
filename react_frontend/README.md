# React Frontend

React demonstration that uses the java_backend API.  This is intended more as an illustration than an exemplar,
as there is so much variation in frontend approaches that a prescribed exemplar would probably be unduly
restrictive. It does provide facilities that may serve as a good starting point, though.

## API configuration
Assumes the API is running at the URL specified in `.env.local`'s VITE_API_BASE, usually `http://localhost:8080/api` for local dev. It can be
overridden at build time by specifying the API URL via the `VITE_API_BASE` environment variable.

## SSO configuration
The frontend will obtain auth tokens from the identity provider specified in `.env.local`'s VITE_SSO_AUTHORITY,
usually `http://localhost:5556` for local dev. It can be overridden at build time by specifying the authority
via the `VITE_SSO_AUTHORITY` environment variable.

## Run
```bash
npm install
npm run dev
```

Open the displayed URL.

Changes to the frontend will automatically deploy.

## Build for deployment
```bash
npm install
npm run build
```

Distributable site will be in the `dist` directory.