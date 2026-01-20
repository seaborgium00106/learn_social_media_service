# Post & User API Guide

## Overview
A RESTful Spring Boot application for managing users and posts with:
- **Spring Data JPA** for database operations
- **H2** embedded database (development)
- **PostgreSQL** support (production-ready)
- **Swagger UI** for API documentation

## Architecture

### Database Schema
```
┌─────────────────┐         ┌──────────────────┐
│     USERS       │         │      POSTS       │
├─────────────────┤         ├──────────────────┤
│ id (PK)         │◄────────│ id (PK)          │
│ username        │         │ user_id (FK)     │
│ email           │         │ text             │
│ created_at      │         │ created_at       │
└─────────────────┘         │ updated_at       │
                            └──────────────────┘
```

### Technology Stack
- **Framework:** Spring Boot 3.2.1
- **ORM:** Hibernate (via Spring Data JPA)
- **Database:** H2 (in-memory) / PostgreSQL
- **API Documentation:** SpringDoc OpenAPI 3 (Swagger)
- **Build Tool:** Maven

---

## Quick Start

### 1. Build the Application
```bash
mvn clean package
```

### 2. Run the Application
```bash
java -jar target/hello-world-api-1.0.0.jar
```

Or using Maven:
```bash
mvn spring-boot:run
```

### 3. Access the Application
- **Swagger UI:** http://localhost:9090/swagger-ui.html
- **H2 Console:** http://localhost:9090/h2-console
  - JDBC URL: `jdbc:h2:mem:postdb`
  - Username: `sa`
  - Password: *(leave empty)*
- **API Base URL:** http://localhost:9090/api/v1

---

## API Endpoints

### User Endpoints

#### Create User
```bash
POST /api/v1/users
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com"
}

Response: 201 Created
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "createdAt": "2026-01-18T15:54:27.120983",
  "postCount": 0
}
```

#### Get All Users
```bash
GET /api/v1/users

Response: 200 OK
[
  {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "createdAt": "2026-01-18T15:54:27.120983",
    "postCount": 2
  }
]
```

#### Get User by ID
```bash
GET /api/v1/users/{id}

Response: 200 OK
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "createdAt": "2026-01-18T15:54:27.120983",
  "postCount": 2
}
```

#### Get User by Username
```bash
GET /api/v1/users/username/{username}

Example: GET /api/v1/users/username/john_doe
```

#### Update User
```bash
PUT /api/v1/users/{id}
Content-Type: application/json

{
  "username": "john_updated",
  "email": "john_new@example.com"
}

Response: 200 OK
```

#### Delete User
```bash
DELETE /api/v1/users/{id}

Response: 204 No Content
```

---

### Post Endpoints

#### Create Post
```bash
POST /api/v1/posts
Content-Type: application/json

{
  "text": "Hello! This is my first post about Spring Boot with JPA!",
  "userId": 1
}

Response: 201 Created
{
  "id": 1,
  "text": "Hello! This is my first post about Spring Boot with JPA!",
  "userId": 1,
  "username": "john_doe",
  "createdAt": "2026-01-18T15:54:36.010880",
  "updatedAt": "2026-01-18T15:54:36.010891"
}
```

#### Get All Posts (with Pagination)
```bash
GET /api/v1/posts?page=0&size=10

Response: 200 OK (sorted by createdAt DESC)
[
  {
    "id": 3,
    "text": "Latest post...",
    "userId": 2,
    "username": "jane_smith",
    "createdAt": "2026-01-18T15:54:36.050301",
    "updatedAt": "2026-01-18T15:54:36.050310"
  },
  ...
]
```

#### Get Post by ID
```bash
GET /api/v1/posts/{id}

Response: 200 OK
```

#### Get Posts by User
```bash
GET /api/v1/posts/user/{userId}

Example: GET /api/v1/posts/user/1

Response: 200 OK
[
  {
    "id": 1,
    "text": "Hello! This is my first post...",
    "userId": 1,
    "username": "john_doe",
    "createdAt": "2026-01-18T15:54:36.010880",
    "updatedAt": "2026-01-18T15:54:36.010891"
  }
]
```

#### Search Posts
```bash
GET /api/v1/posts/search?search=JPA

Response: 200 OK (case-insensitive search)
[
  {
    "id": 1,
    "text": "Hello! This is my first post about Spring Boot with JPA!",
    ...
  }
]
```

#### Update Post
```bash
PUT /api/v1/posts/{id}
Content-Type: application/json

{
  "text": "Updated post content",
  "userId": 1
}

Response: 200 OK
```

#### Delete Post
```bash
DELETE /api/v1/posts/{id}

Response: 204 No Content
```

---

## Code Structure

