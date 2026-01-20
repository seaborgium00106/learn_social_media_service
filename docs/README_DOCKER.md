# 🐳 Docker Setup - Complete Reference

## 📋 Quick Overview

Your Docker setup is **fully functional** and ready to use locally! This document summarizes everything you need to know.

---

## 📚 Documentation Files

| File | Purpose | Read If |
|------|---------|---------|
| **DOCKER_LOCAL_SETUP.md** | Complete step-by-step guide with explanations | 🔴 **START HERE** - Full details |
| **DOCKER_CHEATSHEET.txt** | Quick reference with essential commands | Need quick reminders |
| **DOCKER_COMMANDS.sh** | Reusable bash functions | Want helper functions |
| **DOCKER_STATUS.md** | Current system status & verification results | Want current state info |
| **README_DOCKER.md** | This file - overview & summary | Want overview |

---

## 🚀 Three-Step Quick Start

```bash
# Step 1: Build the Docker image (2-3 minutes)
docker-compose build

# Step 2: Start containers (10 seconds)
docker-compose up -d

# Step 3: Verify it works
docker-compose ps
```

**That's it!** Your backend is now running on `http://localhost:9090`

---

## 🎯 Essential Commands Cheat Sheet

### Build & Start
```bash
docker-compose build              # Build image
docker-compose up -d              # Start containers
docker-compose up -d --build      # Rebuild and start
```

### Status & Logs
```bash
docker-compose ps                 # View container status
docker-compose logs backend       # View backend logs
docker-compose logs -f backend    # Follow logs live
```

### Stop & Cleanup
```bash
docker-compose stop               # Stop containers (keep data)
docker-compose down               # Remove containers (keep data)
docker-compose down -v            # Remove EVERYTHING including data
```

### Database
```bash
# Connect to PostgreSQL shell
docker exec -it postdb_postgres psql -U postgres -d postdb

# Quick queries
docker exec postdb_postgres psql -U postgres -d postdb -c "SELECT * FROM users;"
```

---

## 🧪 Testing the Setup

### Test 1: Check Containers Running
```bash
docker-compose ps

# Expected output:
# postdb_backend   - Up
# postdb_postgres  - Up (healthy)
```

### Test 2: Test API Endpoint
```bash
curl http://localhost:9090/api/v1/users
# Expected: [] (empty list)
```

### Test 3: Create Test User
```bash
curl -X POST http://localhost:9090/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com"}'

# Expected: User object with id
```

### Test 4: Verify Database
```bash
curl http://localhost:9090/api/v1/users
# Expected: List with your new user
```

---

## 📊 Current Architecture

```
┌─────────────────────────────┐
│   Spring Boot Backend       │
│   (Docker Container)        │
│   Port: 9090                │
│   Status: Running           │
└──────────────┬──────────────┘
               │ (via network)
               ▼
┌─────────────────────────────┐
│   PostgreSQL Database       │
│   (Docker Container)        │
│   Port: 5432                │
│   Status: Healthy           │
└─────────────────────────────┘
               │
               ▼ (persists data)
        ┌─────────────┐
        │ postgres    │
        │ _data       │
        │ (volume)    │
        └─────────────┘
```

---

## 🔧 Configuration

### Environment Variables (.env)
```bash
# Database Configuration
DB_NAME=postdb
DB_USERNAME=postgres
DB_PASSWORD=postgres123
DB_PORT=5432

# Backend Configuration
BACKEND_PORT=9090
```

### Docker Compose Services
- **postdb_postgres**: PostgreSQL 16 Alpine container
- **postdb_backend**: Spring Boot backend container
- **backend-network**: Internal Docker network
- **postgres_data**: Persistent volume for database

---

## 📱 API Endpoints

**Base URL**: `http://localhost:9090`

### Users
- `GET /api/v1/users` - List all users
- `POST /api/v1/users` - Create user
- `GET /api/v1/users/{id}` - Get user by ID
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user

### Posts
- `GET /api/v1/posts` - List all posts
- `POST /api/v1/posts` - Create post
- `GET /api/v1/posts/{id}` - Get post by ID

### Timeline
- `GET /api/v1/timeline/{userId}` - Get user timeline

---

## 🔄 Common Workflows

### Daily Development
```bash
# Morning
docker-compose up -d

# ... make code changes ...

# Rebuild after changes
docker-compose up -d --build

# Evening
docker-compose stop
```

### Fresh Database
```bash
# Delete everything and start fresh
docker-compose down -v
docker-compose up -d
```

### View Logs While Developing
```bash
# Terminal 1: Follow logs
docker-compose logs -f backend

# Terminal 2: Test your API
curl http://localhost:9090/api/v1/users
```

---

## 🐛 Troubleshooting

