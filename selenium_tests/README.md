# Selenium Tests

Selenium-based integration tests that drive the application via the frontend running in a browser.

To enable Safari test capability on MacOS, run the following from the commandline:
```shell
safaridriver --enable
```

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
Or by running the `TestAllBrowsers` class, e.g.:
```shell
cd selenium_tests
mvn exec:java
```