```
src/main/java/com/example/helloworld/
│
├── controller/           # REST Controllers
│   ├── UserController.java
│   ├── PostController.java
│   └── GreetingController.java
│
├── service/             # Business Logic
│   ├── UserService.java
│   └── PostService.java
│
├── repository/          # JPA Repositories
│   ├── UserRepository.java
│   └── PostRepository.java
│
├── model/               # JPA Entities
│   ├── User.java
│   └── Post.java
│
├── dto/                 # Data Transfer Objects
│   ├── UserRequest.java
│   ├── UserResponse.java
│   ├── PostRequest.java
│   └── PostResponse.java
│
└── HelloWorldApplication.java  # Main Application
```

---

## Key Features Implemented

### 1. **JPA Relationships**
- `@OneToMany`: User → Posts
- `@ManyToOne`: Post → User
- `@JoinColumn`: Foreign key relationship
- Cascade operations and orphan removal

### 2. **Repository Pattern**
Spring Data JPA auto-generates implementations:
```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
```

### 3. **Custom Query Methods**
```java
List<Post> findByUserId(Long userId);
List<Post> findByTextContainingIgnoreCase(String text);
```

### 4. **Pagination & Sorting**
```java
Page<Post> findAll(Pageable pageable);
Sort.by("createdAt").descending()
```

### 5. **Automatic Timestamps**
```java
@CreationTimestamp
private LocalDateTime createdAt;

@UpdateTimestamp
private LocalDateTime updatedAt;
```

### 6. **Validation & Error Handling**
- Unique username and email validation
- Foreign key validation
- Custom error messages

---

## Switching to PostgreSQL

### Step 1: Install PostgreSQL
```bash
# macOS
brew install postgresql
brew services start postgresql

# Create database
createdb postdb
```

### Step 2: Update `application.properties`
Comment out H2 configuration and uncomment PostgreSQL:

```properties
# H2 Database Configuration (COMMENT OUT)
#spring.datasource.url=jdbc:h2:mem:postdb
#spring.datasource.driverClassName=org.h2.Driver
#spring.h2.console.enabled=false
#spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# PostgreSQL Configuration (UNCOMMENT)
spring.datasource.url=jdbc:postgresql://localhost:5432/postdb
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

### Step 3: Restart Application
```bash
mvn spring-boot:run
```

**That's it!** No code changes needed - same JPA code works with PostgreSQL!

---

## Using Docker for PostgreSQL

### docker-compose.yml
```yaml
version: '3.8'
services:
  postgres:
    image: postgres:16
    container_name: postdb
    environment:
      POSTGRES_DB: postdb
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

### Start PostgreSQL
```bash
docker-compose up -d
```

---

## Testing with Swagger UI

1. Open http://localhost:9090/swagger-ui.html
2. Expand endpoint sections (Users, Posts)
3. Click "Try it out"
4. Fill in request body
5. Click "Execute"
6. View response

---

## Learning Path

### ✅ What You've Learned
1. **Spring Data JPA** - Repository pattern, query methods
2. **Hibernate** - ORM mapping, relationships, cascading
3. **H2 Database** - In-memory database for development
4. **REST API Design** - CRUD operations, DTOs
5. **Database Relationships** - One-to-Many, Foreign Keys
6. **Pagination & Search** - Filtering and sorting data

### 🎯 Next Steps
1. **Add Validation** - `@Valid`, `@NotNull`, `@Size`, etc.
2. **Exception Handling** - `@ControllerAdvice`, custom exceptions
3. **Testing** - Unit tests (Mockito) and integration tests
4. **Security** - Spring Security, JWT authentication
5. **Caching** - Redis or Caffeine for performance
6. **Docker** - Containerize the application
7. **CI/CD** - GitHub Actions, Jenkins

---

## Troubleshooting

### H2 Console Not Loading
- Ensure `spring.h2.console.enabled=true`
- Check URL: http://localhost:9090/h2-console
- JDBC URL must match: `jdbc:h2:mem:postdb`

### SQL Queries Not Showing
- Ensure `spring.jpa.show-sql=true`
- Check console output

### Unique Constraint Violations
- Username and email must be unique
- Check if user already exists

### Lazy Loading Errors
- Use `@Transactional` on service methods
- Consider fetch strategies: `FetchType.EAGER` vs `LAZY`

---

## Additional Resources

- **Spring Data JPA Docs:** https://spring.io/projects/spring-data-jpa
- **Hibernate Guide:** https://hibernate.org/orm/documentation/
- **H2 Database:** https://www.h2database.com/
- **PostgreSQL:** https://www.postgresql.org/docs/

---

## Summary

You've successfully built a **production-ready Spring Boot application** with:
- ✅ Spring Data JPA + Hibernate
- ✅ H2 (development) and PostgreSQL (production) support
- ✅ RESTful API with full CRUD operations
- ✅ Database relationships (One-to-Many)
- ✅ Pagination, sorting, and search
- ✅ Swagger documentation
- ✅ Best practices (DTOs, service layer, repositories)

**Congratulations!** 🎉 You're now ready to build more complex applications!
