# Solar CRM - Spring Boot Backend

A mini Solar CRM backend with OAuth2 authentication and lead management.

## Tech Stack
- Java 17, Spring Boot 3.5
- Spring Security + OAuth2 + JWT
- PostgreSQL + JPA/Hibernate
- Maven

## Setup Steps

### 1. Prerequisites
- Java 17+
- PostgreSQL
- Maven

### 2. Database
CREATE DATABASE solar_crm;

### 3. Google OAuth2
- Go to console.cloud.google.com
- Create OAuth2 credentials
- Add redirect URI: http://localhost:8080/login/oauth2/code/google

### 4. Configure application.properties
spring.datasource.password=YOUR_DB_PASSWORD
spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID_HERE
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET_HERE

### 5. Run
mvn spring-boot:run

## Authentication
Visit: http://localhost:8080/oauth2/authorization/google
Copy the JWT token from the response and use it as Bearer Token in all API calls.

## API Endpoints

### Leads
| Method | URL | Description |
|--------|-----|-------------|
| POST | /api/leads | Create a new lead |
| GET | /api/leads | Get all leads |
| PUT | /api/leads/{id}/status | Update lead status |
| PUT | /api/leads/{id}/assign | Assign lead to agent |

### Dashboard
| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/dashboard | Get stats summary |

## Lead Status Workflow
NEW → CONTACTED → SITE_VISIT → QUOTATION_SENT → WON/LOST

## Running Tests
mvn test