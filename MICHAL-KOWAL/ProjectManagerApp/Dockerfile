FROM openjdk:21
WORKDIR /pm-app
COPY target/ProjectManagerApp-0.0.1-SNAPSHOT.jar pm-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "pm-app.jar"]
