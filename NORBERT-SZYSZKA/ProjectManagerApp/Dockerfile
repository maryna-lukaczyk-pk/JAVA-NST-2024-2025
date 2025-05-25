FROM openjdk:8-jdk-alpine
COPY pom.xml .
COPY src ./src
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ProjectManagerApp-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/ProjectManagerApp-0.0.1-SNAPSHOT.jar.jar"]