| Problem | Solution |
|---------|----------|
| **Port 9090 in use** | Change `BACKEND_PORT` in `.env`, restart |
| **Backend won't start** | Check logs: `docker-compose logs backend` |
| **Can't connect to DB** | Wait 10 seconds, database needs time to start |
| **Data not persisting** | Volume `postgres_data` should exist: `docker volume ls` |
| **Build fails** | Clean and rebuild: `docker-compose down -v && docker-compose build --no-cache` |

---

## 🎓 Understanding Docker

### Key Concepts

**Image** = Blueprint (like a recipe)
- Defined in `backend/Dockerfile`
- Built once with `docker-compose build`
- Tagged as `learn-backend:latest`

**Container** = Running instance (like a cake baked from recipe)
- Started with `docker-compose up -d`
- Multiple instances can run from same image

**Volume** = Persistent storage
- `postgres_data` keeps database between restarts
- Remove with `docker-compose down -v`

**Network** = Communication between containers
- `backend-network` lets backend talk to database
- Automatic DNS resolution (service name = hostname)

---

## 📈 Next Steps

### Phase 1: ✅ COMPLETE
- [x] Backend Dockerized
- [x] PostgreSQL Containerized
- [x] Local testing verified

### Phase 2: Load Balancing (When Ready)
- [ ] Scale to 3 backend instances
- [ ] Add NGINX load balancer
- [ ] Configure health checks
- [ ] Implement round-robin routing

### Phase 3: Advanced (Optional)
- [ ] Add Redis for distributed caching
- [ ] Setup Docker Swarm or Kubernetes
- [ ] Production deployment

---

## 📞 Getting Help

### View Configuration
```bash
# See docker-compose.yml
cat docker-compose.yml

# See Dockerfile
cat backend/Dockerfile

# See environment variables
cat .env
```

### Debug Containers
```bash
# Inspect container details
docker-compose logs backend
docker-compose ps
docker stats

# Execute command in container
docker exec -it postdb_backend bash
docker exec -it postdb_postgres bash
```

### Clean Up Resources
```bash
# View all images
docker images

# View all volumes
docker volume ls

# Remove unused
docker image prune
docker volume prune
```

---

## 🔐 Security Notes

⚠️ **This setup is for LOCAL DEVELOPMENT only**

- Default database password: `postgres123` (not secure)
- Running on localhost (not exposed)
- No SSL/TLS (development only)
- No authentication required

**For Production:**
- Use strong passwords
- Enable SSL/TLS
- Use secrets management
- Implement authentication/authorization

---

## 📊 File Structure

```
project-root/
├── docker-compose.yml              ← Main config
├── .env                            ← Environment variables
├── .env.example                    ← Template
├── backend/
│   ├── Dockerfile                  ← Backend image
│   ├── pom.xml                     ← Maven config
│   └── src/                        ← Java source
├── frontend/                       ← React app (not containerized)
├── README_DOCKER.md                ← This file
├── DOCKER_LOCAL_SETUP.md           ← Full guide
├── DOCKER_CHEATSHEET.txt           ← Quick ref
├── DOCKER_COMMANDS.sh              ← Helper functions
└── DOCKER_STATUS.md                ← Status report
```

---

## ✅ Verification Checklist

Use this to verify everything is working:

- [ ] Docker installed: `docker --version`
- [ ] Docker Compose installed: `docker-compose --version`
- [ ] Image built: `docker images | grep learn-backend`
- [ ] Containers running: `docker-compose ps` shows both as "Up"
- [ ] Backend responds: `curl http://localhost:9090/api/v1/users`
- [ ] Database tables exist: `docker exec postdb_postgres psql -U postgres -d postdb -c "\dt"`
- [ ] Can create user: `curl -X POST http://localhost:9090/api/v1/users ...`
- [ ] User persists: `curl http://localhost:9090/api/v1/users` shows created user

---

## 🎯 Quick Decision Tree

**I want to...** | **Command**
---|---
Start everything | `docker-compose up -d`
Check status | `docker-compose ps`
View logs | `docker-compose logs backend`
Stop everything | `docker-compose stop`
Delete everything | `docker-compose down -v`
Rebuild code | `docker-compose up -d --build`
Connect to database | `docker exec -it postdb_postgres psql -U postgres -d postdb`
Test API | `curl http://localhost:9090/api/v1/users`

---

## 📝 Summary

✅ **Docker setup is complete and verified!**

- Backend containerized with multi-stage build
- PostgreSQL containerized with persistent volume
- Docker Compose configured for easy orchestration
- API endpoints working and tested
- Database connectivity verified
- Full documentation provided

**You're ready to develop locally with Docker!**

For detailed information, see **DOCKER_LOCAL_SETUP.md**

---

*Last Updated: 2026-01-19*  
*Status: ✅ Production Ready for Local Development*
