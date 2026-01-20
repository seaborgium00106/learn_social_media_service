# 🚀 NGINX Load Balancer - Quick Start

## ✅ Status: DEPLOYED AND WORKING

---

## 🎯 What You Have Now

```
Frontend → NGINX (port 80) → 3 Backend Instances → PostgreSQL
```

**Benefits:**
- ✅ 3× capacity
- ✅ High availability
- ✅ Automatic failover
- ✅ Load balancing

---

## 🚀 Quick Commands

### Start Everything
```bash
docker-compose up -d
```

### Check Status
```bash
docker-compose ps
```

Expected output: 5 services (nginx + 3 backends + postgres)

### Test API
```bash
curl http://localhost/api/v1/users
```

### View Logs
```bash
# NGINX logs (see load balancing)
docker-compose logs nginx

# Backend logs
docker-compose logs backend-1
docker-compose logs backend-2
docker-compose logs backend-3
```

### Stop Everything
```bash
docker-compose down
```

---

## 📊 Service Ports

| Service | Port | Access |
|---------|------|--------|
| **NGINX** | 80 | `http://localhost` |
| Backend 1 | 9091 | `http://localhost:9091` |
| Backend 2 | 9092 | `http://localhost:9092` |
| Backend 3 | 9093 | `http://localhost:9093` |
| PostgreSQL | 5432 | `localhost:5432` |

---

## 🧪 Testing Load Balancing

### Create Multiple Users
```bash
for i in {1..5}; do
  curl -X POST http://localhost/api/v1/users \
    -H "Content-Type: application/json" \
    -d "{\"username\":\"user$i\",\"email\":\"user$i@example.com\"}"
  echo ""
done
```

Each request will be handled by a different backend (round-robin).

### Watch NGINX Distribute Requests
```bash
# Terminal 1: Watch NGINX logs
docker-compose logs -f nginx

# Terminal 2: Make requests
curl http://localhost/api/v1/users
```

---

## 🔄 How Load Balancing Works

```
Request 1 → NGINX → Backend 1 (9091)
Request 2 → NGINX → Backend 2 (9092)
Request 3 → NGINX → Backend 3 (9093)
Request 4 → NGINX → Backend 1 (9091)  ← Cycles back
```

All backends share the same PostgreSQL database.

---

## 🛠️ Troubleshooting

### Issue: Port 80 in use
**Solution:** Change NGINX port
```yaml
# In docker-compose.yml
nginx:
  ports:
    - "8080:80"  # Change to 8080
```

### Issue: Backend not responding
**Solution:** Restart specific backend
```bash
docker-compose restart backend-1
```

### Issue: Need to rebuild after code changes
**Solution:**
```bash
docker-compose down
docker-compose build
docker-compose up -d
```

---

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| **NGINX_QUICK_START.md** | This file - quick reference |
| **NGINX_SETUP_COMPLETE.md** | Complete setup documentation |
| **NGINX_LOAD_BALANCER_PLAN.md** | Implementation plan |
| **docker-compose.yml** | Service configuration |
| **nginx/nginx.conf** | NGINX configuration |

---

## 🎯 Key Features

✅ **Load Balancing**: Round-robin across 3 backends  
✅ **Health Checks**: Every 10 seconds on `/actuator/health`  
✅ **Automatic Failover**: Failed backends removed automatically  
✅ **Shared Database**: All backends use PostgreSQL  
✅ **Easy Scaling**: Add more backends anytime  

---

## 🔜 Next Steps

1. **Test the setup**: Make some API calls
2. **Try failover**: Stop one backend, see requests still work
3. **Scale up**: Add backend-4 if needed
4. **Add Redis**: For distributed caching (optional)
5. **Deploy to cloud**: AWS/GCP/Azure ready

---

## 📞 Quick Help

```bash
# Start
docker-compose up -d

# Status
docker-compose ps

# Logs
docker-compose logs nginx

# Test
curl http://localhost/api/v1/users

# Stop
docker-compose down
```

---

**You're ready to go!** 🚀

For detailed info, see `NGINX_SETUP_COMPLETE.md`
