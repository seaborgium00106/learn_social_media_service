# 🐳 Docker Local Setup Guide

Complete guide to build and run the application locally using Docker on your machine.

---

## 📋 Prerequisites

Before you start, make sure you have:

- **Docker Desktop** installed ([Download](https://www.docker.com/products/docker-desktop))
- **Docker Compose** (included with Docker Desktop)
- **Git** (for cloning/version control)
- `.env` file configured (see below)

### Check if Docker is installed:
```bash
docker --version
docker-compose --version
```

---

## 🔧 Configuration

### 1. Environment Variables (.env file)

Your `.env` file should have:

```bash
# Database Configuration
DB_NAME=postdb
DB_USERNAME=postgres
DB_PASSWORD=postgres123
DB_PORT=5432

# Backend Configuration
BACKEND_PORT=9090
```

**Location**: Root directory of the project (same level as `docker-compose.yml`)

If `.env` doesn't exist, copy from `.env.example`:
```bash
cp .env.example .env
```

---

## 🚀 Quick Start (5 Minutes)

### Step 1: Build Docker Image
```bash
docker-compose build
```

**What it does:**
- Reads `backend/Dockerfile`
- Downloads Maven base image
- Compiles your Spring Boot application
- Creates optimized runtime image

**Expected output:**
```
Step 1/15 : FROM maven:3.9.6-eclipse-temurin-17 AS build
...
Successfully built 8ef99680476a
Successfully tagged learn-backend:latest
```

### Step 2: Start Containers
```bash
docker-compose up -d
```

**What it does:**
- Starts PostgreSQL container (waits for it to be healthy)
- Starts Spring Boot backend container
- Creates persistent volume for database
- Creates internal network for communication

**Expected output:**
```
Network learn_backend-network Created
Volume learn_postgres_data Created
Container postdb_postgres Created
Container postdb_backend Created
Container postdb_postgres Started
Container postdb_postgres Healthy
Container postdb_backend Started
```

### Step 3: Verify Everything is Running
```bash
docker-compose ps
```

**Expected output:**
```
NAME              IMAGE                COMMAND                  SERVICE    STATUS
postdb_backend    learn-backend        "java -jar app.jar"      backend    Up (healthy)
postdb_postgres   postgres:16-alpine   "docker-entrypoint.s…"   postgres   Up (healthy)
```

### Step 4: Test the API
```bash
# Get all users (should return empty list)
curl http://localhost:9090/api/v1/users

# Create a test user
curl -X POST http://localhost:9090/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com"
  }'

# Get all users again (should show your new user)
curl http://localhost:9090/api/v1/users
```

---

## 🛑 Stopping and Managing Containers

### Stop all containers (keep volumes/data):
```bash
docker-compose stop
```

Containers are paused but not removed. Start again with:
```bash
docker-compose start
```

### Stop and remove containers (keep volumes/data):
```bash
docker-compose down
```

Containers and network are removed, but database volume persists. Start fresh with:
```bash
docker-compose up -d
```

### Stop, remove containers AND delete database volume (clean slate):
```bash
docker-compose down -v
```

⚠️ **WARNING**: This deletes all database data!

---

## 📊 Viewing Logs

### View all logs:
```bash
docker-compose logs
```

### View backend logs only:
```bash
docker-compose logs backend
```

### View database logs only:
```bash
docker-compose logs postgres
```

### View last 50 lines and follow (live):
```bash
docker-compose logs -f --tail=50 backend
```

Press `Ctrl+C` to stop following logs.

---

## 🔄 Rebuilding After Code Changes

### Scenario 1: Changed Java code (backend)
```bash
# Rebuild the image
docker-compose build

# Restart containers
docker-compose down
docker-compose up -d
```

Or in one command:
```bash
docker-compose up -d --build
```

### Scenario 2: Changed docker-compose.yml
```bash
docker-compose down
docker-compose up -d
```

### Scenario 3: Changed .env variables
```bash
docker-compose down
docker-compose up -d
```

---

## 🗄️ Database Access

### Connect to PostgreSQL directly:
```bash
docker exec -it postdb_postgres psql -U postgres -d postdb
```

You'll get a PostgreSQL prompt:
```
postdb=#
```

### Common PostgreSQL commands:

**List all tables:**
```sql
\dt
```

**Show table structure:**
```sql
\d users
```

**Select all users:**
```sql
SELECT * FROM users;
```

**Exit PostgreSQL:**
```
\q
```

### Example: Full database query
```bash
docker exec -it postdb_postgres psql -U postgres -d postdb -c "SELECT * FROM users;"
```

---

## 🐛 Troubleshooting

### Issue 1: Port 9090 already in use
```bash
# Change port in .env
BACKEND_PORT=9091

# Restart containers
docker-compose down
docker-compose up -d
```

### Issue 2: Port 5432 already in use
```bash
# Change port in .env
DB_PORT=5433

# Restart containers
docker-compose down
docker-compose up -d

# Update connection strings if needed
```

### Issue 3: Backend container exits immediately
```bash
# Check logs
docker-compose logs backend

# Common causes:
# - Database not ready (wait 10 seconds and restart)
# - Port already in use
# - Application error (check logs for details)
```

### Issue 4: Cannot connect to database
```bash
# Check if PostgreSQL is healthy
docker-compose ps

# View PostgreSQL logs
docker-compose logs postgres

# Connect to container directly
docker exec -it postdb_postgres bash

# Inside container, test connection:
pg_isready -U postgres
```

### Issue 5: Docker build fails
```bash
# Clean up and rebuild from scratch
docker-compose down -v
docker system prune -a
docker-compose build --no-cache
docker-compose up -d
```

---

## 📱 Testing the Application

### 1. Create a user:
```bash
curl -X POST http://localhost:9090/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com"
  }'
```

Response:
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "createdAt": "2026-01-19T20:15:30.123456"
}
```

### 2. Get all users:
```bash
curl http://localhost:9090/api/v1/users
```

### 3. Create a post:
```bash
curl -X POST http://localhost:9090/api/v1/posts \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "content": "Hello from Docker!"
  }'
