# Clinio

Self-hosted clinic appointment scheduling and patient records platform built with Java 21 and Spring Boot 3.

Clinio gives small medical practices, dental offices, and therapy clinics a modern way to manage patients, book appointments, maintain visit notes, and handle basic billing without expensive SaaS EHR systems.

## Features

- Multi-clinic tenant model with role-based access control
- Patient demographics, insurance, and duplicate detection
- Provider schedules with conflict-aware appointment booking
- Waiting room check-in queue
- SOAP visit notes with templates and signing
- Basic invoicing and payment tracking
- Email appointment reminders

## Stack

- Java 21, Spring Boot 3.3, Spring Security 6, Spring Data JPA
- PostgreSQL with Flyway migrations
- Thymeleaf staff UI, REST API with OpenAPI
- Docker Compose for local PostgreSQL and Mailhog

## Local setup

1. Start dependencies:

```bash
docker compose up -d
```

2. Copy environment template:

```bash
cp .env.example .env
```

3. Run the application:

```bash
./mvnw spring-boot:run
```

4. Health check:

```bash
curl http://localhost:8080/api/health
```

## RBAC overview

| Role | Patients | Appointments | Visit notes | Invoices |
|------|----------|--------------|-------------|----------|
| Admin | full | full | full | full |
| Provider | own panel | own schedule | own notes | read |
| Nurse | read/update | read/update | read | none |
| Receptionist | register/search | book/check-in | none | read |
| Billing | demographics only | read | none | full |

## Disclaimer

Clinio is a demonstration project. It is not a certified EHR and does not claim HIPAA compliance. Do not use with real protected health information in production.

## License

MIT
