FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests -pl eureka-server

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=builder /app/eureka-server/target/*.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]