```

### 4. Get all posts:
```bash
curl http://localhost:9090/api/v1/posts
```

### 5. View timeline:
```bash
curl http://localhost:9090/api/v1/timeline/1
```

---

## 📊 Docker File Structure

```
project-root/
├── docker-compose.yml          ← Orchestration config
├── .env                        ← Environment variables
├── .env.example                ← Template for .env
├── backend/
│   ├── Dockerfile              ← Backend image definition
│   ├── pom.xml                 ← Maven dependencies
│   └── src/                    ← Java source code
└── DOCKER_LOCAL_SETUP.md       ← This file
```

---

## 🎯 Common Workflows

### Workflow 1: Daily Development
```bash
# Start of day
docker-compose up -d

# Make code changes...

# Rebuild and restart
docker-compose up -d --build

# End of day
docker-compose stop
```

### Workflow 2: Fresh Start (Clean Database)
```bash
# Remove everything including data
docker-compose down -v

# Start fresh
docker-compose up -d
```

### Workflow 3: Debugging
```bash
# View logs in real-time
docker-compose logs -f backend

# In another terminal, test API
curl http://localhost:9090/api/v1/users

# Access database
docker exec -it postdb_postgres psql -U postgres -d postdb
```

### Workflow 4: Backup Database
```bash
# Dump database to file
docker exec postdb_postgres pg_dump -U postgres -d postdb > backup.sql

# Restore from backup
docker exec -i postdb_postgres psql -U postgres -d postdb < backup.sql
```

---

## 🔐 Security Notes

⚠️ **For Development Only!**

- Default credentials: `postgres:postgres123`
- Running on localhost (not exposed to internet)
- Database not encrypted
- No SSL/TLS in development setup

**For production:**
- Use strong passwords
- Enable SSL/TLS
- Use secrets management
- Enable authentication/authorization

---

## 📈 Performance Tips

### Tip 1: Use named volumes
```bash
# Persist data across containers
docker volume ls
```

### Tip 2: Check resource usage
```bash
docker stats
```

### Tip 3: Prune unused images/volumes
```bash
# Remove unused images
docker image prune

# Remove unused volumes
docker volume prune

# Full cleanup
docker system prune -a
```

### Tip 4: Build caching
```bash
# Use build cache (faster rebuilds)
docker-compose build

# Skip cache if needed
docker-compose build --no-cache
```

---

## 🆘 Getting Help

### View Docker version info:
```bash
docker version
```

### View Docker system info:
```bash
docker info
```

### View running processes in container:
```bash
docker-compose top backend
```

### Execute command in running container:
```bash
docker exec -it postdb_backend java -version
```

### View container events:
```bash
docker-compose events
```

---

## 📝 Quick Reference Card

| Task | Command |
|------|---------|
| Build image | `docker-compose build` |
| Start containers | `docker-compose up -d` |
| Stop containers | `docker-compose stop` |
| Remove containers | `docker-compose down` |
| Remove everything | `docker-compose down -v` |
| View status | `docker-compose ps` |
| View logs | `docker-compose logs backend` |
| Rebuild & restart | `docker-compose up -d --build` |
| Connect to DB | `docker exec -it postdb_postgres psql -U postgres -d postdb` |
| Test API | `curl http://localhost:9090/api/v1/users` |

---

## 🎓 Docker Concepts

### Image vs Container
- **Image**: Blueprint (like a recipe)
- **Container**: Running instance (like a baked cake)

### Volumes
- Persistent storage that survives container restart
- Data stored in `postgres_data` volume

### Networks
- Internal communication between containers
- `backend-network` allows backend → postgres communication

### Health Checks
- Monitors container health
- Docker waits for health before starting dependent containers

---

## 🔗 Useful Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [PostgreSQL Docker Hub](https://hub.docker.com/_/postgres)
- [OpenJDK Docker Hub](https://hub.docker.com/_/openjdk)

---

**Last Updated**: 2026-01-19  
**Status**: ✅ Verified and working
