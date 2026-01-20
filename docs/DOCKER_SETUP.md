# 🐳 Docker Setup Guide

This guide explains how to run the backend and PostgreSQL using Docker.

---

## **Prerequisites**

- **Docker** (version 20.10+): [Install Docker](https://docs.docker.com/get-docker/)
- **Docker Compose** (version 2.0+): Usually included with Docker Desktop

Verify installation:
```bash
docker --version
docker compose version
```

---

## **Quick Start**

### **1. Start Everything**
```bash
# Build and start all services
docker compose up --build

# Or run in detached mode (background)
docker compose up --build -d
```

### **2. Access the Application**
- **Backend API**: http://localhost:9090
- **Swagger UI**: http://localhost:9090/swagger-ui.html
- **Health Check**: http://localhost:9090/actuator/health
- **PostgreSQL**: localhost:5432 (use any PostgreSQL client)

### **3. Stop Everything**
```bash
# Stop services (preserves data)
docker compose stop

# Stop and remove containers (preserves data volumes)
docker compose down

# Stop and remove everything including data
docker compose down -v
```

---

## **Available Commands**

### **Build & Start**
```bash
# Build images and start services
docker compose up --build

# Start in background (detached mode)
docker compose up -d

# Rebuild without cache
docker compose build --no-cache
docker compose up
```

### **View Logs**
```bash
# View all logs
docker compose logs

# Follow logs in real-time
docker compose logs -f

# View logs for specific service
docker compose logs backend
docker compose logs postgres

# Follow backend logs only
docker compose logs -f backend
```

### **Restart Services**
```bash
# Restart all services
docker compose restart

# Restart specific service
docker compose restart backend
docker compose restart postgres
```

### **Stop & Clean Up**
```bash
# Stop services (keeps containers)
docker compose stop

# Stop and remove containers (keeps volumes/data)
docker compose down

# Remove everything including volumes (⚠️ deletes database data)
docker compose down -v

# Remove unused images
docker image prune
```

### **Service Management**
```bash
# View running containers
docker compose ps

# View container resource usage
docker stats

# Execute commands in running container
docker compose exec backend sh
docker compose exec postgres psql -U postgres -d postdb
```

---

## **Configuration**

### **Environment Variables**

Edit `.env` file to customize configuration:

```bash
# Database
DB_NAME=postdb
DB_USERNAME=postgres
DB_PASSWORD=postgres123
DB_PORT=5432

# Backend
BACKEND_PORT=9090
```

### **Database Connection**

**From Host Machine:**
```
Host: localhost
Port: 5432
Database: postdb
Username: postgres
Password: postgres123
```

**From Backend Container:**
```
Host: postgres (Docker Compose service name)
Port: 5432
Database: postdb
```

---

## **Development Workflow**

### **Option 1: Full Docker Development**
1. Make code changes in `backend/src`
2. Rebuild and restart:
   ```bash
   docker compose up --build
   ```

### **Option 2: Hybrid (DB in Docker, Backend Local)**
1. Start only PostgreSQL:
   ```bash
   docker compose up postgres
   ```
2. Run backend locally:
   ```bash
   cd backend
   mvn spring-boot:run -Dspring-boot.run.profiles=prod
   ```
   Update `application-prod.properties` to use `localhost:5432`

### **Option 3: Watch Mode (Auto-Rebuild)**
```bash
# Use Docker Compose watch (Docker Compose v2.22+)
docker compose watch
```

---

## **Database Management**

### **Access PostgreSQL Shell**
```bash
docker compose exec postgres psql -U postgres -d postdb
```

### **Common PostgreSQL Commands**
```sql
-- List all tables
\dt

-- Describe table structure
\d users
\d posts
\d friendships

-- Query data
SELECT * FROM users;
SELECT * FROM posts LIMIT 10;

-- Exit
\q
```

### **Backup Database**
```bash
# Create backup
docker compose exec postgres pg_dump -U postgres postdb > backup.sql

# Restore backup
docker compose exec -T postgres psql -U postgres postdb < backup.sql
```

### **Reset Database**
```bash
# Remove volume and recreate
docker compose down -v
docker compose up -d
```

---

## **Troubleshooting**

### **Problem: Port Already in Use**
```
Error: Bind for 0.0.0.0:9090 failed: port is already allocated
```

**Solution:**
1. Change port in `.env`:
   ```bash
   BACKEND_PORT=9091
   ```
2. Or stop the service using the port:
   ```bash
   # Find process using port 9090
   lsof -i :9090
   kill -9 <PID>
   ```

### **Problem: Backend Can't Connect to Database**
```
Error: Connection refused to postgres:5432
```

**Solution:**
- Wait for PostgreSQL to be fully ready (health check takes ~30s)
- Check logs: `docker compose logs postgres`
- Verify network: `docker network ls`

### **Problem: Out of Disk Space**
```bash
# Check Docker disk usage
docker system df

# Clean up unused resources
docker system prune -a --volumes
```

### **Problem: Container Crashes on Startup**
```bash
# View logs
docker compose logs backend

# Check container status
docker compose ps

# Inspect container
docker inspect postdb_backend
```

---

## **Architecture Overview**

```
┌─────────────────────────────────┐
│   Host Machine (Your Computer) │
│                                 │
│  ┌──────────────────────────┐  │
│  │   Docker Engine          │  │
│  │                          │  │
│  │  ┌────────────────────┐  │  │
│  │  │  Backend Container │  │  │
│  │  │  (Spring Boot)     │  │  │
│  │  │  Port: 9090        │  │  │
│  │  └────────┬───────────┘  │  │
│  │           │              │  │
│  │           ▼              │  │
│  │  ┌────────────────────┐  │  │
│  │  │ PostgreSQL         │  │  │
│  │  │ Container          │  │  │
│  │  │ Port: 5432         │  │  │
│  │  │ Volume: postgres_  │  │  │
│  │  │         data       │  │  │
│  │  └────────────────────┘  │  │
│  │                          │  │
│  │  Network: backend-network│  │
│  └──────────────────────────┘  │
└─────────────────────────────────┘
```

---

## **Multi-Stage Build Benefits**

The `Dockerfile` uses multi-stage builds:

**Stage 1 (Build):**
- Uses Maven image with full JDK
- Compiles source code
- Creates JAR file

**Stage 2 (Runtime):**
- Uses lightweight JRE image (smaller)
- Only copies JAR file (no source code)
- Runs as non-root user (security)

**Benefits:**
- ✓ Smaller final image (~200MB vs ~600MB)
- ✓ Faster deployment
- ✓ More secure (no build tools in production)

---

## **Next Steps**

1. ✅ **Test the Setup**
   ```bash
   docker compose up --build
   ```

2. ✅ **Load Sample Data** (optional)
   ```bash
   # Use your existing data scripts
   bash user_creation.sh
   bash post_creation.sh
   bash create_friends.sh
   ```

3. ✅ **Update Frontend** (if needed)
   - Update API base URL to `http://localhost:9090`

4. ✅ **Ready for Load Balancing**
   - Once comfortable with Docker, we can add:
     - Multiple backend instances
     - NGINX load balancer
     - Redis for distributed caching

---

## **Useful Resources**

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
- [PostgreSQL Docker Hub](https://hub.docker.com/_/postgres)

---

**Need Help?**
- View logs: `docker compose logs -f`
- Check health: `docker compose ps`
- Restart: `docker compose restart`
