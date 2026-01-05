# Habit Tracker Backend

A production-style backend service for habit tracking, designed with scalability,
security, and clean API contracts in mind.  
This project focuses on  real-world backend practices , not just CRUD.

---

## 🚀 Overview

The Habit Tracker Backend provides APIs to:
- Manage users and authentication
- Create and track habits
- Record daily habit progress
- Calculate streaks and analytics
- Secure endpoints using JWT-based authentication

The system is  stateless ,  Dockerized , and deployed on  AWS EC2 , with data
persisted in  AWS RDS (MySQL) .

---

## 🧱 Architecture

+---------------------------+
| Client (Browser / |
| Postman / Frontend) |
+-------------+-------------+
|
| HTTP / JSON
v
+---------------------------+
| Swagger UI |
| (/swagger-ui.html) |
+-------------+-------------+
|
v
+------------------------------------------------+
| AWS EC2 Instance |
| |
| +----------------------------------------+ |
| | Docker Container | |
| | | |
| | Spring Boot Application | |
| | - REST APIs | |
| | - JWT Authentication | |
| | - Business Logic | |
| | - Validation & Error Handling | |
| +----------------------------------------+ |
| |
+-----------------------+------------------------+
|
| JDBC
v
+------------------------------------------------+
| AWS RDS (MySQL) |
| - Users |
| - Habits |
| - Habit Records |
| - Goals & Analytics |
+------------------------------------------------+




---

## 🧠 Key Architectural Decisions

### Why Database is NOT inside Docker?
- Docker containers are  ephemeral 
- Database requires  durability and backups 
- AWS RDS provides:
  - Automated backups
  - High availability
  - Persistence independent of EC2 lifecycle

---

### Why JWT instead of Session-based Auth?
- Stateless authentication
- No server-side session storage
- Scales horizontally without shared memory
- Works well with Docker & cloud deployments

---

### What happens if EC2 crashes?
- ❌ Application becomes temporarily unavailable
- ✅ Database (RDS) remains safe
- ✅ New EC2 can be launched and app redeployed

---

### What happens if RDS crashes?
- ❌ Data access stops
- ❌ App cannot serve requests
- ✅ Recoverable via RDS backups / snapshots

---

### What if traffic doubles?
- Stateless backend allows:
  - Horizontal scaling (multiple EC2 instances)
  - Load balancer (future-ready)
- Database can be vertically scaled or upgraded

---

## 🛠 Tech Stack

-  Java 17 
-  Spring Boot 3 
-  Spring Security (JWT) 
-  Spring Data JPA 
-  MySQL (AWS RDS) 
-  Docker 
-  AWS EC2 
-  Swagger OpenAPI 
-  Maven 

---

## 🔐 Authentication

- JWT-based authentication
- Tokens validated via filter
- Stateless security model
- Secured endpoints with role-based access

---

## 📘 API Documentation

Swagger UI is available at:
http://18.204.229.95:8080/swagger-ui/index.html#/

Swagger includes:
- Request/response schemas
- Validation errors
- Authentication details
- HTTP status codes

---

## ⚙️ Running Locally

### Using Docker

```bash
docker-compose up --build
