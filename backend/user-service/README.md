# MockMule – User Service

The User Service is a Spring Boot microservice responsible for user registration, login, authentication, and role-based authorization in the MockMule platform.

## 1. Overview

The service provides:

- User registration
- User login
- Password encryption using BCrypt
- JWT generation and validation
- Stateless authentication
- Role-based authorization (`USER` and `ADMIN`)
- Centralized exception handling
- Request validation
- Consistent API response structure

## 2. Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot | Backend framework |
| Spring Security | Authentication and authorization |
| Spring Data JPA | Database operations |
| H2 Database | Development database |
| JWT (JJWT) | Token-based authentication |
| BCrypt | Password encryption |
| Maven | Build and dependency management |
| JUnit 5 | Unit and integration testing |
| Mockito | Mocking dependencies |
| MockMvc | API integration testing |

## 3. Project Structure

```text
user-service/
├── src/
│   ├── main/
│   │   ├── java/com/mockmule/userservice/
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── JwtAuthenticationFilter.java
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   └── response/
│   │   │   ├── entity/
│   │   │   ├── enums/
│   │   │   ├── exception/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   │   └── impl/
│   │   │   └── UserServiceApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/mockmule/userservice/
│           ├── tests/
│           │   ├── controller/
│           │   ├── service/
│           │   ├── repository/
│           │   └── security/
│           └── UserServiceApplicationTests.java
└── pom.xml
```

## 4. API Endpoints

Base path: `/api/v1/users`

| Method | Endpoint | Description | Authentication |
|---|---|---|---|
| GET | `/health` | Health check | Public |
| POST | `/register` | Register a user | Public |
| POST | `/login` | Authenticate a user | Public |

### 4.1 User Registration

**Endpoint:** `POST /api/v1/users/register`

Request:

```json
{
  "username": "mockuser",
  "email": "mockuser@example.com",
  "password": "Test@1234"
}
```

Successful response – HTTP 201:

```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "username": "mockuser",
    "email": "mockuser@example.com",
    "message": "User registered successfully"
  }
}
```

Possible error responses:

- HTTP 400 – Invalid request data
- HTTP 409 – Email or username already exists

### 4.2 User Login

**Endpoint:** `POST /api/v1/users/login`

Request:

```json
{
  "email": "mockuser@example.com",
  "password": "Test@1234"
}
```

Successful response – HTTP 200:

```json
{
  "success": true,
  "message": "User logged in successfully",
  "data": {
    "id": 1,
    "username": "mockuser",
    "email": "mockuser@example.com",
    "token": "<JWT_TOKEN>",
    "message": "User logged in successfully"
  }
}
```

Possible error response:

- HTTP 401 – Invalid email or password

### 4.3 Health Check

**Endpoint:** `GET /api/v1/users/health`

This endpoint is publicly accessible and can be used to verify that the service is responding.

## 5. Authentication and Security

The service uses JWT-based stateless authentication.

### Registration

1. Validate the incoming registration request.
2. Check whether the email and username already exist.
3. Encrypt the password using BCrypt.
4. Save the user with the default `USER` role.
5. Return the registration response.

### Login

1. Find the user using the supplied email.
2. Verify the password using BCrypt.
3. Generate a signed JWT.
4. Return the token to the client.

### JWT Authentication

For protected endpoints, clients must provide the JWT in the Authorization header:

```http
Authorization: Bearer <JWT_TOKEN>
```

The JWT authentication filter:

- Extracts the token from the Authorization header.
- Validates the token.
- Extracts the user ID.
- Retrieves the user from the database.
- Establishes the Spring Security authentication context.
- Assigns the user's role as an authority.

### Role-Based Authorization

The service currently supports:

| Role | Description |
|---|---|
| USER | Standard registered user |
| ADMIN | Administrative user |

Admin endpoints under `/api/v1/admin/**` require the `ADMIN` role.

New registrations receive the `USER` role by default. Users cannot assign themselves an admin role through the registration API.

## 6. Database Configuration

The current development setup uses an H2 in-memory database.

Example configuration:

```properties
spring.datasource.url=jdbc:h2:mem:mockmule_users
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

H2 Console:

```text
http://localhost:8080/h2-console
```

Use the JDBC URL configured in `application.properties` to connect.

Note: The in-memory database is reset when the application restarts.

## 7. Environment Configuration

The JWT signing secret must be supplied through an environment variable.

Generate a Base64 secret using:

```bash
openssl rand -base64 32
```

Configure the environment variable:

```bash
export JWT_SECRET=<YOUR_BASE64_SECRET>
```

Spring Boot maps this environment variable to the `jwt.secret` property.

**Security guidelines:**

- Never commit the JWT secret to GitHub.
- Do not hardcode production secrets.
- Use a separate secret for each environment.
- Keep the secret private.

## 8. Running the Application

### Prerequisites

- Java 21
- Maven
- Git

### Clone the repository

```bash
git clone https://github.com/AamulSri/MockMule.git
```

Navigate to the service:

```bash
cd MockMule/backend/user-service
```

Configure the JWT secret, then start the application:

```bash
./mvnw spring-boot:run
```

The service uses port `8080` by default unless another port is configured.

## 9. Running Tests

Execute all tests:

```bash
./mvnw clean test
```

Run the tests without cleaning:

```bash
./mvnw test
```

The current test suite covers:

- User registration and duplicate registration
- Login and invalid credentials
- Password verification
- JWT generation and extraction
- Invalid and expired JWTs
- JWT authentication filter behavior
- Missing users
- Request validation
- Protected API access
- Role-based authorization
- Application context loading

Latest verified test result:

```text
Tests run: 23
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

## 10. Current Development Status

**Completed:**

- User registration and login APIs
- Password encryption
- JWT authentication
- Spring Security configuration
- USER and ADMIN roles
- Centralized exception handling
- Request validation
- Unit testing
- Integration testing
- Initial code cleanup

**Future enhancements:**

- Production database integration with PostgreSQL
- User profile management
- Refresh token support
- Password reset functionality
- Account verification
- API Gateway integration

---

**MockMule – Practice with purpose. Perform with confidence.**