#!/bin/bash

set -e

echo "======================================"
echo "🚀 Starting Movie Ticket System"
echo "======================================"

# Move to project root (script-safe)
cd "$(dirname "$0")/.."

echo ""
echo "🔧 Building and starting all services..."
echo ""

docker-compose up --build -d

echo ""
echo "⏳ Waiting for infrastructure services to stabilize..."
sleep 15

echo ""
echo "🔍 Checking running containers..."
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

echo ""
echo "======================================"
echo "✅ SYSTEM STARTED SUCCESSFULLY"
echo "======================================"

echo ""
echo "📌 Core Services URLs:"
echo "--------------------------------------"
echo "API Gateway:        http://localhost:8080"
echo "Eureka Server:      http://localhost:8761"
echo ""
echo "Auth Service:       http://localhost:8081"
#echo "Catalogue Service:  http://localhost:8082"
#echo "Ticket Service:     http://localhost:8083"
#echo "Payment Service:    http://localhost:8084"
echo "Notification Svc:   http://localhost:8085"
echo ""
echo "--------------------------------------"
echo "📦 Infrastructure:"
echo "Kafka:              localhost:9092"
echo "MongoDB:            localhost:27017"
echo "Redis:              localhost:6379"
echo "======================================"

echo ""
echo "🧠 TIP:"
echo "If services fail to register, check Eureka:"
echo "http://localhost:8761"
echo "======================================"