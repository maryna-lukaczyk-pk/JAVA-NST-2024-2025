FROM maven:3.9-eclipse-temurin-23 AS build
WORKDIR /app

COPY . .
RUN mvn clean package -DskipTests

RUN find /app/target -name "*.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar" -not -name "*-tests.jar" -exec cp {} /app/application.jar \;

FROM eclipse-temurin:23-jre

WORKDIR /app
COPY --from=build /app/application.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]