FROM eclipse-temurin:21-jdk-alpine
ARG JAR_FILE=target/ProjectManagerApp-0.0.1-SNAPSHOT.jar
WORKDIR /app
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
