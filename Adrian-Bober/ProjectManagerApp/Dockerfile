# --------------------------------------------
# Etap 1: Budowanie aplikacji (Maven + JDK)
# --------------------------------------------
FROM maven:3.8.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# --------------------------------------------
# Etap 2: Uruchomienie aplikacji (tylko JRE)
# --------------------------------------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]