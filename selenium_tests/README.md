# Selenium Tests

Selenium-based integration tests that drive the application via the frontend running in a browser.

**NOTE**: Work-in-progress. No guarantee this works as documented!

## Local Simple Tests

1 - Launch the application backend, such as via:
```shell
cd docker
docker-compose -f docker-compose-allbackend.yaml up
```
2 - Launch the application frontend, such as via:
```shell
cd react_frontend
npm install
npm run dev
```
3 - Run the unit tests, such as via:
```shell
cd selenium_tests
mvn test
```

## Local Advanced Tests

1 - Launch the application backend, such as via:
```shell
cd docker
docker-compose -f docker-compose-allbackend.yaml up
```
2 - Launch the application frontend, such as via:
```shell
cd react_frontend
npm install
npm run dev
```
3 - Launch the Selenium Docker containers:
```shell
cd selenium_tests/docker
docker-compose up
```
Once the docker containers have been launched, go to http://localhost:4444 to see the Selenium console.

Use 'secret' as the VNC password when prompted, if you wish to view interactive activity.

To enable Safari test capability on MacOS, run the following from the commandline:
```shell
safaridriver --enable
```
4 - Run the unit tests, such as via:
```shell
cd selenium_tests
mvn test
```
Or by running the `TestAllBrowsers` class, e.g.:
```shell
mvn exec:java
```
