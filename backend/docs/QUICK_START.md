# Quick Start Guide

## What We Built
A full-stack Spring Boot REST API with:
- **Users** table (id, username, email, created_at)
- **Posts** table (id, text, user_id, created_at, updated_at)
- **One-to-Many** relationship (User → Posts)

## Run the Application

### Option 1: Using Maven
```bash
mvn spring-boot:run
```

### Option 2: Using JAR
```bash
mvn clean package
java -jar target/hello-world-api-1.0.0.jar
```

## Access Points
- **Swagger UI:** http://localhost:9090/swagger-ui.html
- **H2 Console:** http://localhost:9090/h2-console
- **API Base:** http://localhost:9090/api/v1

## Quick Test

### Create a User
```bash
wget -q -O- --post-data='{"username": "john", "email": "john@example.com"}' \
  --header='Content-Type: application/json' \
  http://localhost:9090/api/v1/users
```

### Create a Post
```bash
wget -q -O- --post-data='{"text": "My first post!", "userId": 1}' \
  --header='Content-Type: application/json' \
  http://localhost:9090/api/v1/posts
```

### Get All Posts
```bash
wget -q -O- http://localhost:9090/api/v1/posts
```

## Project Structure
```
src/main/java/com/example/helloworld/
├── controller/      # REST endpoints
├── service/         # Business logic
├── repository/      # Database access (JPA)
├── model/          # Entities (User, Post)
└── dto/            # Request/Response objects
```

## Database: H2 vs PostgreSQL

### Currently Using: H2 (In-Memory)
- ✅ Zero setup
- ✅ Perfect for learning
- ✅ Data resets on restart
- ✅ H2 Console for debugging

### Switch to PostgreSQL (Production)
Edit `src/main/resources/application.properties`:
```properties
# Comment out H2 lines
# Uncomment PostgreSQL lines
spring.datasource.url=jdbc:postgresql://localhost:5432/postdb
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

**No code changes needed!** Same JPA code works with both databases.

## Key Technologies
- **Spring Boot 3.2.1**
- **Spring Data JPA** (with Hibernate)
- **H2 Database** (dev) / **PostgreSQL** (prod)
- **Swagger/OpenAPI** for documentation
- **Lombok** to reduce boilerplate

## All Endpoints

### Users
- `POST /api/v1/users` - Create user
- `GET /api/v1/users` - Get all users
- `GET /api/v1/users/{id}` - Get user by ID
- `GET /api/v1/users/username/{username}` - Get by username
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user

### Posts
- `POST /api/v1/posts` - Create post
- `GET /api/v1/posts` - Get all posts (paginated)
- `GET /api/v1/posts/{id}` - Get post by ID
- `GET /api/v1/posts/user/{userId}` - Get user's posts
- `GET /api/v1/posts/search?search=text` - Search posts
- `PUT /api/v1/posts/{id}` - Update post
- `DELETE /api/v1/posts/{id}` - Delete post

## Next Steps for Learning
1. ✅ **Done:** Basic CRUD with JPA
2. 🎯 Add input validation (`@Valid`, `@NotNull`)
3. 🎯 Add exception handling (`@ControllerAdvice`)
4. 🎯 Write unit tests (JUnit, Mockito)
5. 🎯 Add Spring Security (authentication)
6. 🎯 Try PostgreSQL with Docker
7. 🎯 Add pagination improvements
8. 🎯 Implement soft deletes

## Need Help?
- Check `API_GUIDE.md` for comprehensive documentation
- Use Swagger UI to explore API interactively
- Use H2 Console to see database tables and data
