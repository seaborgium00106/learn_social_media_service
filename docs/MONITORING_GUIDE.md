# 📊 Monitoring NGINX & Backend Load Balancing

## 🎯 Goal
See which backend instance handles each request in real-time.

---

## 🔍 Quick Commands

### View NGINX Logs (See Routing)
```bash
docker-compose logs -f nginx
```
**What you'll see:**
- Every incoming request
- Which backend it was routed to
- Response status (200, 404, 500, etc.)
- Timestamp

### View Backend Logs
```bash
# Watch all backends
docker-compose logs -f backend-1 backend-2 backend-3

# Watch specific backend
docker-compose logs -f backend-1
```
**What you'll see:**
- Request processing
- Database queries
- Application logs
- Errors/warnings

### View Last N Lines
```bash
# Last 50 lines
docker-compose logs --tail=50 nginx

# Last 100 lines from all backends
docker-compose logs --tail=100 backend-1 backend-2 backend-3
```

### Filter Logs by Time
```bash
# Logs from last 5 minutes
docker-compose logs --since=5m nginx

# Logs from specific time
docker-compose logs --since="2024-01-19T14:00:00" nginx
```

---

## 🧪 Practical Demo

### Setup 1: Single Terminal (Quick Check)
```bash
# Make a request and see the last log entry
curl http://localhost/api/v1/users
docker-compose logs --tail=5 nginx | grep "GET /api"
```

### Setup 2: Two Terminals (Watch Live)
**Terminal 1:** Watch NGINX
```bash
docker-compose logs -f nginx
```

**Terminal 2:** Make requests
```bash
curl http://localhost/api/v1/users
curl http://localhost/api/v1/users
curl http://localhost/api/v1/users
```

Watch Terminal 1 to see round-robin in action!

### Setup 3: Three Terminals (Full Monitoring)
**Terminal 1:** NGINX logs
```bash
docker-compose logs -f nginx
```

**Terminal 2:** All backend logs
```bash
docker-compose logs -f backend-1 backend-2 backend-3
```

**Terminal 3:** Make requests
```bash
# Single request
curl http://localhost/api/v1/users

# Multiple requests
for i in {1..10}; do curl http://localhost/api/v1/users; sleep 1; done
```

---

## 🎬 Automated Demo Script

We created a script that demonstrates load balancing:

```bash
./test_load_balancing.sh
```

**What it does:**
1. Checks container status
2. Makes 6 requests sequentially
3. Shows which backend handled each request
4. Demonstrates round-robin pattern

---

## 📋 Understanding NGINX Logs

### Log Format
```
172.20.0.1 - - [19/Jan/2026:14:30:45 +0000] "GET /api/v1/users HTTP/1.1" 200 156 "-" "curl/7.88.1"
```

**Breaking it down:**
- `172.20.0.1` - Client IP (Docker internal)
- `[19/Jan/2026:14:30:45 +0000]` - Timestamp
- `"GET /api/v1/users HTTP/1.1"` - Request method and URL
- `200` - HTTP status code
- `156` - Response size in bytes
- `"curl/7.88.1"` - User agent

### Key Things to Look For
- **Status codes:**
  - `200` - Success ✅
  - `404` - Not found
  - `500` - Server error ❌
  - `502` - Bad gateway (backend down) ❌
  - `503` - Service unavailable ❌

- **Response time:** How long request took
- **Patterns:** Are requests distributed evenly?

---

## 📋 Understanding Backend Logs

### Spring Boot Log Format
```
2026-01-19 14:30:45.123  INFO 1 --- [nio-9090-exec-1] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Initializing...
2026-01-19 14:30:45.456  INFO 1 --- [nio-9090-exec-1] c.e.h.controller.UserController          : GET /api/v1/users
```

**Key Elements:**
- Timestamp
- Log level (INFO, WARN, ERROR)
- Thread name (`nio-9090-exec-1`)
- Logger name (class path)
- Message

### What to Monitor
- **Requests:** See when backend receives requests
- **Database queries:** SQL statements executed
- **Errors:** Exceptions, stack traces
- **Performance:** Response times

---

## 🔍 Advanced Monitoring Commands

### Grep for Specific Patterns
```bash
# Only GET requests
docker-compose logs nginx | grep "GET"

# Only errors
docker-compose logs backend-1 | grep "ERROR"

# Only status 200
docker-compose logs nginx | grep " 200 "

# Specific endpoint
docker-compose logs nginx | grep "/api/v1/users"
```

### Count Requests Per Backend
```bash
# Count how many times each backend was used
docker-compose logs nginx | grep "backend-1" | wc -l
docker-compose logs nginx | grep "backend-2" | wc -l
docker-compose logs nginx | grep "backend-3" | wc -l
```

