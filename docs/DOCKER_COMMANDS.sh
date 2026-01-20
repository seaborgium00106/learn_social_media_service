#!/bin/bash

# 🐳 DOCKER COMMANDS REFERENCE
# Copy and paste these commands directly into your terminal

echo "╔════════════════════════════════════════════════════════════╗"
echo "║         DOCKER COMMANDS REFERENCE SCRIPT                  ║"
echo "║        Use individual commands from this script            ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# ============================================================
# BUILD & START
# ============================================================

# Build Docker image
build_image() {
    echo "🔨 Building Docker image..."
    docker-compose build
}

# Start all containers
start_containers() {
    echo "🚀 Starting containers..."
    docker-compose up -d
}

# Start with rebuild (after code changes)
start_with_rebuild() {
    echo "🔄 Rebuilding and starting..."
    docker-compose up -d --build
}

# Quick start (build + start)
quick_start() {
    echo "⚡ Quick start..."
    docker-compose build
    docker-compose up -d
    echo "✅ Containers started!"
    echo "Backend: http://localhost:9090"
    echo "Database: localhost:5432"
}

# ============================================================
# STATUS & MONITORING
# ============================================================

# Check container status
status() {
    echo "📊 Container Status:"
    docker-compose ps
}

# View backend logs
logs_backend() {
    echo "📋 Backend Logs:"
    docker-compose logs backend
}

# View database logs
logs_postgres() {
    echo "📋 Database Logs:"
    docker-compose logs postgres
}

# View all logs
logs_all() {
    echo "📋 All Logs:"
    docker-compose logs
}

# Follow backend logs (live)
logs_follow() {
    echo "📋 Following Backend Logs (press Ctrl+C to stop):"
    docker-compose logs -f backend
}

# View last 50 lines and follow
logs_tail() {
    echo "📋 Last 50 lines (live):"
    docker-compose logs -f --tail=50 backend
}

# Docker resource usage
resource_usage() {
    echo "💾 Docker Resource Usage:"
    docker stats
}

# ============================================================
# STOP & CLEANUP
# ============================================================

# Stop containers (keep data)
stop_containers() {
    echo "⏹️  Stopping containers..."
    docker-compose stop
}

# Stop and remove containers (keep data)
down() {
    echo "🛑 Stopping and removing containers..."
    docker-compose down
}

# Stop and remove everything including database (DESTRUCTIVE)
clean_everything() {
    echo "⚠️  WARNING: This will DELETE all database data!"
    read -p "Continue? (yes/no) " -n 3 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo "🗑️  Removing everything..."
        docker-compose down -v
        echo "✅ Clean slate ready!"
    else
        echo "❌ Cancelled"
    fi
}

# ============================================================
# DATABASE OPERATIONS
# ============================================================

# Connect to PostgreSQL
db_connect() {
    echo "🗄️  Connecting to PostgreSQL..."
    docker exec -it postdb_postgres psql -U postgres -d postdb
}

# List all tables
db_list_tables() {
    echo "📊 Database Tables:"
    docker exec -it postdb_postgres psql -U postgres -d postdb -c "\dt"
}

# View users table
db_view_users() {
    echo "👥 Users Table:"
    docker exec postdb_postgres psql -U postgres -d postdb -c "SELECT id, username, email, created_at FROM users;"
}

# View posts table
db_view_posts() {
    echo "📝 Posts Table:"
    docker exec postdb_postgres psql -U postgres -d postdb -c "SELECT id, user_id, content, created_at FROM posts LIMIT 10;"
}

# View friendships table
db_view_friendships() {
    echo "🤝 Friendships Table:"
    docker exec postdb_postgres psql -U postgres -d postdb -c "SELECT id, user_id, friend_id FROM friendships LIMIT 10;"
}

# Backup database to file
db_backup() {
    BACKUP_FILE="backup_$(date +%Y%m%d_%H%M%S).sql"
    echo "💾 Backing up database to $BACKUP_FILE..."
    docker exec postdb_postgres pg_dump -U postgres -d postdb > "$BACKUP_FILE"
    echo "✅ Backup complete: $BACKUP_FILE"
}

# Restore database from backup
db_restore() {
    if [ -z "$1" ]; then
        echo "❌ Usage: db_restore backup_file.sql"
        return 1
    fi
    echo "♻️  Restoring from $1..."
    docker exec -i postdb_postgres psql -U postgres -d postdb < "$1"
    echo "✅ Restore complete"
}

# ============================================================
# API TESTING
# ============================================================

# Get all users
api_get_users() {
    echo "📋 Getting all users..."
    curl -s http://localhost:9090/api/v1/users | jq .
}

# Create test user
api_create_user() {
    USERNAME="${1:-testuser}"
    EMAIL="${2:-test@example.com}"
    echo "➕ Creating user: $USERNAME..."
    curl -s -X POST http://localhost:9090/api/v1/users \
      -H "Content-Type: application/json" \
      -d "{\"username\":\"$USERNAME\",\"email\":\"$EMAIL\"}" | jq .
}

