#!/bin/bash
# Simple script to test Docker setup

echo "Building the application..."
cd "$(dirname "$0")"
./mvnw clean package -DskipTests

echo "Building Docker image and starting containers..."
docker-compose down
docker-compose up --build -d

echo "Waiting for application to start..."
sleep 30

echo "Testing application health..."
curl -s http://localhost:8080/actuator/health | grep -q "UP" && echo "Application is healthy!" || echo "Application health check failed!"

echo "Testing application info..."
curl -s http://localhost:8080/actuator/info

echo "To stop the containers, run: docker-compose down"