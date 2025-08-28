FROM maven:3.8-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY --from=builder /app/${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]