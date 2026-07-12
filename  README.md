# Banking API

A RESTful Banking API built with Spring Boot.

## Features

- User registration
- User login with JWT authentication
- Create bank accounts
- View account details
- Deposit money
- Withdraw money
- Transfer money between accounts
- Global exception handling
- API documentation with Swagger
- Unit tests with JUnit and Mockito

## Technologies

- Java 21
- Spring Boot 3
- Spring Security
- JWT (JSON Web Token)
- Spring Data JPA
- Hibernate
- PostgreSQL
- Flyway
- Maven
- Swagger / OpenAPI
- JUnit 5
- Mockito

## Project Structure

```
src
├── auth
├── account
├── transaction
├── common
└── resources
```

## API Endpoints

### Authentication

| Method | Endpoint |
|--------|----------|
| POST | /api/auth/register |
| POST | /api/auth/login |

### Accounts

| Method | Endpoint |
|--------|----------|
| POST | /api/accounts |
| GET | /api/accounts/{id} |

### Transactions

| Method | Endpoint |
|--------|----------|
| POST | /api/transactions/deposit |
| POST | /api/transactions/withdraw |
| POST | /api/transactions/transfer |

## Authentication

Login returns a JWT token.

Example:

```text
Authorization: Bearer <your-jwt-token>
```

All protected endpoints require this header.

## Swagger

After starting the application:

```
http://localhost:8080/swagger-ui/index.html
```

## Running the application

```bash
git clone https://github.com/YOUR_GITHUB_USERNAME/BankingApi.git
cd BankingApi
./mvnw spring-boot:run
```

## Tests

Run all tests:

```bash
./mvnw test
```

## Author

Marina Likhareva