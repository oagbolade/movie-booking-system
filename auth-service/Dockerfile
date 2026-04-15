FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

COPY .. .

RUN mvn clean install -DskipTests

WORKDIR /app/auth-service

RUN mvn package -DskipTests

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=builder /app/auth-service/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]