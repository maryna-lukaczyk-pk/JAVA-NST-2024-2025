FROM ubuntu:latest
LABEL authors="pingu"
RUN apt-get update && \
    apt-get install -y openjdk-17-jre && \
    apt-get clean
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]