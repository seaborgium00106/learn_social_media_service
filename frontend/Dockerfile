# Multi-stage Dockerfile for React/Vite Frontend
# Supports both development (hot reload) and production (static files) modes

# Stage 1: Dependencies
FROM node:20-alpine AS dependencies

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci

# Stage 2: Development
FROM dependencies AS development

WORKDIR /app

# Copy all source files
COPY . .

# Expose the development server port
EXPOSE 3000

# Health check for dev server
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:3000 || exit 1

# Run development server with host binding to accept connections from outside container
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]

# Stage 3: Builder (for production)
FROM dependencies AS builder

WORKDIR /app

# Copy all source files
COPY . .

# Build the application
RUN npm run build

# Stage 4: Production (serve built files)
FROM node:20-alpine AS production

WORKDIR /app

# Install a simple static file server
RUN npm install -g serve

# Copy built files from builder stage
COPY --from=builder /app/dist ./dist

# Expose the production server port
EXPOSE 3000

# Health check for production server
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:3000 || exit 1

# Serve the built files
CMD ["serve", "-s", "dist", "-l", "3000"]

# Default to production stage
