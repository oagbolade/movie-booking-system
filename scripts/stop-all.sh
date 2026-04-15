#!/bin/bash

set -e

echo "======================================"
echo "🛑 Stopping Movie Ticket System"
echo "======================================"

cd "$(dirname "$0")/.."

echo ""
echo "🔍 Checking running containers..."
RUNNING=$(docker ps -q)

if [ -z "$RUNNING" ]; then
  echo "⚠️ No running containers found."
  echo "System is already stopped."
  exit 0
fi

echo ""
echo "📦 Currently running services:"
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

echo ""
echo "⏳ Stopping all services gracefully..."
docker-compose down

echo ""
echo "⏳ Waiting for cleanup..."
sleep 5

echo ""
echo "======================================"
echo "✅ ALL SERVICES STOPPED SUCCESSFULLY"
echo "======================================"

echo ""
echo "🧠 OPTIONAL CLEANUP COMMANDS:"
echo "--------------------------------------"
echo "Remove volumes (MongoDB data reset):"
echo "  docker-compose down -v"
echo ""
echo "Remove unused images:"
echo "  docker system prune -a"
echo ""
echo "Remove Kafka/Zookeeper volumes (if added later):"
echo "  docker volume prune"
echo "======================================"