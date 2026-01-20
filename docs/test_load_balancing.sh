#!/bin/bash

# 🔄 Load Balancing Demo Script
# This script makes requests and shows which backend handles each one

echo "╔══════════════════════════════════════════════════════════════════════╗"
echo "║                                                                      ║"
echo "║              🔄 LOAD BALANCING DEMONSTRATION                         ║"
echo "║                                                                      ║"
echo "╚══════════════════════════════════════════════════════════════════════╝"
echo ""

# Check if containers are running
echo "📊 Checking container status..."
docker-compose ps | grep -E "backend|nginx"
echo ""

# Function to make a request and show timestamp
make_request() {
    local num=$1
    echo "═══════════════════════════════════════════════════════════════════════"
    echo "Request #$num - $(date '+%H:%M:%S')"
    echo "═══════════════════════════════════════════════════════════════════════"
    
    # Make the request
    curl -s http://localhost/api/v1/users > /dev/null
    
    # Give time for logs to appear
    sleep 0.5
    
    # Show the last NGINX log entry
    echo ""
    echo "NGINX Log:"
    docker-compose logs --tail=1 nginx 2>/dev/null | grep "GET /api"
    
    # Show recent backend logs (see which one responded)
    echo ""
    echo "Backend Logs (last 2 seconds):"
    docker-compose logs --since=2s backend-1 backend-2 backend-3 2>/dev/null | grep -E "backend-[123].*GET|Mapped.*users" | tail -3
    
    echo ""
    sleep 1
}

echo "🚀 Making 6 requests to demonstrate round-robin load balancing..."
echo "   Watch which backend handles each request!"
echo ""
sleep 2

# Make 6 requests
for i in {1..6}; do
    make_request $i
done

echo "╔══════════════════════════════════════════════════════════════════════╗"
echo "║                                                                      ║"
echo "║                     ✅ DEMONSTRATION COMPLETE                        ║"
echo "║                                                                      ║"
echo "╚══════════════════════════════════════════════════════════════════════╝"
echo ""
echo "📊 Summary:"
echo "   • Each request went through NGINX (port 80)"
echo "   • NGINX distributed them across backends 1, 2, 3"
echo "   • Pattern should be: 1 → 2 → 3 → 1 → 2 → 3 (round-robin)"
echo ""
echo "💡 To monitor in real-time:"
echo "   Terminal 1: docker-compose logs -f nginx"
echo "   Terminal 2: docker-compose logs -f backend-1 backend-2 backend-3"
echo "   Terminal 3: curl http://localhost/api/v1/users"
echo ""
