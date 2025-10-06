# --- Build stage ---
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# Pré-télécharge les deps pour accélérer les builds suivants
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

# --- Runtime stage ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseContainerSupport"
COPY --from=build /app/target/*.jar app.jar
# EXPOSE non obligatoire (compose fait déjà la redirection), mais utile en local:
EXPOSE 9003
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
