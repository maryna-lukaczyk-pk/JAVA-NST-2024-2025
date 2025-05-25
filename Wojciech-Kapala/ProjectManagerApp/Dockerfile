# Multi-stage build dla optymalizacji rozmiaru obrazu
FROM eclipse-temurin:21-jdk-alpine AS build

# Ustaw katalog roboczy
WORKDIR /app

# Skopiuj Maven wrapper i plik konfiguracyjny
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Pobierz zależności (dla lepszego cache'owania)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Skopiuj kod źródłowy
COPY src ./src

# Zbuduj aplikację (pomiń testy - uruchomimy je osobno)
RUN ./mvnw clean package -DskipTests -B

# Etap produkcyjny - użyj JRE zamiast JDK
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Zainstaluj narzędzia diagnostyczne (opcjonalnie)
RUN apk add --no-cache curl

# Skopiuj zbudowany JAR z etapu build
COPY --from=build /app/target/ProjectManagerApp-0.0.1-SNAPSHOT.jar app.jar

# Utwórz użytkownika bez uprawnień root (bezpieczeństwo)
RUN addgroup -g 1001 -S appuser && adduser -u 1001 -S appuser -G appuser
RUN chown -R appuser:appuser /app
USER appuser

# Expose port Spring Boot (domyślnie 8080)
EXPOSE 8080

# Ustaw zmienne środowiskowe JVM (dostosowane do Java 21)
ENV JAVA_OPTS="-Xmx1g -Xms512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Uruchom aplikację
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]