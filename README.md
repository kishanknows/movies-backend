# Movies Backend

A Spring Boot REST API backend application for managing movies, built with Java 17, PostgreSQL, and Spring Security with JWT authentication.

## Overview

This project provides a robust backend service for movie management operations. It includes features like authentication, user management, and movie CRUD operations with a secure JWT-based authentication system.

## Tech Stack

- **Java**: 17
- **Framework**: Spring Boot 3.5.8
- **Database**: PostgreSQL
- **Build Tool**: Maven 3
- **Security**: Spring Security + JWT (JJWT 0.11.5)
- **ORM**: Spring Data JPA
- **Containerization**: Docker & Docker Compose
- **Environment Management**: java-dotenv

## Prerequisites

Before running the project, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.8+**
- **PostgreSQL 12+**
- **Docker & Docker Compose** (optional, for containerized deployment)
- **.env file** (for environment configuration)

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/kishanknows/movies-backend.git
cd movies-backend
```

### 2. Configure Environment Variables

Create a `.env` file in the project root with the following configuration:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/movies_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password
JWT_SECRET=your_jwt_secret_key_here
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

### 3. Install Dependencies

Using Maven:

```bash
./mvnw clean install
```

Or on Windows:

```bash
mvnw.cmd clean install
```

### 4. Run the Application

**Development Mode:**

```bash
./mvnw spring-boot:run
```

**Production Mode:**

First, build the JAR:

```bash
./mvnw clean package
```

Then run:

```bash
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## Docker Setup

### Using Docker Compose (Recommended)

1. Create a `.env` file in the project root with your database credentials
2. Run the following command:

```bash
docker-compose up --build
```

This will:
- Build the application Docker image
- Start the Spring Boot application on port 8080
- Configure database connections from environment variables

### Manual Docker Build

```bash
# Build the image
docker build -t movies-backend:latest .

# Run the container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/movies_db \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=your_password \
  movies-backend:latest
```

## Project Structure

```
movies-backend/
├── .mvn/                          # Maven wrapper configuration
├── src/
│   ├── main/
│   │   ├── java/com/kishanknows/
│   │   │   ├── controller/        # REST API endpoints
│   │   │   ├── service/           # Business logic
│   │   │   ├── repository/        # Database access layer
│   │   │   ├── entity/            # JPA entities
│   │   │   ├── security/          # JWT & Security config
│   │   │   ├── dto/               # Data Transfer Objects
│   │   │   ├── config/            # Application configuration
│   │   │   └── BackendApplication.java # Main entry point
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/
│       └── java/com/kishanknows/  # Unit & Integration tests
├── target/                        # Compiled output (auto-generated)
├── pom.xml                        # Maven dependencies & build config
├── Dockerfile                     # Docker configuration
├── docker-compose.yml             # Docker Compose orchestration
├── .gitignore                     # Git ignore rules
└── README.md                      # This file
```

## API Documentation

