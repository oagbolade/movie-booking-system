#!/bin/bash

set -e

SERVICE="eureka-server"

echo "Creating Eureka Server..."

mkdir -p $SERVICE
cd $SERVICE

mkdir -p src/main/java/com/platform/eureka
mkdir -p src/main/resources

touch pom.xml
touch Dockerfile

touch src/main/java/com/platform/eureka/EurekaServerApplication.java
touch src/main/resources/application.yml

echo "✅ Eureka Server structure created successfully!"