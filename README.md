# Pinecone Vector Database Demo

> [!NOTE]
> Please read this article to know more about this repo!  
> https://open.substack.com/pub/maclt/p/vector-database-tutorial

A Spring Boot application demonstrating how to use the Pinecone vector database with Kotlin. This project provides a REST API for vector operations including insertion, search, and random vector generation.

## Overview

This application demonstrates:
- Integration with Pinecone vector database using the official Java client
- Spring Boot REST API implementation
- Vector similarity search operations
- Structured logging with SLF4J and Log4j2

## Prerequisites

- Java 17 or higher
- Maven
- Pinecone API key (free tier available at [pinecone.io](https://www.pinecone.io/))

## Project Structure

```
src/
├── main/
│   └── kotlin/net/taromurakami/pinecone/
│       ├── domain/                 # Domain models
│       │   ├── QueryResult.kt      # Query result data class
│       │   └── Vector.kt           # Vector data class
│       ├── presentation/           # API controllers
│       │   └── MainController.kt   # REST endpoints
│       ├── repository/             # Data access
│       │   └── PineconeRepository.kt # Pinecone client wrapper
│       ├── service/                # Business logic
│       │   └── MainService.kt      # Vector operations
│       └── App.kt                  # Application entry point
└── test/
    ├── http/                       # HTTP request examples
    │   └── test.http               # API test requests
    └── kotlin/                     # Unit tests
```

## Configuration

### Pinecone API Key

Set your Pinecone API key as an environment variable:

```shell
export PINECONE_API_KEY=your_api_key_here
```

### Logging

The service uses Spring Boot's built-in logging. The configuration file is located at `src/main/resources/application.properties`.

Logs are written to both the console and a file at `logs/app.log`.

## API Endpoints

The application exposes the following REST endpoints:

### Insert Vector
```
POST /vectors
Content-Type: application/json

[0.1, 0.2, 0.3]
```
Response: Returns the created vector with a generated UUID

### Query Similar Vectors
```
GET /vectors/query?limit=10
Content-Type: application/json

[0.1, 0.2, 0.3]
```
Parameters:
- `limit` (optional): Maximum number of results to return (default: 3)

Response: Returns matching vectors with similarity scores

### Find Vector by ID
```
GET /vectors/find-by-id/{id}
```
Parameters:
- `id` (required): The unique identifier of the vector to retrieve

Response: Returns the vector with the specified ID

### Insert Random Vector
```
POST /vectors/random
```
Response: Returns the created random vector with a generated UUID

## Build Commands

Format and check code style:
```shell
mvn ktlint:check
mvn ktlint:format
```

Run tests:
```shell
mvn test
```

Build the project:
```shell
mvn package
mvn clean package
mvn clean package -DskipTests
```

## Run Commands

Run with Spring Boot:
```shell
mvn spring-boot:run
```

Run the packaged JAR:
```shell
java -jar target/pinecone-1.0-SNAPSHOT.jar
```

## Dependencies

- Spring Boot 3.2.3
- Kotlin 2.1.20
- Pinecone Java Client 4.0.1
- JUnit 5.12.2
- Log4j2 (via Spring Boot)

For a complete dependency tree:
```shell
mvn dependency:tree
```

## How It Works

1. The application initializes a connection to Pinecone using your API key
2. It creates a "quickstart" index if it doesn't exist
3. The REST API allows you to:
   - Insert vectors with unique IDs
   - Query for similar vectors using cosine similarity
   - Find specific vectors by their ID
   - Generate and insert random normalized vectors

Each vector operation is logged for monitoring and debugging purposes.
