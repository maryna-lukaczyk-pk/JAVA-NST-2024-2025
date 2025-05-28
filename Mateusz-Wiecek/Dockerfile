# --- Etap 1: budowanie aplikacji z Mavenem ---
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app

# kopiujemy pom.xml i źródła
COPY pom.xml .
COPY src ./src

# budujemy JAR-a, pomijając testy
RUN mvn clean package -DskipTests

# --- Etap 2: kontener z JRE ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# kopiujemy gotowy JAR z poprzedniego etapu
COPY --from=build /app/target/ProjectManagerApp-0.0.1-SNAPSHOT.jar app.jar

# otwieramy port 8080
EXPOSE 8080

# domyślne polecenie startujące aplikację
ENTRYPOINT ["java","-jar","app.jar"]
