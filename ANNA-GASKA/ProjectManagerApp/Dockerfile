# 1) bazowy obraz JDK 21
FROM eclipse-temurin:21-jdk-jammy

# 2) katalog roboczy w kontenerze
WORKDIR /app

# 3) kopiujemy zbudowany jar do kontenera
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# 4) otwieramy port 8080
EXPOSE 8080

# 5) komenda startowa
ENTRYPOINT ["java", "-jar", "app.jar"]