# Get all posts
api_get_posts() {
    echo "📝 Getting all posts..."
    curl -s http://localhost:9090/api/v1/posts | jq .
}

# Create test post
api_create_post() {
    USER_ID="${1:-1}"
    CONTENT="${2:-Test post from Docker}"
    echo "➕ Creating post..."
    curl -s -X POST http://localhost:9090/api/v1/posts \
      -H "Content-Type: application/json" \
      -d "{\"userId\":$USER_ID,\"content\":\"$CONTENT\"}" | jq .
}

# Get user timeline
api_get_timeline() {
    USER_ID="${1:-1}"
    echo "📰 Getting timeline for user $USER_ID..."
    curl -s http://localhost:9090/api/v1/timeline/$USER_ID | jq .
}

# ============================================================
# TROUBLESHOOTING
# ============================================================

# Restart backend container
restart_backend() {
    echo "🔄 Restarting backend..."
    docker-compose restart backend
}

# Restart database container
restart_postgres() {
    echo "🔄 Restarting database..."
    docker-compose restart postgres
}

# Execute command in backend container
exec_backend() {
    if [ -z "$1" ]; then
        echo "❌ Usage: exec_backend 'command'"
        return 1
    fi
    echo "⚙️  Executing in backend: $1"
    docker exec -it postdb_backend $1
}

# Execute command in postgres container
exec_postgres() {
    if [ -z "$1" ]; then
        echo "❌ Usage: exec_postgres 'command'"
        return 1
    fi
    echo "⚙️  Executing in postgres: $1"
    docker exec -it postdb_postgres $1
}

# Check backend health
health_check() {
    echo "🏥 Checking backend health..."
    curl -s http://localhost:9090/actuator/health 2>/dev/null || echo "❌ Backend not responding"
}

# Full cleanup and rebuild
full_reset() {
    echo "⚠️  WARNING: This will DELETE everything and rebuild!"
    read -p "Continue? (yes/no) " -n 3 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo "🗑️  Cleaning up..."
        docker-compose down -v
        docker system prune -a -f
        echo "🔨 Rebuilding..."
        docker-compose build --no-cache
        echo "🚀 Starting..."
        docker-compose up -d
        echo "✅ Full reset complete!"
    else
        echo "❌ Cancelled"
    fi
}

# ============================================================
# HELP MENU
# ============================================================

help_menu() {
    cat << 'EOF'

╔════════════════════════════════════════════════════════════╗
║            AVAILABLE DOCKER COMMANDS                       ║
╚════════════════════════════════════════════════════════════╝

BUILD & START:
  quick_start              - Build and start everything
  build_image              - Build Docker image only
  start_containers         - Start containers
  start_with_rebuild       - Rebuild and start

STATUS & MONITORING:
  status                   - Show container status
  logs_backend             - View backend logs
  logs_postgres            - View database logs
  logs_all                 - View all logs
  logs_follow              - Follow backend logs live
  logs_tail                - Last 50 lines live
  resource_usage           - Docker resource usage
  health_check             - Check backend health

STOP & CLEANUP:
  stop_containers          - Stop containers (keep data)
  down                     - Stop and remove containers (keep data)
  clean_everything         - Remove EVERYTHING including data (DESTRUCTIVE)
  full_reset               - Clean, rebuild, restart

DATABASE:
  db_connect               - Connect to PostgreSQL shell
  db_list_tables           - List all tables
  db_view_users            - View users table
  db_view_posts            - View posts table
  db_view_friendships      - View friendships table
  db_backup                - Backup database to SQL file
  db_restore <file>        - Restore database from backup

API TESTING:
  api_get_users            - List all users
  api_create_user [name] [email] - Create test user
  api_get_posts            - List all posts
  api_create_post [id] [content] - Create test post
  api_get_timeline [id]    - Get user timeline

TROUBLESHOOTING:
  restart_backend          - Restart backend container
  restart_postgres         - Restart database container
  exec_backend 'cmd'       - Run command in backend
  exec_postgres 'cmd'      - Run command in postgres
  help_menu                - Show this help

EXAMPLES:
  quick_start              # First time setup
  status                   # Check if running
  logs_follow              # Monitor backend
  api_create_user alice alice@example.com  # Create user
  db_view_users            # See all users in database
  db_backup                # Backup database
  clean_everything         # Delete everything

EOF
}

# ============================================================
# MAIN - Show help if script is run directly
# ============================================================

if [ "$0" = "${BASH_SOURCE[0]}" ]; then
    help_menu
    echo ""
    echo "💡 To use these commands:"
    echo "   1. Source this file: source DOCKER_COMMANDS.sh"
    echo "   2. Run any command: quick_start"
    echo ""
    echo "Or copy individual commands into your terminal!"
fi
