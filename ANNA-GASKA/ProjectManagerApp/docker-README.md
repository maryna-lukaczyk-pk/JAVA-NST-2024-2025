# Docker Setup for Project Manager App

This document provides instructions for running the Project Manager application using Docker.

## Prerequisites

- Docker and Docker Compose installed on your system
- Java 21 (for building the application locally)

## Docker Files

The project includes the following Docker-related files:

1. `Dockerfile` - Defines how to build the application image
2. `docker-compose.yml` - Defines the services (app and database)
3. `docker-test.ps1` - PowerShell script to test the Docker setup (Windows)
4. `docker-test.sh` - Bash script to test the Docker setup (Linux/Mac)

## Running with Docker

### Option 1: Using Docker Compose (Recommended)

1. Build and start the containers:
   ```
   docker-compose up --build
   ```

2. To run in detached mode (background):
   ```
   docker-compose up --build -d
   ```

3. To stop the containers:
   ```
   docker-compose down
   ```

### Option 2: Using the Test Script

1. For Windows, run:
   ```
   .\docker-test.ps1
   ```

2. For Linux/Mac, run:
   ```
   chmod +x docker-test.sh
   ./docker-test.sh
   ```

## Configuration

The Docker setup uses the following configuration:

- PostgreSQL database running on port 5432
- Spring Boot application running on port 8080
- Environment variables for database connection are set in docker-compose.yml
- JVM options are optimized for containerized environments

## Accessing the Application

- Application: http://localhost:8080
- API Documentation: http://localhost:8080/swagger-ui.html
- Health Check: http://localhost:8080/actuator/health

## Troubleshooting

1. If the application fails to start, check the logs:
   ```
   docker-compose logs app
   ```

2. If the database fails to start, check the logs:
   ```
   docker-compose logs db
   ```

3. To rebuild the containers from scratch:
   ```
   docker-compose down
   docker-compose build --no-cache
   docker-compose up
   ```

## Security Notes

- The Docker configuration uses a non-root user for the application container
- Database credentials are set in docker-compose.yml and should be changed for production
- Volume data is persisted between container restarts