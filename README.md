# Pinecone Tutorial

This project demonstrates how to use the Pinecone Java client with Log4j2 for logging.

## Prerequisites

- Java 17 or higher
- Maven
- Pinecone API key

## Configuration

### Pinecone API Key

Set your Pinecone API key as an environment variable:

```shell
export PINECONE_API_KEY=your_api_key_here
```

### Logging

The service uses Log4j2 for logging. The configuration file is located at `src/main/resources/log4j2.xml`.

Logs are written to both the console and a file at `logs/app.log`.

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

Run with Maven:
```shell
java -cp target/pinecone-1.0-SNAPSHOT.jar net.taromurakami.pinecone.AppKt
```

Run with all dependencies included:
```shell
java -jar target/pinecone-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Dependency
```shell
mvn dependency:tree
```