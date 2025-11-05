# --- STAGE 1: Build ---
# Questa parte rimane invariata e funziona
FROM maven:4.0.0-rc-4-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

# --- STAGE 2: Run ---
# IMMAGINE CORRETTA: Usiamo eclipse-temurin per il JRE
FROM eclipse-temurin:21-jre-alpine

# Espone la porta
EXPOSE 8080

# Copia il .jar dallo stage di build
COPY --from=build /app/target/*.jar app.jar

# Comando di avvio
ENTRYPOINT ["java", "-jar", "/app.jar"]