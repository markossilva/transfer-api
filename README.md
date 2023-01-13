# Multi wallet api transfer

[![Build Status](https://app.travis-ci.com/kyriosdata/exemplo.svg)](https://app.travis-ci.com/markossilva/transfer-api.svg?branch=main)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=markossilva_transfer-api&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=markossilva_transfer-api)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=markossilva_transfer-api&metric=bugs)](https://sonarcloud.io/summary/new_code?id=markossilva_transfer-api)

## General info
This project is simples Lorem ipsum dolor generator.
Minimal [Spring Boot](http://projects.spring.io/spring-boot/) sample app.

## Requirements

For building and running the application you need:

- [JDK 11](https://www.oracle.com/java/technologies/downloads/#java11)
- [Maven 3](https://maven.apache.org)

## Setup
You can build a jar file and run it from the command line:


```
git clone https://github.com/markossilva/transfer-api.git
cd transfer-api
./mvnw package
java -jar target/transfer-api.jar
```

You can then access transfer-api here: http://localhost:8080/

Or you can run it from Maven directly using the Spring Boot Maven plugin.

```
./mvnw spring-boot:run
```
## Database configuration

In its default configuration, transfer-api uses an in-memory database (H2) which
gets populated at startup with data. The h2 console is automatically exposed at `http://localhost:8080/h2-console`
and it is possible to inspect the content of the database using the params.

```
url: jdbc:h2:file:./data/transferapi
username: sa
password: password
```

# Contributing

The [issue tracker](https://github.com/markossilva/transfer-api/issues) is the preferred channel for bug reports, features requests and submitting pull requests.

# License

Project is licensed under the [MIT](LICENSE) license.



