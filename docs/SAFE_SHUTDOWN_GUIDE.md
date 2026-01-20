# 🛑 Safe Shutdown & Cleanup Guide

## ✅ Recommended Safe Shutdown Procedure

### **Option 1: Stop and Keep Data (Recommended)**
```bash
# Stop all containers but keep data
docker-compose down
```
**What it does:**
- ✓ Stops all containers
- ✓ Removes containers
- ✓ Removes networks
- ✓ **KEEPS** database volume (postgres_data)
- ✓ **KEEPS** all your data

**When to use:** Daily development - stop at end of day, start fresh tomorrow

---

### **Option 2: Stop Without Removing Containers**
```bash
# Just pause containers
docker-compose stop
```
**What it does:**
- ✓ Stops all containers
- ✓ Keeps containers (don't need to recreate)
- ✓ **KEEPS** all data
- Fast restart with `docker-compose start`

**When to use:** Short breaks - lunch, meetings, etc.

---

### **Option 3: Full Cleanup (Delete Everything)**
```bash
# Remove everything including data
docker-compose down -v
```
**What it does:**
- ✓ Stops all containers
- ✓ Removes containers
- ✓ Removes networks
- ✗ **DELETES** database volume
- ✗ **DELETES** all your data

**When to use:** Fresh start, testing, or when you want to delete all data

---

### **Option 4: Nuclear Option (Clean System)**
```bash
# Stop everything
docker-compose down -v

# Remove all unused Docker resources
docker system prune -a -f

# Remove all volumes
docker volume prune -f
```
**What it does:**
- ✗ **DELETES EVERYTHING**
- Removes all stopped containers
- Removes all unused images
- Removes all unused volumes
- Frees up maximum disk space

**When to use:** Docker is messed up, need to start completely fresh

---

## 📋 Step-by-Step Safe Shutdown

### Daily Workflow (Keep Data)
```bash
# 1. Stop all services
docker-compose down

# 2. Verify everything stopped
docker-compose ps

# 3. (Optional) Check disk space saved
docker system df
```

### Complete Cleanup (Delete Data)
```bash
# 1. Stop and remove everything
docker-compose down -v

# 2. Clean up unused resources
docker system prune -f

# 3. Verify cleanup
docker ps -a
docker volume ls
docker network ls
```

---

## 🔍 Verification Commands

### Check Running Containers
```bash
docker-compose ps
# or
docker ps
```

### Check All Containers (including stopped)
```bash
docker ps -a
```

### Check Volumes (database data)
```bash
docker volume ls
```

### Check Networks
```bash
docker network ls
```

### Check Disk Usage
```bash
docker system df
```

---

## ⚠️ Common Issues & Solutions

### Issue 1: "Resource is still in use" when removing network
**Cause:** Old container still attached to network

**Solution:**
```bash
# Stop all containers
docker stop $(docker ps -q)

# Remove old containers
docker container prune -f

# Try again
docker-compose down
```

### Issue 2: Old containers still running after `docker-compose down`
**Cause:** Containers created outside of current docker-compose.yml

**Solution:**
```bash
# List all containers
docker ps -a | grep postdb

# Stop specific container
docker stop postdb_backend

# Remove specific container
docker rm postdb_backend

# Or remove all stopped containers
docker container prune -f
```

### Issue 3: Port still in use after shutdown
**Cause:** Container still running or another process using port

**Solution:**
```bash
# Check what's using the port (macOS/Linux)
lsof -i :9090
lsof -i :80

# Find and stop the container
docker ps
docker stop <container_id>

# Or kill the process
kill -9 <PID>
```

### Issue 4: Can't remove volume (in use)
**Cause:** Container still using the volume

**Solution:**
```bash
# Stop all containers first
docker-compose down

# Then remove volume
docker volume rm learn_postgres_data

# Or force remove all volumes
docker volume prune -f
```

---

## 🎯 Quick Reference

| Goal | Command | Keeps Data? |
|------|---------|-------------|
| Stop for the day | `docker-compose down` | ✅ Yes |
| Quick pause | `docker-compose stop` | ✅ Yes |
| Fresh start | `docker-compose down -v` | ❌ No |
| Clean everything | `docker system prune -a -f` | ❌ No |

---

## 📊 What Gets Deleted/Kept

### `docker-compose down`
- ✗ Containers (removed)
- ✗ Networks (removed)
- ✅ Volumes (kept)
- ✅ Images (kept)
- ✅ **Your data (kept)**

### `docker-compose down -v`
- ✗ Containers (removed)
- ✗ Networks (removed)
- ✗ Volumes (removed)
- ✅ Images (kept)
- ✗ **Your data (deleted)**

### `docker system prune -a -f`
- ✗ Stopped containers (removed)
- ✗ Unused networks (removed)
- ✗ Dangling images (removed)
- ✗ Build cache (removed)
- ✅ Running containers (kept)
- ✅ Volumes (kept by default)

---

## 🔄 Restart After Shutdown

### After `docker-compose stop`
```bash
docker-compose start
```
Fast restart (containers already exist)

### After `docker-compose down`
```bash
docker-compose up -d
```
Recreates containers, keeps data

### After `docker-compose down -v`
```bash
docker-compose up -d
```
Fresh start with empty database

---

## 💾 Backup Before Cleanup

### Backup Database
```bash
# Export database to SQL file
docker exec postdb_postgres pg_dump -U postgres -d postdb > backup.sql
```

### Restore Database
```bash
# After starting fresh
docker exec -i postdb_postgres psql -U postgres -d postdb < backup.sql
```

### Backup Volume
```bash
# Create backup of volume
docker run --rm -v learn_postgres_data:/data -v $(pwd):/backup \
  alpine tar czf /backup/postgres_backup.tar.gz /data
```

---

## ✅ Recommended Daily Workflow

### End of Day
```bash
# Stop containers but keep data
docker-compose down
```

### Start of Day
```bash
# Start fresh
docker-compose up -d

# Check status
docker-compose ps
```

### Weekly Cleanup (Optional)
```bash
# Remove unused images
docker image prune -a -f

# Check disk space
docker system df
```

---

## 🆘 Emergency Cleanup (Docker Not Working)

If Docker is completely stuck:

```bash
# 1. Stop Docker Desktop (macOS/Windows)
# Quit Docker Desktop application

# 2. Kill all Docker processes (Linux/macOS)
killall Docker
killall com.docker.backend

# 3. Restart Docker Desktop

# 4. Clean everything
docker system prune -a -f --volumes

# 5. Start fresh
docker-compose up -d
```

---

## 📝 Summary

**For Daily Use:**
```bash
# Stop
docker-compose down

# Start
docker-compose up -d
```

**For Fresh Start:**
```bash
# Delete everything
docker-compose down -v

# Start clean
docker-compose up -d
```

**For Troubleshooting:**
```bash
# Nuclear option
docker stop $(docker ps -q)
docker system prune -a -f --volumes
docker-compose up -d
```

---

## 🎯 Current Status (After Cleanup)

Run these to verify clean state:
```bash
docker ps -a        # Should show no containers
docker volume ls    # Should show no volumes
docker network ls   # Should show only default networks
```

---

**✅ You're now clean and ready to start fresh!**

Run `docker-compose up -d` when ready to start again.
