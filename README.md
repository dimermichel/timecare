# TimeCare 🏥

> A microservices-based healthcare scheduling platform designed to streamline the connection between patients and medical professionals.

[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![GraphQL](https://img.shields.io/badge/GraphQL-API-E10098.svg)](https://graphql.org/)
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
- **Secure Authentication**: JWT-based authentication and authorization with role-based access control
- **Event-Driven Notifications**: Asynchronous email delivery with Dead Letter Queue (DLQ) handling
- **Audit Logging**: Complete notification history and failure tracking with MongoDB persistence
- **GraphQL API**: Type-safe, flexible API with GraphiQL playground

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
│   + DLQ Support  │
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
- **Technology**: Spring Boot 4.0 + GraphQL + Spring Security
- **Database**: PostgreSQL 16
- **Responsibilities**:
  - JWT-based authentication and authorization
  - Appointment CRUD operations with role-based access
  - User registration with role-specific profiles
  - Message publishing to RabbitMQ
  - GraphQL API endpoint with GraphiQL playground

#### 2. **timecare-notification** (Port 8082)
- **Purpose**: Asynchronous notification processing
- **Technology**: Spring Boot 4.0 + Spring AMQP
- **Database**: MongoDB 7
- **Responsibilities**:
  - Email notification delivery (with simulated SMTP)
  - Email activity logging
  - Dead Letter Queue (DLQ) processing for failed messages
  - Failure audit trail and retry tracking

### Infrastructure Components

| Component | Purpose | Ports |
|-----------|---------|-------|
| **PostgreSQL** | Relational data storage for core entities | 5432 |
| **MongoDB** | NoSQL storage for logs and failed notifications | 27017 |
| **RabbitMQ** | Message broker with DLQ and management UI | 5672, 15672 |

## ✨ Key Features

### 🔐 Security
- JWT-based stateless authentication
- Role-based access control (RBAC) with fine-grained permissions
- GraphQL query-level authorization
- Password encryption with BCrypt
- Patient data isolation (patients can only access their own data)
- Medical staff permissions for appointment management

### 📊 Data Management
- JPA/Hibernate with PostgreSQL for transactional data
- MongoDB for document storage and audit trails
- Automatic schema management with Hibernate DDL
- HikariCP connection pooling

### 🔔 Notification System
- Asynchronous processing via RabbitMQ
- Dead Letter Queue (DLQ) for failed message handling
- Automatic retry mechanism with exponential backoff
- Complete notification audit trail in MongoDB
- Simulated SMTP with failure injection (every 5th email fails for testing)
- Persistent storage of failed notifications for troubleshooting

### 🎨 GraphQL API
- Type-safe schema with enums for appointment status
- Query and mutation support
- GraphiQL interface for testing (http://localhost:8081/graphql)
- Custom exception handling with `@GraphQLExceptionHandler`
- Support for complex nested queries

## 🚀 Technologies

### Backend
- **Java 25** - Latest JDK with modern language features
- **Spring Boot 4.0** - Application framework
- **Spring Security 7.0** - Authentication & Authorization
- **Spring Data JPA 4.0** - ORM layer with Hibernate 7.1
- **Spring Data MongoDB 5.0** - NoSQL integration
- **Spring AMQP 4.0** - RabbitMQ integration
- **Spring GraphQL 2.0** - API layer
- **Jakarta EE** - Enterprise Java specifications

### Infrastructure
- **Docker & Docker Compose** - Containerization and orchestration
- **PostgreSQL 16** - Relational database with ACID compliance
- **MongoDB 7** - Document database for flexible schema
- **RabbitMQ 3** - Message broker with AMQP protocol

### Tools & Libraries
- **Lombok 1.18.42** - Boilerplate reduction
- **JJWT 0.11.5** - JWT implementation
- **Maven** - Build and dependency management
- **SLF4J + Logback** - Logging framework
- **HikariCP 7.0** - JDBC connection pooling

## 🛠 Getting Started

### Prerequisites

- Docker Desktop installed and running
- Docker Compose v3.9+
- 8GB RAM minimum recommended
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
   All services should show "Up" status.

4. **Access the application**
    - GraphQL Playground: http://localhost:8081/graphql
    - RabbitMQ Management: http://localhost:15672 (admin/admin)
    - Core API: http://localhost:8081
    - Notification Service: http://localhost:8082

### Health Check
```
bash
# Check timecare-core
curl http://localhost:8081/actuator/health

# Check timecare-notification
curl http://localhost:8082/actuator/health

# Check RabbitMQ
curl http://localhost:15672/api/overview
```
## 📖 API Documentation

### Authentication

**Login Endpoint**
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

| Role | Email | Password | Additional Info |
|------|-------|----------|-----------------|
| **Medic** | `medic@email.com` | `medic` | Specialty: Cardiologist |
| **Patient** | `patient@email.com` | `patient` | Insurance: HealthPlus |
| **Nurse** | `nurse@email.com` | `nurse` | Department: Emergency |

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
  "insuranceProvider": "HealthPlus Insurance"
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
  "name": "Dr. Emily Smith",
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
  "name": "Joy Williams",
  "department": "Pediatrics"
}
```
### GraphQL Examples

**Important:** All GraphQL requests require authentication header:
```

Authorization: Bearer <your-jwt-token>
```
**Query: Get All Appointments**
```
graphql
query {
  appointments(justUpcoming: true) {
    id
    dateTime
    status
    patient {
      id
      dateOfBirth
      insuranceProvider
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
        email
      }
    }
  }
}
```
**Query: Get Appointments by Patient**
```
graphql
query {
  appointmentsByPatient(patientId: 1) {
    id
    dateTime
    status
    medic {
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
    dateTime: "2025-12-25T10:00:00"
    medicId: 1
    patientId: 2
  }) {
    id
    status
    dateTime
    patient {
      User {
        name
      }
    }
    medic {
      User {
        name
      }
    }
  }
}
```
**Mutation: Update Appointment**
```
graphql
mutation {
  updateAppointment(
    id: 1
    input: {
      dateTime: "2025-12-26T14:30:00"
      medicId: 1
      patientId: 2
    }
  ) {
    id
    dateTime
    status
  }
}
```
**Mutation: Update Appointment Status**
```
graphql
mutation {
  updateAppointmentStatus(id: 1, status: CONFIRMED) {
    id
    status
    dateTime
  }
}
```
**Query: Get All Medics**
```
graphql
query {
  medics {
    id
    specialty
    User {
      id
      name
      email
    }
  }
}
```
**Query: Get All Patients**
```
graphql
query {
  patients {
    id
    dateOfBirth
    insuranceProvider
    User {
      id
      name
      email
    }
  }
}
```
**Query: Get All Nurses**
```
graphql
query {
  nurses {
    id
    department
    User {
      id
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
### Role-Based Access Control

| Action | Patient | Medic | Nurse |
|--------|---------|-------|-------|
| View own appointments | ✅ | N/A | N/A |
| View all appointments | ❌ | ✅ | ✅ |
| Create appointment | ✅ (own only) | ✅ | ✅ |
| Update appointment | ✅ (own only) | ✅ | ✅ |
| Update status | ✅ (own only) | ✅ | ✅ |

## 📂 Project Structure
```

timecare/
├── docker-compose.yml              # Service orchestration
├── postman/                        # Postman collection for API testing
│   └── TimeCare.postman_collection.json
├── rabbitmq/
│   └── definitions.json            # RabbitMQ queues, exchanges, and DLQ config
├── timecare-core/                  # Core business service
│   ├── src/main/java/com/michelmaia/timecare_core/
│   │   ├── bootstrap/              # Data initialization (default users)
│   │   ├── config/
│   │   │   ├── GraphQLExceptionHandler.java
│   │   │   ├── RabbitMQConfig.java
│   │   │   └── SecurityConfig.java
│   │   ├── controller/             # GraphQL & REST controllers
│   │   │   ├── AppointmentGraphQLController.java
│   │   │   ├── AuthController.java
│   │   │   ├── MedicGraphQLController.java
│   │   │   ├── NurseGraphQLController.java
│   │   │   ├── PatientGraphQLController.java
│   │   │   └── RegistrationController.java
│   │   ├── dto/                    # Data transfer objects
│   │   │   ├── AppointmentInputDTO.java
│   │   │   └── RegistrationDTO.java
│   │   ├── exception/              # Custom exceptions
│   │   │   └── AccessDeniedGraphQLException.java
│   │   ├── messaging/              # RabbitMQ producers
│   │   │   ├── dto/NotificationMessage.java
│   │   │   └── NotificationProducer.java
│   │   ├── model/                  # JPA entities
│   │   │   ├── Appointment.java
│   │   │   ├── AppointmentStatus.java (enum)
│   │   │   ├── Medic.java
│   │   │   ├── Nurse.java
│   │   │   ├── Patient.java
│   │   │   ├── Role.java (enum)
│   │   │   └── User.java
│   │   ├── repository/             # Data access layer
│   │   │   ├── AppointmentRepository.java
│   │   │   ├── MedicRepository.java
│   │   │   ├── NurseRepository.java
│   │   │   ├── PatientRepository.java
│   │   │   └── UserRepository.java
│   │   ├── security/               # JWT & authentication
│   │   │   ├── CurrentUser.java
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   └── JwtUtil.java
│   │   └── service/                # Business logic
│   │       ├── AppointmentService.java
│   │       ├── MedicService.java
│   │       ├── NurseService.java
│   │       ├── PatientService.java
│   │       ├── RegistrationService.java
│   │       └── UserService.java
│   ├── src/main/resources/
│   │   ├── graphql/
│   │   │   └── squema.graphqls     # GraphQL schema definition
│   │   └── application.properties  # Configuration
│   ├── Dockerfile
│   └── pom.xml
└── timecare-notification/          # Notification service
    ├── src/main/java/com/michelmaia/timecare_notification/
    │   ├── config/
    │   │   ├── MongoConfig.java
    │   │   └── RabbitConsumerConfig.java
    │   ├── consumer/
    │   │   └── EmailNotificationConsumer.java
    │   ├── dto/
    │   │   └── NotificationMessage.java (record)
    │   ├── listener/
    │   │   └── DLQNotificationListener.java
    │   ├── model/                  # MongoDB documents
    │   │   ├── EmailLog.java
    │   │   └── FailedNotification.java
    │   ├── repository/             # MongoDB repositories
    │   │   ├── EmailLogRepository.java
    │   │   └── FailedNotificationRepository.java
    │   └── service/
    │       ├── EmailSenderService.java
    │       └── FailedNotificationService.java
    ├── src/main/resources/
    │   ├── application.properties
    │   └── application-dev.properties
    ├── Dockerfile
    └── pom.xml
```
## 💻 Development

### Local Development (Without Docker)

1. **Start infrastructure services only**
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
   ./mvnw spring-boot:run -Dspring.profiles.active=dev
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
SPRING_RABBITMQ_PORT=5672
SPRING_RABBITMQ_USERNAME=admin
SPRING_RABBITMQ_PASSWORD=admin
JWT_SECRET=your-super-secret-key-that-is-at-least-256-bits-long-for-HS256
JWT_EXPIRATION=3600000
```
**timecare-notification**
```
properties
SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/timecare_notification_db
SPRING_RABBITMQ_HOST=localhost
SPRING_RABBITMQ_PORT=5672
SPRING_RABBITMQ_USERNAME=admin
SPRING_RABBITMQ_PASSWORD=admin
```
### Running Tests
```
bash
# Run tests for core service
cd timecare-core
./mvnw test

# Run tests for notification service
cd timecare-notification
./mvnw test
```
## 📊 Monitoring & Troubleshooting

### RabbitMQ Management Console

Access: **http://localhost:15672**
- **Username**: `admin`
- **Password**: `admin`

**Features:**
- Monitor `email.notifications.queue` and `email.notifications.dlq`
- View message rates and delivery status
- Inspect DLQ messages for failures
- Manage exchanges (`email.notifications.exchange`, `email.notifications.dlx`)
- Visualize bindings and routing

### Check Logs
```
bash
# Core service logs
docker logs timecare_core -f

# Notification service logs
docker logs timecare_notification -f

# RabbitMQ logs
docker logs rabbitmq_shared -f

# PostgreSQL logs
docker logs postgres_timecare_core -f

# MongoDB logs
docker logs mongo_timecare_notification -f
```
### Database Access

**PostgreSQL**
```
bash
docker exec -it postgres_timecare_core psql -U timecare_core_user -d timecare_core_db
```
**Common PostgreSQL queries:**
```
sql
-- View all users
SELECT * FROM users;

-- View appointments with patient/medic details
SELECT a.id, a.date_time, a.status, 
       p.name as patient_name, m.name as medic_name
FROM appointments a
JOIN patients pt ON a.patient_id = pt.id
JOIN users p ON pt.user_id = p.id
JOIN medics md ON a.medic_id = md.id
JOIN users m ON md.user_id = m.id;
```
**MongoDB**
```
bash
docker exec -it mongo_timecare_notification mongosh -u timecare_notification_user -p timecare_notification_pass
```
**Common MongoDB queries:**
```
javascript
// Switch to database
use timecare_notification_db

// View email logs
db.emailLog.find().pretty()

// View failed notifications
db.failedNotification.find().pretty()

// Count failures
db.failedNotification.countDocuments()
```
### Common Issues

| Issue | Solution |
|-------|----------|
| Port already in use | Change port mappings in `docker-compose.yml` |
| Services not starting | Check `docker-compose logs <service>` for errors |
| Authentication fails | Verify JWT secret configuration matches in both services |
| Messages stuck in queue | Check RabbitMQ management console for bindings |
| MongoDB connection error | Ensure MongoDB URI is correct in application.properties |
| GraphQL errors | Check GraphiQL playground for schema validation |

### Testing Notification Failures

The `EmailSenderService` simulates SMTP failures by throwing an exception every 5th email. To test DLQ handling:

1. Create 5 appointments rapidly
2. Check RabbitMQ management - every 5th message goes to DLQ
3. Query MongoDB `failedNotification` collection to see failures
4. Check logs for DLQ processing

## 🔄 Recent Updates

### Latest Features

#### **Postman Collection Integration** (December 13, 2025)
- Added comprehensive Postman collection with detailed documentation
- Organized endpoints into folders (Authentication, User Management, GraphQL)
- Included example requests for all major API operations
- Added environment variables for easy configuration

#### **RabbitMQ and MongoDB Migration** (December 13, 2025)
- Renamed queues for clarity: `email.notifications.queue`, `email.notifications.dlq`
- Introduced `RabbitConsumerConfig` for custom message converters with type mapping
- Enhanced MongoDB configuration with `MongoConfig` for direct URI handling
- Improved appointment date formatting with `DateTimeFormatter`
- Refactored `NotificationMessage` to use Java records for immutability
- Updated Docker health checks for improved container orchestration

#### **Dead Letter Queue (DLQ) Integration** (December 9, 2025)
- Mounted `definitions.json` for declarative RabbitMQ configuration
- Introduced `FailedNotification` model for MongoDB persistence
- Added `DLQNotificationListener` for dead letter processing
- Implemented `FailedNotificationService` for comprehensive failure audit
- Automatic retry tracking with timestamp and reason

#### **Enhanced Appointment Management** (December 9, 2025)
- Added `updateAppointmentStatus` mutation for status transitions
- Implemented role-based status update restrictions
- Improved date/time formatting in DTOs with ISO-8601 support
- Renamed `doctorId` to `medicId` for consistency across the codebase
- Enhanced validation for appointment inputs

#### **Role-Based Access Control (RBAC)** (December 8, 2025)
- Fine-grained permissions per role (Medic, Nurse, Patient)
- Patient data isolation - patients can only access their own data
- Medical staff access controls with cross-patient visibility
- GraphQL-level authorization with `@PreAuthorize`
- Custom `AccessDeniedGraphQLException` for security violations

#### **Authentication System Overhaul** (December 5, 2025)
- Migrated from basic authentication to JWT-based authentication
- Replaced `Doctor` entity with `Medic` for medical terminology consistency
- Added `Nurse` and `Patient` entities with role-specific fields
- Implemented comprehensive user management with registration endpoints
- Transactional user registration with profile creation

### Migration from v1.0

If you're upgrading from an earlier version:

1. **Database Schema**: Run migrations for new `medic`, `nurse`, and `patient` tables
2. **API Changes**: Update GraphQL queries to use `medicId` instead of `doctorId`
3. **Authentication**: Migrate from basic auth to JWT tokens
4. **RabbitMQ**: Update queue names to new convention
5. **MongoDB**: Ensure `FailedNotification` collection is created

## 🤝 Contributing

Contributions are welcome! This project is part of the **FIAP 2025 Java Tech Challenge**.

### How to Contribute

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Follow Java code conventions
- Write unit tests for new features
- Update documentation for API changes
- Use Lombok annotations to reduce boilerplate
- Follow RESTful principles for new endpoints
- Ensure GraphQL schema is properly documented

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👤 Author

**Michel Maia**
- GitHub: [@dimermichel](https://github.com/dimermichel)
- Project: FIAP 2025 Java Tech Challenge
- LinkedIn: [Connect with me](https://www.linkedin.com/in/dimermichel)

## 🙏 Acknowledgments

- **FIAP 2025** - Java Tech Challenge sponsor and organizer
- **Spring Boot Community** - Excellent framework and documentation
- **PostgreSQL Team** - Robust relational database
- **RabbitMQ Team** - Reliable message broker
- **MongoDB Team** - Flexible NoSQL solution
- **GraphQL Foundation** - Modern API technology

## 📞 Support

For questions or issues:

1. Check existing [GitHub issues](https://github.com/dimermichel/timecare/issues)
2. Create a [new issue](https://github.com/dimermichel/timecare/issues/new) with detailed description
3. Review the [troubleshooting guide](#-monitoring--troubleshooting)
4. Check the [Postman collection](postman/TimeCare.postman_collection.json) for API examples

### Useful Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [GraphQL Java Documentation](https://www.graphql-java.com/)
- [RabbitMQ Tutorials](https://www.rabbitmq.com/getstarted.html)
- [MongoDB Documentation](https://docs.mongodb.com/)

---

**Made with ❤️ for FIAP 2025 Java Tech Challenge**
