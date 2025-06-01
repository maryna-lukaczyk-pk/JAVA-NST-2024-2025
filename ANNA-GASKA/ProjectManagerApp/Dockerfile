# 1) Use a specific JDK 21 version for better reproducibility
FROM eclipse-temurin:21-jdk-jammy

# 2) Set working directory in container
WORKDIR /app

# 3) Add a non-root user for security
RUN addgroup --system --gid 1001 appuser && \
    adduser --system --uid 1001 --gid 1001 appuser

# 4) Copy the built jar to the container
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# 5) Set proper permissions
RUN chown -R appuser:appuser /app
USER appuser

# 6) Expose port 8080
EXPOSE 8080

# 7) Set JVM options for containerized environment
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# 8) Start command
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
