# Full Stack Social Network Application - Monorepo

A complete full-stack application featuring a Spring Boot backend API and a React/TypeScript frontend, orchestrated with Docker Compose and Nginx load balancing.

## 📁 Project Structure

```
.
├── backend/                 # Java/Spring Boot REST API
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
├── frontend/                # React/TypeScript UI
│   ├── src/
│   ├── package.json
│   ├── vite.config.ts
│   └── README.md
├── nginx/                   # Nginx load balancer configuration
│   ├── nginx.conf
│   └── Dockerfile
├── docs/                    # Documentation and scripts
│   ├── DOCKER_SETUP.md
│   ├── DOCKER_LOCAL_SETUP.md
│   ├── MONITORING_GUIDE.md
│   └── setup_data.sh
├── docker-compose.yml       # Docker Compose orchestration
├── .env.example            # Environment variables template
└── README.md               # This file
```

## 🚀 Quick Start

### Prerequisites
- Docker & Docker Compose
- Git
- Node.js 18+ (for local frontend development)
- Java 17+ (for local backend development)
- Maven 3.8+ (for local backend building)

### Option 1: Docker Compose (Recommended)

```bash
# Clone the repository
git clone <repository-url>
cd <repository-name>

# Copy environment variables
cp .env.example .env

# Start all services
docker-compose up -d

# Services will be available at:
# - Frontend: http://localhost:3000
# - Backend API: http://localhost:8080
# - Nginx (load balanced): http://localhost:80
```

### Option 2: Local Development

**Backend:**
```bash
cd backend
mvn spring-boot:run
# API runs on http://localhost:8080
```

**Frontend:**
```bash
cd frontend
npm install
npm run dev
# UI runs on http://localhost:5173
```

## 📚 Documentation

- [Backend README](./backend/README.md) - Backend API documentation
- [Frontend README](./frontend/README.md) - Frontend UI documentation
- [Docker Setup Guide](./docs/DOCKER_SETUP.md) - Docker configuration details
- [Monitoring Guide](./docs/MONITORING_GUIDE.md) - Application monitoring
- [Nginx Setup](./docs/NGINX_SETUP_COMPLETE.md) - Load balancer configuration

## 🐳 Docker Services

The `docker-compose.yml` orchestrates:

- **Backend (Port 8080)** - Spring Boot API with multiple instances
- **Frontend (Port 3000)** - React application
- **Nginx (Port 80)** - Load balancer and reverse proxy
- **PostgreSQL** - Database (if configured)

## 🔧 Configuration

Environment variables are managed in `.env`:

```env
SPRING_PROFILES_ACTIVE=docker
DATABASE_URL=jdbc:postgresql://postgres:5432/myapp
FRONTEND_URL=http://localhost:3000
```

See `.env.example` for all available options.

## 📝 Available Scripts

### Setup Scripts (in `docs/`)
- `setup_data.sh` - Initialize sample data
- `user_creation.sh` - Create test users
- `post_creation.sh` - Create sample posts
- `create_friends.sh` - Setup friendships

### Docker Commands
```bash
# Start services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down

# Remove volumes (careful!)
docker-compose down -v
```

## 🏗️ Architecture

### Backend
- **Framework:** Spring Boot 3.x
- **Language:** Java 17+
- **Database:** PostgreSQL
- **Testing:** JUnit, Mockito

### Frontend
- **Framework:** React 18+
- **Language:** TypeScript
- **Build Tool:** Vite
- **State Management:** Zustand
- **Styling:** CSS Modules

### Infrastructure
- **Containerization:** Docker
- **Orchestration:** Docker Compose
- **Load Balancing:** Nginx
- **Monitoring:** Docker stats, logs

## 🧪 Testing

**Backend:**
```bash
cd backend
mvn test
```

**Frontend:**
```bash
cd frontend
npm test
```

## 📦 Building for Production

**Backend:**
```bash
cd backend
mvn clean package -DskipTests
```

**Frontend:**
```bash
cd frontend
npm run build
```

**Docker Images:**
```bash
docker-compose build
```

## 🔐 Security Notes

- Never commit `.env` files with real credentials
- Use `.env.example` for template configuration
- Rotate credentials regularly
- Use environment-specific configurations
- Enable HTTPS in production

## 🤝 Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add your feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Submit a pull request

## 📋 Git Structure

This is a monorepo containing both frontend and backend code. Each can be developed and tested independently, but are deployed together.

## 🆘 Troubleshooting

### Services won't start
```bash
# Check Docker daemon is running
docker ps

# View detailed logs
docker-compose logs -f service-name
```

### Port conflicts
- Change ports in `docker-compose.yml`
- Or stop conflicting services: `lsof -i :port-number`

### Database issues
- Check database volume: `docker volume ls`
- Reset with: `docker-compose down -v`

## 📞 Support

For issues and questions:
1. Check existing documentation in `docs/`
2. Review service logs: `docker-compose logs`
3. Open an issue on GitHub

## 📄 License

[Add your license here]

## 🎉 Features

- ✅ Full-stack Docker setup
- ✅ Nginx load balancing
- ✅ RESTful API
- ✅ React frontend
- ✅ User authentication
- ✅ Social features (friends, posts, timeline)
- ✅ Automated testing
- ✅ Development and production profiles

---

**Last Updated:** January 2026
