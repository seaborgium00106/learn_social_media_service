# 🔄 NGINX Load Balancer Implementation Plan

## 📊 Current State
```
docker-compose.yml (CURRENT):
├── postdb_backend    (port 9090)
├── postdb_postgres   (port 5432)
└── backend-network
```

## 🎯 Target State
```
docker-compose.yml (NEW):
├── nginx             (port 80 → routes to backends)
├── backend-1         (port 9091)
├── backend-2         (port 9092)
├── backend-3         (port 9093)
├── postdb_postgres   (port 5432)
└── backend-network
```

---

## 📋 Implementation Steps

### Phase 1: Create NGINX Configuration
**Files to Create:**
1. `nginx/nginx.conf` - NGINX configuration with load balancing
2. `nginx/Dockerfile` - NGINX container image

**What it does:**
- Listens on port 80
- Routes requests to 3 backend instances (9091, 9092, 9093)
- Round-robin load balancing
- Health checks on backends
- Automatic failover if backend down

---

### Phase 2: Update docker-compose.yml
**Changes:**
1. Add NGINX service (listens on port 80)
2. Scale backend from 1 instance to 3 instances (9091, 9092, 9093)
3. Keep PostgreSQL as is (port 5432)
4. All services on same `backend-network`

**Result:**
- Run: `docker-compose up -d`
- All 5 services start automatically
- NGINX routes traffic to backends

---

### Phase 3: Update Frontend Configuration
**File:** `frontend/src/config/apiConfig.ts`

**Current:**
```typescript
baseURL: 'http://localhost:9090'
```

**Updated:**
```typescript
baseURL: 'http://localhost:80'  // or just 'http://localhost'
```

---

### Phase 4: Testing & Verification
1. Start all containers: `docker-compose up -d`
2. Verify all 5 services running: `docker-compose ps`
3. Test API through NGINX: `curl http://localhost:80/api/v1/users`
4. Check load balancing: Watch logs as you make requests

---

## 📁 Files to Create/Modify

| File | Action | Priority |
|------|--------|----------|
| `nginx/nginx.conf` | **CREATE** | 🔴 High |
| `nginx/Dockerfile` | **CREATE** | 🔴 High |
| `docker-compose.yml` | **MODIFY** | 🔴 High |
| `frontend/src/config/apiConfig.ts` | **MODIFY** | 🟡 Medium |

---

## 🔍 What NGINX Will Do

1. **Listen on port 80**
   - Frontend connects to `http://localhost`
   - NGINX receives all requests

2. **Route to backends (round-robin)**
   - Request 1 → Backend 1 (9091)
   - Request 2 → Backend 2 (9092)
   - Request 3 → Backend 3 (9093)
   - Request 4 → Backend 1 (9091) [repeats]

3. **Health checks**
   - Every 10 seconds check `/actuator/health`
   - If backend down → remove from rotation
   - If backend recovers → add back to rotation

4. **Automatic failover**
   - If Backend 2 fails → requests go to 1 & 3
   - No manual intervention needed

---

## ⚙️ Architecture Diagram

```
                    User/Frontend
                         │
                         ▼
                  ┌──────────────┐
                  │    NGINX     │
                  │  (port 80)   │
                  │              │
                  │ Load Balancer│
                  └──────┬───────┘
                         │
           ┌─────────────┼─────────────┐
           │             │             │
           ▼             ▼             ▼
        ┌────────┐   ┌────────┐   ┌────────┐
        │Backend │   │Backend │   │Backend │
        │   1    │   │   2    │   │   3    │
        │:9091   │   │:9092   │   │:9093   │
        └────┬───┘   └────┬───┘   └────┬───┘
             │            │            │
             └────────────┼────────────┘
                          │
                          ▼
                   ┌──────────────┐
                   │ PostgreSQL   │
                   │ (port 5432)  │
                   └──────────────┘
```

---

## 🚀 Quick Commands Preview

After implementation:

```bash
# Start all (NGINX + 3 backends + PostgreSQL)
docker-compose up -d

# Check all services
docker-compose ps

# View NGINX logs
docker-compose logs nginx

# View backend logs
docker-compose logs backend-1
docker-compose logs backend-2
docker-compose logs backend-3

# Test through NGINX
curl http://localhost/api/v1/users

# Monitor load balancing
docker-compose logs -f nginx
```

---

## 📊 Load Balancing Behavior

**Example: Creating 3 users in sequence**

```
User Request 1 → NGINX → Backend 1 (9091)   ✓ User created
User Request 2 → NGINX → Backend 2 (9092)   ✓ User created
User Request 3 → NGINX → Backend 3 (9093)   ✓ User created
User Request 4 → NGINX → Backend 1 (9091)   ✓ User created (cycles back)

All users stored in shared PostgreSQL database
```

---

## 🔐 Key Points

✅ **What NGINX Will Do:**
- Round-robin requests to 3 backends
- Health check each backend every 10 seconds
- Automatically remove unhealthy backends from rotation
- Serve as single entry point (port 80)

✅ **No Single Point of Failure:**
- If Backend 1 dies → requests go to 2 & 3
- If Backend 2 dies → requests go to 1 & 3
- If Backend 3 dies → requests go to 1 & 2
- All data in shared PostgreSQL (no data loss)

✅ **Backward Compatible:**
- Current frontend/backend code unchanged
- Just point frontend to port 80 instead of 9090
- Everything else works the same

---

## ⏱️ Implementation Time Estimate

| Task | Time |
|------|------|
| Create nginx/nginx.conf | 10 min |
| Create nginx/Dockerfile | 5 min |
| Update docker-compose.yml | 10 min |
| Update frontend config | 5 min |
| Build and test | 10 min |
| **Total** | **~40 minutes** |

---

## ✅ Success Criteria

After implementation:

- [ ] `docker-compose ps` shows 5 services (nginx + 3 backends + postgres)
- [ ] All services show "Up" status
- [ ] `curl http://localhost/api/v1/users` works
- [ ] Creating users works through NGINX
- [ ] NGINX logs show round-robin routing
- [ ] If you stop one backend, requests still work
- [ ] Frontend connects to `http://localhost`

---

## 🎯 Ready to Proceed?

**Next actions:**
1. ✅ You approve this plan
2. I create the 4 files (nginx.conf, Dockerfile, update docker-compose.yml, update frontend config)
3. We test everything locally
4. Verify load balancing is working

**Proceed with implementation?** Yes / No / Ask questions first

---

## 📝 Alternative Approach (If Needed)

If you want to keep things simpler initially:
- Run 1 backend + NGINX (test NGINX alone)
- Then scale to 3 backends later

Would you prefer this?

---

**Status**: 📋 Plan Ready - Waiting for Approval
