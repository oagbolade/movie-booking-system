FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests -pl notification-service

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=builder /app/notification-service/target/*.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]