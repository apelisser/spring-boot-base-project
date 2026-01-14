# Base Project - Spring Boot REST API

## Overview

This project serves as a **base template** for developing REST applications using **Spring Boot** and **spring-web**, already prepared for **containerized local execution** with Docker and NGINX.

The project is designed to simulate a **production-like setup**, including:

* multiple backend instances
* reverse proxy
* external context path
* centralized build and runtime control

---

## Implemented Features

### 🌐 Internationalization (i18n)

* Message internationalization structure is in place
* `en_US` is the default locale
* Easily extensible to other languages

### 🧵 Request Context

* Centralized request context handling
* A `requestId` is generated for every request
* The `requestId` is returned in the `x-request-id` response header
* Additional contextual data can be added if needed

### 📄 Logging

* Logback is configured with a custom pattern
* `requestId` is automatically included in all application logs
* Improves traceability across distributed requests

### ⚠️ Exception Handling

* Centralized exception handling structure
* Validation errors and common REST exceptions are customized
* Responses follow the **Problem Details (RFC 7807)** format
* Easy to extend with custom exception handlers

---

## Technology Stack

* **Java Development Kit**: 25
* **Spring Boot**: 4
* **Maven** (via Maven Wrapper)
* **Docker**
* **Docker Compose**
* **NGINX**
* **Git**

---

## Local Execution (Recommended)

> ⚠️ **Do not build or run the application manually with Maven or Docker commands.**
> The project provides a dedicated script to manage the entire lifecycle.

### Build Script

The `build.sh` script is the **single entry point** to:

* build the Docker image
* extract the application version from `pom.xml`
* manage Docker Compose
* start, stop, or restart the environment

To see all available options and usage instructions, run:

```sh
./build.sh --help
```

---

## Available Commands

### Build Docker Image Only (default)

```sh
./build.sh
```

or explicitly:

```sh
./build.sh --build
```

---

### Build Image and Start Containers

```sh
./build.sh --up
```

---

### Restart the Entire Environment

```sh
./build.sh --restart
```

This will:

1. Stop containers
2. Rebuild the image
3. Start the environment again

---

### Stop and Remove Containers

```sh
./build.sh --down
```

---

## What Gets Started

After running `./build.sh --up`, the following services will be available:

* **NGINX** (reverse proxy):

  * Port: `80`
* **2 backend application instances**:

  * Instance 1: internal port `8081`
  * Instance 2: internal port `8082`

All traffic is routed through NGINX.

---

## Application URLs

### Swagger UI

```text
http://localhost/base-app/swagger-ui/index.html
```

### OpenAPI JSON

```text
http://localhost/base-app/v3/api-docs
```

---

## API Endpoints (Examples)

### Get Application Information

* **Method**: `GET`
* **Endpoint**:

  ```
  http://localhost/base-app/api/v1/info
  ```
* **Response**:

  ```json
  {
    "name": "string",
    "description": "string",
    "appVersion": "string",
    "springBootVersion": "string",
    "javaVersion": "string"
  }
  ```

---

### Test Internationalization

* **Method**: `GET`
* **Endpoint**:

  ```
  http://localhost/base-app/api/v1/i18n/test
  ```
* **Header**:

  ```
  Accept-Language: en-US | pt-BR
  ```
* **Response (en-US)**:

  ```json
  {
    "message": "TEST (en-US)"
  }
  ```

---

### Error Handling Example

* **Method**: `GET`
* **Endpoint**:

  ```
  http://localhost/base-app/api/v1/unknown
  ```
* **Response**:

  ```json
  {
    "type": "about:blank",
    "title": "Resource not found",
    "status": 404,
    "detail": "Resource 'api/v1/unknown' does not exist.",
    "instance": "/base-app/api/v1/unknown",
    "timestamp": "2024-09-07T22:01:47.31595866Z",
    "requestId": "f5281b21-888e-4730-98e2-26ec71afcd61",
    "userMessage": "The accessed resource does not exist."
  }
  ```

---

## Architecture Notes

* The application **does not use a Spring context-path**
* `/base-app` is handled **exclusively by NGINX**
* NGINX removes `/base-app` before forwarding requests to the backend
* This setup mirrors real production environments using reverse proxies

---

## Final Notes

* Maven Wrapper (`./mvnw`) guarantees consistent Maven versions
* Docker image tags are automatically aligned with `pom.xml` version
* The environment is fully reproducible with a single command
