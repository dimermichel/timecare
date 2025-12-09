# TimeCare 🏥

> A microservices-based healthcare scheduling platform designed to streamline the connection between patients and medical professionals.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📋 Table of Contents

- [Overview](#-overview)
- [Architecture](#-architecture)
- [Key Features](#-key-features)
- [Technologies](#-technologies)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Project Structure](#-project-structure)
- [Development](#-development)
- [Monitoring & Troubleshooting](#-monitoring--troubleshooting)
- [Recent Updates](#-recent-updates)
- [Contributing](#-contributing)
- [License](#-license)

## 🎯 Overview

TimeCare is an enterprise-grade healthcare scheduling system that demonstrates modern microservices architecture patterns. The platform handles user management across multiple roles (Medic, Patient, Nurse), appointment scheduling via GraphQL, and asynchronous email notifications using event-driven architecture with robust failure handling.

### Business Capabilities

- **Multi-role User Management**: Support for Medics, Patients, and Nurses with role-specific features
- **Appointment Scheduling**: Full CRUD operations with status tracking (Scheduled, Confirmed, Cancelled, Completed)
- **Secure Authentication**: JWT-based authentication and authorization
- **Event-Driven Notifications**: Asynchronous email delivery with Dead Letter Queue (DLQ) handling
- **Audit Logging**: Complete notification history and failure tracking

## 🏗 Architecture

The system follows a distributed microservices architecture with clear separation of concerns:
```

┌─────────────────┐         ┌──────────────────┐         ┌─────────────────────┐
│  GraphQL Client │────────▶│  timecare-core   │────────▶│    PostgreSQL       │
│   (Port 8081)   │         │  (Business Logic)│         │  (Relational Data)  │
└─────────────────┘         └──────────────────┘         └─────────────────────┘
│
▼
┌──────────────────┐
│    RabbitMQ      │
│  (Message Broker)│
└──────────────────┘
│
▼
┌──────────────────┐         ┌─────────────────────┐
│timecare-notify   │────────▶│      MongoDB        │
│  (Email Service) │         │  (Logs & Failures)  │
└──────────────────┘         └─────────────────────┘
```
### Microservices

#### 1. **timecare-core** (Port 8081)
- **Purpose**: Core business logic and API gateway
- **Technology**: Spring Boot + GraphQL
- **Database**: PostgreSQL
- **Responsibilities**:
  - User authentication and authorization
  - Appointment CRUD operations
  - Role-based access control
  - Message publishing to RabbitMQ

#### 2. **timecare-notification** (Port 8082)
- **Purpose**: Asynchronous notification processing
- **Technology**: Spring Boot + AMQP
- **Database**: MongoDB
- **Responsibilities**:
  - Email notification delivery
  - Activity logging
  - DLQ processing for failed messages
  - Failure audit trail

### Infrastructure Components

| Component | Purpose | Port |
|-----------|---------|------|
| **PostgreSQL** | Relational data storage | 5432 |
| **MongoDB** | NoSQL logs and analytics | 27017 |
| **RabbitMQ** | Message broker with DLQ | 5672, 15672 |

## ✨ Key Features

### 🔐 Security
- JWT-based stateless authentication
- Role-based access control (RBAC)
- GraphQL query authorization
- Password encryption with BCrypt

### 📊 Data Management
- JPA/Hibernate with PostgreSQL
- MongoDB for document storage
- Transactional consistency
- Automatic schema management

### 🔔 Notification System
- Asynchronous processing via RabbitMQ
- Automatic retry mechanism
- Dead Letter Queue (DLQ) for failure handling
- Complete notification audit trail
- MongoDB persistence for troubleshooting

### 🎨 GraphQL API
- Type-safe schema
- Query and mutation support
- GraphiQL interface for testing
- Custom exception handling

## 🚀 Technologies

### Backend
- **Java 21** - Latest LTS version
- **Spring Boot 4.0** - Framework
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - ORM layer
- **Spring Data MongoDB** - NoSQL integration
- **Spring AMQP** - RabbitMQ integration
- **Spring GraphQL** - API layer

### Infrastructure
- **Docker & Docker Compose** - Containerization
- **PostgreSQL 16** - Relational database
- **MongoDB 7** - Document database
- **RabbitMQ 3** - Message broker

### Tools & Libraries
- **Lombok** - Boilerplate reduction
- **JJWT** - JWT implementation
- **Maven** - Build tool
- **SLF4J** - Logging

## 🛠 Getting Started

### Prerequisites

- Docker Desktop installed and running
- Docker Compose v3.9+
- 8GB RAM minimum
- Ports available: 5432, 5672, 8081, 8082, 15672, 27017

### Quick Start

1. **Clone the repository**
   ```bash
   git clone https://github.com/dimermichel/timecare.git
   cd timecare
   ```

2. **Start all services**
   ```bash
   docker-compose up -d --build
   ```

3. **Verify services are running**
   ```bash
   docker-compose ps
   ```

4. **Access the application**
    - GraphQL API: http://localhost:8081/graphql
    - RabbitMQ Management: http://localhost:15672 (admin/admin)

### Health Check
```
bash
# Check timecare-core
curl http://localhost:8081/actuator/health

# Check timecare-notification
curl http://localhost:8082/actuator/health
```
## 📖 API Documentation

### Authentication

**Login**
```
http
POST http://localhost:8081/auth/login
Content-Type: application/json

{
  "email": "medic@email.com",
  "password": "medic"
}
```
**Response:**
```
json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```
### Default Credentials

| Role | Email | Password | Specialty/Info |
|------|-------|----------|----------------|
| **Medic** | `medic@email.com` | `medic` | Cardiologist |
| **Patient** | `patient@email.com` | `patient` | Insurance Provider |
| **Nurse** | `nurse@email.com` | `nurse` | Department |

### User Registration

Register new users with role-specific attributes:

**Register a Patient**
```
http
POST http://localhost:8081/register
Content-Type: application/json

{
  "email": "john.doe@email.com",
  "password": "securePassword123",
  "role": "PATIENT",
  "name": "John Doe",
  "dateOfBirth": "1990-05-15",
  "insuranceProvider": "HealthPlus"
}
```
**Register a Medic**
```
http
POST http://localhost:8081/register
Content-Type: application/json

{
  "email": "dr.smith@email.com",
  "password": "securePassword123",
  "role": "MEDIC",
  "name": "Dr. Smith",
  "specialty": "Neurology"
}
```
**Register a Nurse**
```
http
POST http://localhost:8081/register
Content-Type: application/json

{
  "email": "nurse.joy@email.com",
  "password": "securePassword123",
  "role": "NURSE",
  "name": "Joy",
  "department": "Pediatrics"
}
```
### GraphQL Examples

All GraphQL requests require authentication header:
```

Authorization: Bearer <your-jwt-token>
```
**Query: Get All Appointments**
```
graphql
query {
  appointments {
    id
    dateTime
    status
    patient {
      id
      User {
        name
        email
      }
    }
    medic {
      id
      specialty
      User {
        name
      }
    }
  }
}
```
**Mutation: Create Appointment**
```
graphql
mutation {
  createAppointment(input: {
    dateTime: "2025-12-25T10:00"
    doctorId: 1
    patientId: 2
  }) {
    id
    status
    dateTime
  }
}
```
**Mutation: Update Appointment Status**
```
graphql
mutation {
  updateAppointmentStatus(id: 1, status: "CONFIRMED") {
    id
    status
    dateTime
  }
}
```
**Query: Get Patients**
```
graphql
query {
  patients {
    id
    dateOfBirth
    insuranceProvider
    User {
      name
      email
    }
  }
}
```
### Appointment Status Flow
```

SCHEDULED ──▶ CONFIRMED ──▶ COMPLETED
     │
     └──────────▶ CANCELLED
```
## 📂 Project Structure
```

timecare/
├── docker-compose.yml              # Service orchestration
├── rabbitmq/
│   └── definitions.json            # RabbitMQ pre-configuration
├── timecare-core/                  # Core business service
│   ├── src/main/java/.../
│   │   ├── bootstrap/              # Data initialization
│   │   ├── config/                 # Security, RabbitMQ, GraphQL
│   │   ├── controller/             # GraphQL & REST controllers
│   │   ├── dto/                    # Data transfer objects
│   │   ├── exception/              # Custom exceptions
│   │   ├── messaging/              # RabbitMQ producers
│   │   ├── model/                  # JPA entities
│   │   ├── repository/             # Data access layer
│   │   ├── security/               # JWT & authentication
│   │   └── service/                # Business logic
│   ├── src/main/resources/
│   │   ├── graphql/schema.graphqls # GraphQL schema
│   │   └── application.properties  # Configuration
│   ├── Dockerfile
│   └── pom.xml
└── timecare-notification/          # Notification service
    ├── src/main/java/.../
    │   ├── consumer/               # RabbitMQ consumers
    │   ├── dto/                    # Message DTOs
    │   ├── listener/               # DLQ listener
    │   ├── model/                  # MongoDB documents
    │   ├── repository/             # MongoDB repositories
    │   └── service/                # Email logic
    ├── src/main/resources/
    │   └── application.properties
    ├── Dockerfile
    └── pom.xml
```
## 💻 Development

### Local Development (Without Docker)

1. **Start infrastructure services**
   ```bash
   docker-compose up postgres mongo rabbitmq -d
   ```

2. **Run timecare-core**
   ```bash
   cd timecare-core
   ./mvnw spring-boot:run
   ```

3. **Run timecare-notification**
   ```bash
   cd timecare-notification
   ./mvnw spring-boot:run
   ```

### Build Locally
```
bash
# Build core service
cd timecare-core
./mvnw clean package

# Build notification service
cd timecare-notification
./mvnw clean package
```
### Environment Variables

**timecare-core**
```
properties
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/timecare_core_db
SPRING_DATASOURCE_USERNAME=timecare_core_user
SPRING_DATASOURCE_PASSWORD=timecare_core_pass
SPRING_RABBITMQ_HOST=localhost
JWT_SECRET=your-secret-key
JWT_EXPIRATION=3600000
```
**timecare-notification**
```
properties
SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/timecare_notification_db
SPRING_RABBITMQ_HOST=localhost
```
## 📊 Monitoring & Troubleshooting

### RabbitMQ Management Console

Access: http://localhost:15672
- **Username**: `admin`
- **Password**: `admin`

**Features:**
- Monitor queues and exchanges
- View message rates
- Inspect DLQ messages
- Manage bindings

### Check Logs
```
bash
# Core service logs
docker logs timecare_core -f

# Notification service logs
docker logs timecare_notification -f

# RabbitMQ logs
docker logs rabbitmq_shared -f
```
### Database Access

**PostgreSQL**
```
bash
docker exec -it postgres_timecare_core psql -U timecare_core_user -d timecare_core_db
```
**MongoDB**
```
bash
docker exec -it mongo_timecare_notification mongosh -u timecare_notification_user -p timecare_notification_pass
```
### Common Issues

| Issue | Solution |
|-------|----------|
| Port already in use | Change port mappings in `docker-compose.yml` |
| Services not starting | Check `docker-compose logs <service>` |
| Authentication fails | Verify JWT secret configuration |
| Messages stuck in queue | Check RabbitMQ management console |

## 🔄 Recent Updates

### Latest Features

*   **RabbitMQ DLQ Integration** (Latest)
    - Mounted `definitions.json` for custom RabbitMQ configurations
    - Introduced `FailedNotification` model for MongoDB persistence
    - Added `DLQNotificationListener` for dead letter processing
    - Implemented `FailedNotificationService` for failure audit

*   **Enhanced Appointment Management**
    - `updateAppointmentStatus` mutation added
    - Role-based status update restrictions
    - Improved date/time formatting in DTOs
    - Removed deprecated DTOs

*   **Role-Based Access Control (RBAC)**
    - Fine-grained permissions per role
    - Patient data isolation
    - Medical staff access controls
    - GraphQL-level authorization

*   **Authentication System Overhaul**
    - Migrated from basic auth to JWT
    - Replaced Doctor entity with Medic
    - Added Nurse and Patient entities
    - Comprehensive user management

## 🤝 Contributing

Contributions are welcome! This project is part of the FIAP 2025 Java Tech Challenge.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👤 Author

**Michel Maia**
- GitHub: [@dimermichel](https://github.com/dimermichel)
- Project: FIAP 2025 Java Tech Challenge

## 🙏 Acknowledgments

- FIAP 2025 Java Tech Challenge
- Spring Boot Community
- PostgreSQL Team
- RabbitMQ Team
- MongoDB Team

## 📞 Support

For questions or issues:

1. Check existing [issues](https://github.com/dimermichel/timecare/issues)
2. Create a [new issue](https://github.com/dimermichel/timecare/issues/new)
3. Review the [troubleshooting guide](#-monitoring--troubleshooting)

---

**Made with ❤️ for FIAP 2025 Java Tech Challenge**
