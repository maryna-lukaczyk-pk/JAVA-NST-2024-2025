# Wybieramy wersję Javy zgodną z moim projektem
FROM openjdk:21-jdk-slim

# Ustawiamy argument dla nazwy pliku JAR
ARG JAR_FILE=target/*.jar

# Kopiujemy zbudowany plik JAR do kontenera
COPY ${JAR_FILE} app.jar

# Ustawiamy port, na którym aplikacja będzie nasłuchiwać (standardowo 8080 dla Spring Boot)
EXPOSE 8080

# Komenda uruchamiająca aplikację po starcie kontenera
ENTRYPOINT ["java", "-jar", "/app.jar"]