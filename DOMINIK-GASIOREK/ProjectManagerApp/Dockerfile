FROM openjdk:17-jdk-slim
ARG APP_JAR=ProjectManagerApp-0.0.1-SNAPSHOT.jar
COPY target/${APP_JAR} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
