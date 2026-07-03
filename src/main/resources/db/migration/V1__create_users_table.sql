CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       date_of_birth DATE NOT NULL,
                       customer_number VARCHAR(20) NOT NULL UNIQUE,
                       phone_number VARCHAR(30),
                       email VARCHAR(120) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL,
                       enabled BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP
);