### Watch Specific Backend Health
```bash
# Monitor health check endpoint
docker-compose logs -f backend-1 | grep "actuator/health"
```

---

## 🎯 Common Monitoring Scenarios

### Scenario 1: Testing Load Distribution
**Goal:** Verify requests are distributed evenly

```bash
# Terminal 1: Watch NGINX
docker-compose logs -f nginx

# Terminal 2: Make 9 requests
for i in {1..9}; do 
  echo "Request $i"
  curl -s http://localhost/api/v1/users > /dev/null
  sleep 0.5
done
```

**Expected:** 3 requests per backend (round-robin)

### Scenario 2: Testing Failover
**Goal:** See what happens when a backend goes down

```bash
# Terminal 1: Watch NGINX
docker-compose logs -f nginx

# Terminal 2: Stop backend-1
docker-compose stop backend-1

# Terminal 3: Make requests
for i in {1..6}; do 
  curl http://localhost/api/v1/users
  sleep 1
done
```

**Expected:** Requests only go to backend-2 and backend-3

### Scenario 3: Debugging Errors
**Goal:** Find why requests are failing

```bash
# Check NGINX for errors
docker-compose logs nginx | grep -E "50[0-9]|40[0-9]"

# Check backends for exceptions
docker-compose logs backend-1 backend-2 backend-3 | grep -E "ERROR|Exception"

# Check database connectivity
docker-compose logs postgres | grep -E "ERROR|FATAL"
```

---

## 📊 Real-Time Monitoring Dashboard (Terminal)

Want a live view? Run these in separate terminals:

### Terminal 1: NGINX Access Log
```bash
docker-compose logs -f nginx | grep --line-buffered "GET /api"
```

### Terminal 2: Backend Activity
```bash
docker-compose logs -f backend-1 backend-2 backend-3 | grep --line-buffered "GET"
```

### Terminal 3: Error Monitor
```bash
docker-compose logs -f nginx backend-1 backend-2 backend-3 | grep --line-buffered -E "ERROR|WARN|50[0-9]"
```

### Terminal 4: Request Generator
```bash
watch -n 2 'curl -s http://localhost/api/v1/users > /dev/null && echo "Request sent at $(date)"'
```

---

## 🛠️ Troubleshooting with Logs

### Issue: No logs appearing
```bash
# Check if containers are running
docker-compose ps

# Check if services are healthy
docker-compose ps | grep healthy
```

### Issue: Can't see which backend handled request
```bash
# NGINX logs should show upstream server
# Look for lines like: "upstream: http://backend-1:9090"
docker-compose logs nginx | grep upstream
```

### Issue: Logs are too verbose
```bash
# Filter for specific log level
docker-compose logs backend-1 | grep "INFO"

# Only errors
docker-compose logs backend-1 | grep "ERROR"
```

---

## 📝 Log Management Tips

### Save Logs to File
```bash
# Save NGINX logs
docker-compose logs nginx > nginx_logs.txt

# Save all logs
docker-compose logs > all_logs.txt

# Save with timestamp
docker-compose logs nginx > nginx_logs_$(date +%Y%m%d_%H%M%S).txt
```

### Clear Old Logs (Start Fresh)
```bash
# Restart containers (clears in-memory logs)
docker-compose restart nginx

# Or rebuild
docker-compose down
docker-compose up -d
```

### Limit Log Size
In `docker-compose.yml`, add:
```yaml
services:
  backend-1:
    logging:
      driver: "json-file"
      options:
        max-size: "10m"
        max-file: "3"
```

---

## 🎯 Quick Reference Card

| Task | Command |
|------|---------|
| Watch NGINX live | `docker-compose logs -f nginx` |
| Watch all backends | `docker-compose logs -f backend-1 backend-2 backend-3` |
| Last 50 lines | `docker-compose logs --tail=50 nginx` |
| Last 5 minutes | `docker-compose logs --since=5m nginx` |
| Filter errors | `docker-compose logs nginx | grep ERROR` |
| Count requests | `docker-compose logs nginx | grep "GET" | wc -l` |
| Run demo | `./test_load_balancing.sh` |

---

## 📚 Summary

**To see load balancing in action:**
1. Open terminal: `docker-compose logs -f nginx`
2. Open another: `curl http://localhost/api/v1/users` (repeat)
3. Watch first terminal to see which backend handles each request

**To debug issues:**
1. Check NGINX logs: `docker-compose logs nginx`
2. Check backend logs: `docker-compose logs backend-1`
3. Look for ERROR/WARN messages

**To run automated demo:**
```bash
./test_load_balancing.sh
```

---

**Happy monitoring!** 🚀
