# Hello World API - Spring Boot Project

A simple Spring Boot REST API that returns greeting messages with Swagger/OpenAPI documentation.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Project Structure

```
.
├── pom.xml                                  # Maven configuration with Spring Boot and Swagger
├── src/
│   ├── main/
│   │   ├── java/com/example/helloworld/
│   │   │   ├── HelloWorldApplication.java   # Spring Boot main application
│   │   │   ├── controller/
│   │   │   │   └── GreetingController.java  # REST API endpoints
│   │   │   ├── model/
│   │   │   │   └── Greeting.java            # Response model
│   │   │   └── config/
│   │   │       └── OpenApiConfig.java       # Swagger/OpenAPI configuration
│   │   └── resources/
│   │       └── application.properties       # Application configuration
│   └── test/
│       └── java/com/example/helloworld/
│           └── controller/
│               └── GreetingControllerTest.java  # Unit tests
```

## Building the Project

```bash
# Build the project and create an executable JAR
mvn clean package

# Skip tests during build (optional)
mvn clean package -DskipTests
```

## Running the Application

### Option 1: Using Maven
```bash
mvn spring-boot:run
```

### Option 2: Using the built JAR
```bash
java -jar target/hello-world-api-1.0.0.jar
```

## API Endpoints

The application runs on `http://localhost:8080` by default.

### 1. Hello Endpoint
- **URL**: `GET /hello`
- **Parameters**: `name` (optional, default: "World")
- **Example**: `http://localhost:8080/hello?name=John`
- **Response**:
```json
{
  "message": "Hello, John!"
}
```

### 2. Greeting Endpoint
- **URL**: `GET /greeting`
- **Parameters**: `firstName` (optional, default: "World")
- **Example**: `http://localhost:8080/greeting?firstName=Jane`
- **Response**:
```json
{
  "message": "Greetings, Jane!"
}
```

## Swagger UI Documentation

Access the interactive Swagger documentation at:
```
http://localhost:8080/swagger-ui.html
```

API documentation in JSON format:
```
http://localhost:8080/v3/api-docs
```

## Running Tests

```bash
# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=GreetingControllerTest
```

## Dependencies

- **Spring Boot 3.2.1**: Framework for building the REST API
- **SpringDoc OpenAPI 2.3.0**: Swagger/OpenAPI documentation generation
- **Spring Boot Test**: Testing framework (JUnit 5)

## Future API Additions

To add new endpoints to this project:

1. Create a new method in `GreetingController` or a new controller class
2. Add Swagger annotations (`@Operation`, `@ApiResponse`, `@Tag`) for documentation
3. Create corresponding model classes in the `model` package
4. Add unit tests in the `test` package
5. Rebuild the project

The Swagger dependency is already configured, so all new endpoints will automatically appear in the Swagger UI.

## Configuration

Edit `src/main/resources/application.properties` to customize:
- Server port
- Application name
- Logging levels
- Other Spring Boot properties

## Troubleshooting

- **Port 8080 already in use**: Change the port in `application.properties`
  ```properties
  server.port=8081
  ```
- **Build failures**: Ensure Java 17+ is installed: `java -version`
- **Maven issues**: Clear the local cache: `mvn clean install -U`
