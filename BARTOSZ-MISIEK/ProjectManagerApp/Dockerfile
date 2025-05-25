FROM amazoncorretto:23-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY .env .env

ENTRYPOINT ["java","-jar","/app.jar"]