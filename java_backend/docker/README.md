This directory contains two Docker compositions for rapidly spooling up a SQL Server instance
with the required databases for testing the application, if you have Docker or Rancher Desktop installed.

The `standard` version uses prebuilt images that should work in any platform except 
M4 Apple Silicon processors.

The `m4` version builds an image for M4 processors, but may fail due to certificate issues
if your environment uses zScaler or similar man-in-the-middle security scanners.

To use it, go into the `standard` or `m4` directory as appropriate, and run the following 
to launch a SQL Server instance with the required databases in a Docker container:
```
docker-compose up -d
```

When you're done, run this to shut down the SQL Server instance:
```
docker-compose down
```
