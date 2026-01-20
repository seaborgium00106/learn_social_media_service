# Frontend Docker Setup Guide

## Overview

The frontend React/Vite application is now fully dockerized with support for both **development** and **production** modes.

## Architecture

```
┌─────────────────┐      ┌──────────────────┐
│   Frontend      │      │   NGINX          │
│   (Port 3000)   │◄────►│   Load Balancer  │
│   React/Vite    │      │   (Port 80)      │
└─────────────────┘      └──────────────────┘
                                  │
                    ┌─────────────┼─────────────┐
                    ▼             ▼             ▼
              ┌──────────┐  ┌──────────┐  ┌──────────┐
              │Backend-1 │  │Backend-2 │  │Backend-3 │
              │(Port 9091)│ │(Port 9092)│ │(Port 9093)│
              └──────────┘  └──────────┘  └──────────┘
                    │             │             │
                    └─────────────┼─────────────┘
                                  ▼
                          ┌──────────────┐
                          │  PostgreSQL  │
                          │  (Port 5432) │
                          └──────────────┘
```

## Quick Start

### Development Mode (Hot Reload)

```bash
# Start all services in development mode
docker-compose up

# Or rebuild and start
docker-compose up --build

# Access points:
# - Frontend: http://localhost:3000
# - API (via NGINX): http://localhost
```

### Production Mode (Optimized Build)

```bash
# Build and start in production mode
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up --build

# Access points:
# - Frontend: http://localhost:3000 (serving static files)
# - API (via NGINX): http://localhost
```

### Run Only Frontend

```bash
# Development
docker-compose up frontend

# Production
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up frontend
```

## Features

### Development Mode
- ✅ Hot Module Replacement (HMR) - instant updates on code changes
- ✅ Source maps for debugging
- ✅ Volume mounts for live code editing
- ✅ Fast rebuild times
- ✅ Development server with Vite

### Production Mode
- ✅ Optimized production build
- ✅ Static file serving with `serve`
- ✅ Minified and bundled assets
- ✅ Smaller image size
- ✅ Better performance

## Configuration

### Environment Variables

Edit `.env` file to configure:

```bash
# Frontend Port
FRONTEND_PORT=3000

# API Configuration
VITE_API_BASE_URL=http://localhost        # Use NGINX load balancer
VITE_API_TIMEOUT=10000
VITE_APP_NAME=Social Network
VITE_DEFAULT_PAGE_SIZE=10

# HMR Configuration (for Docker development)
VITE_HMR_HOST=localhost
VITE_HMR_PORT=3000
VITE_HMR_PROTOCOL=http
```

### API Base URL Options

1. **Through NGINX Load Balancer** (Recommended):
   ```bash
   VITE_API_BASE_URL=http://localhost
   ```

2. **Direct Backend Access**:
   ```bash
   VITE_API_BASE_URL=http://localhost:9091  # Backend-1
   ```

3. **Internal Docker Network** (when frontend runs inside Docker):
   ```bash
   VITE_API_BASE_URL=http://nginx  # Use service name
   ```

## Docker Files

### 1. `frontend/Dockerfile`
Multi-stage Dockerfile with 4 stages:
- **dependencies**: Install npm packages
- **development**: Dev server with HMR
- **builder**: Build production assets
- **production**: Serve static files

### 2. `frontend/.dockerignore`
Excludes unnecessary files from Docker context:
- `node_modules/`
- `dist/`
- `.git/`
- IDE files

### 3. `docker-compose.yml`
Main compose file with frontend service configured for development mode

### 4. `docker-compose.prod.yml`
Production override that changes the build target

## Useful Commands

### Build Images

```bash
# Build development image
docker build -t frontend-dev --target development ./frontend

# Build production image
docker build -t frontend-prod --target production ./frontend
```

### View Logs

```bash
# All services
docker-compose logs -f

# Frontend only
docker-compose logs -f frontend

# Last 100 lines
docker-compose logs --tail=100 frontend
```

### Restart Services

```bash
# Restart frontend
docker-compose restart frontend

# Rebuild and restart
docker-compose up -d --build frontend
```

### Stop Services

```bash
# Stop all
docker-compose down

# Stop and remove volumes
docker-compose down -v

# Stop frontend only
docker-compose stop frontend
```

### Execute Commands in Container

```bash
# Open shell in frontend container
docker-compose exec frontend sh

# Run npm commands
docker-compose exec frontend npm run lint
docker-compose exec frontend npm test
```

## Troubleshooting

### HMR Not Working

If hot reload isn't working in development mode:

1. Check that volumes are mounted correctly in `docker-compose.yml`
2. Ensure `VITE_HMR_HOST` matches your host machine
3. Verify `vite.config.ts` has correct HMR settings

### Build Errors

```bash
# Clear Docker cache and rebuild
docker-compose build --no-cache frontend

# Check TypeScript errors locally
cd frontend
npm run build
```

### Port Conflicts

If port 3000 is already in use:

```bash
# Change in .env file
FRONTEND_PORT=3001

# Restart
docker-compose up -d frontend
```

### Container Won't Start

```bash
# Check container logs
docker-compose logs frontend

# Check container status
docker-compose ps

# Inspect container
docker inspect postdb_frontend
```

## Performance Tips

### Development
- Use volume mounts for instant updates
- Keep `node_modules` in container (not mounted)
- Use `.dockerignore` to exclude unnecessary files

### Production
- Multi-stage builds reduce image size
- Static file serving is faster than dev server
- Enable gzip compression in production web server

## Next Steps

1. **Add NGINX reverse proxy** for frontend (optional)
2. **Set up CI/CD pipeline** for automated builds
3. **Add health monitoring** with Docker healthchecks
4. **Configure HTTPS** for production
5. **Add Redis caching** for API responses

## Resources

- [Vite Docker Guide](https://vitejs.dev/guide/build.html)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Multi-stage Builds](https://docs.docker.com/build/building/multi-stage/)
