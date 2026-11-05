# Calculator API

A small Kotlin and Spring Boot API for arithmetic on two numbers. Results are returned as JSON numbers. Swagger UI lets you try each operation in the browser.

## Run

Install JDK 17 or newer and Git. The Gradle wrapper downloads Gradle and the project dependencies on the first run, so an Internet connection is needed.

```sh
git clone https://github.com/thejosht/seg3502-lab6-calculator.git
cd seg3502-lab6-calculator
./gradlew bootRun
```

On Windows, use `gradlew.bat bootRun` instead. Open [Swagger UI](http://localhost:8080/swagger-ui.html). The root address also redirects there. The server listens only on this computer.

If port 8080 is already in use:

```sh
./gradlew bootRun --args='--server.port=8081'
```

Use the chosen port in the URLs below.

## Operations

Both path parameters are numbers and may be negative or decimal.

| Method | Route | Example result |
| --- | --- | --- |
| GET | `/calculator/add/10/2` | `12.0` |
| GET | `/calculator/subtract/10/2` | `8.0` |
| GET | `/calculator/multiply/10/2` | `20.0` |
| GET | `/calculator/divide/7/2` | `3.5` |

Each route follows `/calculator/{operation}/{nombre1}/{nombre2}`. Subtraction and division use the first number followed by the second.

```sh
curl http://localhost:8080/calculator/add/10/2
curl http://localhost:8080/calculator/subtract/10/2
curl http://localhost:8080/calculator/multiply/10/2
curl http://localhost:8080/calculator/divide/7/2
```

Invalid numbers, non-finite values, division by zero and numeric overflow return HTTP 400 with a short message. Calculations use `Double`, so ordinary floating-point rounding applies. There is no database or API key to configure.

## OpenAPI

- [Swagger UI](http://localhost:8080/swagger-ui.html)
- [JSON specification](http://localhost:8080/v3/api-docs)
- [YAML specification](http://localhost:8080/v3/api-docs.yaml)

The specification is generated from the controller and documents all four routes, path parameters, numeric results and HTTP 400 errors.

## Test and build

```sh
./gradlew clean test bootJar
java -jar build/libs/calculator-api-0.0.1-SNAPSHOT.jar
```

The tests call a real server on a random local port. They cover all operations, negative and decimal inputs, invalid values, zero division, overflow, documentation and routing. Open `build/reports/tests/test/index.html` for the results. Stop a running server with Ctrl+C.

## References

- [Spring Boot documentation](https://docs.spring.io/spring-boot/reference/)
- [springdoc OpenAPI documentation](https://springdoc.org/)