### Authentication

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "securepassword",
  "name": "John Doe"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "securepassword"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 86400
}
```

### Protected Endpoints

All protected endpoints require the JWT token in the Authorization header:

```http
Authorization: Bearer <your_jwt_token_here>
```

### Movie Endpoints (Example)

#### Get All Movies
```http
GET /api/movies
Authorization: Bearer <token>
```

#### Get Movie by ID
```http
GET /api/movies/{id}
Authorization: Bearer <token>
```

#### Create Movie
```http
POST /api/movies
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Movie Title",
  "description": "Movie description",
  "releaseDate": "2025-01-01",
  "rating": 8.5
}
```

#### Update Movie
```http
PUT /api/movies/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Updated Title",
  "description": "Updated description",
  "rating": 9.0
}
```

#### Delete Movie
```http
DELETE /api/movies/{id}
Authorization: Bearer <token>
```

## Database Schema

The application uses PostgreSQL with the following main entities:

- **User**: User authentication and profile
- **Movie**: Movie information and metadata
- **Review**: Movie reviews and ratings

Tables are automatically created/updated based on JPA entity definitions via Hibernate DDL management.

## Configuration Files

### application.properties

Default configuration:

```properties
spring.application.name=backend
spring.datasource.driverClassName=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
server.port=8080
```

### application-dev.properties

Development-specific settings with SQL logging enabled.

### application-prod.properties

Production-specific optimizations and security settings.

## Building and Packaging

### Build JAR

```bash
./mvnw clean package
```

### Build Docker Image

```bash
docker build -t kishanknows/movies-backend:latest .
```

### Maven Lifecycle Commands

```bash
./mvnw clean       # Clean build directory
./mvnw compile     # Compile source code
./mvnw test        # Run tests
./mvnw package     # Create JAR file
./mvnw install     # Install to local Maven repository
```

## Testing

Run unit and integration tests:

```bash
./mvnw test
```

Run specific test class:

```bash
./mvnw test -Dtest=YourTestClass
```

Run tests with coverage:

```bash
./mvnw clean test jacoco:report
```

## Security

### JWT Authentication

The application uses JWT tokens for stateless authentication:

1. User provides credentials to login endpoint
2. Server validates credentials and generates JWT token
3. Client includes token in Authorization header for subsequent requests
4. Server validates token signature and expiration

### Password Security

- Passwords are hashed using Spring Security's BCryptPasswordEncoder
- Never store plain text passwords

### CORS Configuration

Configure CORS in application.properties as needed for frontend integration.

## Deployment

### Local Deployment

1. Build the application: `./mvnw clean package`
2. Ensure PostgreSQL is running
3. Configure environment variables
4. Run: `java -jar target/backend-0.0.1-SNAPSHOT.jar`

### Docker Deployment

1. Build image: `docker build -t movies-backend:latest .`
2. Run container: `docker run -p 8080:8080 --env-file .env movies-backend:latest`

### Production Deployment

For production environments:

1. Use environment-specific configuration files
2. Enable HTTPS/TLS
3. Configure logging appropriately
4. Set up monitoring and alerting
5. Use container orchestration (Kubernetes, Docker Swarm) if needed
6. Implement proper database backups
7. Use secrets management for sensitive data

## Troubleshooting

### Issue: Port 8080 already in use

**Solution**: Change the port in `application.properties`:
```properties
server.port=8081
```

Or pass as runtime argument:
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Issue: Database connection failed

**Solution**: Verify your `.env` file has correct database credentials:
```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/movies_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=correct_password
```

### Issue: JWT token validation fails

**Solution**: Ensure `JWT_SECRET` is set in `.env` file and is consistent across application restarts.

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Make your changes
4. Commit: `git commit -am 'Add new feature'`
5. Push: `git push origin feature/your-feature`
6. Create a Pull Request

## Code Style

- Use Spring Boot conventions
- Follow Java naming conventions
- Write descriptive commit messages
- Add comments for complex logic
- Keep methods focused and single-responsibility

## Dependencies

All dependencies are managed in `pom.xml`. Key dependencies:

| Dependency | Version | Purpose |
|-----------|---------|---------|
| Spring Boot Starter | 3.5.8 | Core framework |
| Spring Data JPA | Latest | Database ORM |
| PostgreSQL Driver | Latest | Database connectivity |
| Spring Security | Latest | Authentication & Authorization |
| JJWT | 0.11.5 | JWT token handling |
| java-dotenv | 5.2.2 | Environment variable loading |

## Performance Optimization

- Enable query caching for frequently accessed data
- Use lazy loading for JPA relationships
- Implement pagination for large datasets
- Add database indexes on frequently queried columns
- Use connection pooling (HikariCP is default in Spring Boot)

## Monitoring

Monitor your application with:

- Application logs in `logs/` directory
- Spring Boot Actuator endpoints (add spring-boot-starter-actuator)
- Database query performance metrics
- Exception tracking and logging

## License

This project is open source and available under the MIT License.

## Support

For issues, questions, or suggestions, please open an issue on [GitHub Issues](https://github.com/kishanknows/movies-backend/issues).

## Changelog

### Version 0.0.1-SNAPSHOT

- Initial project setup
- Spring Boot 3.5.8 configuration
- PostgreSQL integration with Spring Data JPA
- JWT-based authentication
- Docker and Docker Compose configuration
- Maven build setup with Java 17

---

**Last Updated**: 2026-05-09  
**Repository**: [kishanknows/movies-backend](https://github.com/kishanknows/movies-backend)
