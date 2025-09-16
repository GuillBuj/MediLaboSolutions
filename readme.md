# 🏥 MediLaboSolutions
![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![SpringCloud](https://img.shields.io/badge/Spring%20Cloud-2022.0.4-lightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)
![MongoDB](https://img.shields.io/badge/MongoDB-6.0-orange)
![Docker](https://img.shields.io/badge/Docker-Ready-blue).

> A microservices-based application for Type 2 Diabetes risk screening, developed with Spring Boot and a cloud-native architecture. Built for healthcare professionals to manage patient data, clinical notes, and generate automated risk assessments.

---

## 🚀 Project Overview

This project is a Type 2 Diabetes risk screening application developed for healthcare clinics and private practices. The application enables professionals to:

- Securely manage patient records

- Record and access clinical notes from practitioners

- Generate automated diabetes risk assessments based on patient data and medical notes

- Access a clean, intuitive web interface

---

## ⚙️ Tech Stack
- Java
- Spring Boot
- Spring Cloud Gateway
- Maven
- Docker
- HTML
- Thymeleaf
- Bootstrap
- JUnit 5
- JaCoCo
- Surefire

---

## 🏗️ Architecture
The application follows a microservices architecture with the following components:
- patient-service: patients data management(MySQL database)
- note-service: notes data mangement(MongoDB database)
- report-service: diabetes risk assesment based on patient data and notes
- gateway: central gateway with routing and security
- frontend-service: web interface
- eureka-server: for dynamic service location.

---

## 🐳 Quick Start with Docker Compose
The easiest way to run the entire ecosystem is with Docker Compose.

- Clone the repository:
  git clone https://github.com/GuillBuj/MediLaboSolutions.git
  cd MediLaboSolutions

- Start all services:
  docker-compose up -d

- Access the application:
  -Frontend Application: http://localhost:8084
  -API Gateway: http://localhost:8083

## 🖥️ Quick Start Locally (For Development)
For developers who want to run and debug the services directly on their machine without Docker.

Prerequisites
Java 21 (or later) installed and JAVA_HOME configured.
Maven 3.9.6+ installed.
A local MySQL server running (or adjust application.properties for your connection string).
A local MongoDB server running (or adjust application.properties for your connection string).

Steps

- Clone the repository:
  git clone https://github.com/GuillBuj/MediLaboSolutions.git
  cd MediLaboSolutions

- Build the project and install all dependencies:
  It is highly recommended to run a clean install first. This ensures a fresh build, downloads all dependencies, and installs the modules to your local Maven repository.
  mvn clean install

- Start the databases.
  You can use your own installations or start just the databases from the Docker Compose file(docker-compose up mysql-db mongodb -d)

- Run the microservices in the following order:
    - eureka-server
    - the core services(patient-service, note-service and report-service)
    - gateway-service
    - frontend-service.

- Access the application:
    - Frontend Application: http://localhost:8084
    - API Gateway: http://localhost:8083
    - Report Service API: http://localhost:8082
    - Note Service API: http://localhost:8081
    - Patient Service API: http://localhost:8080
    - Eureka Server: http://localhost:8761

## 🚀 Live Artifacts

- 📈 **Code Coverage (JaCoCo)**:  
  [📊 View Report](https://GuillBuj.github.io/MediLaboSolutions/jacoco/index.html)

- ✅ **Unit Test Report (Surefire)**:  
[🧪 patient-service](https://GuillBuj.github.io/MediLaboSolutions/target-patient/surefire-report/surefire.html)
[🧪 note-service](https://GuillBuj.github.io/MediLaboSolutions/target-note/surefire-report/surefire.html)
[🧪 report-service](https://GuillBuj.github.io/MediLaboSolutions/target-report/surefire-report/surefire.html)

- 📚 **Javadoc Documentation**:  
  [📘 Browse Javadoc](https://GuillBuj.github.io/MediLaboSolutions/docs/index.html)

---
## 🌱 Green Code Recommendations

**🚀 Recommended Practices:**

- **Optimize Algorithm Complexity**: Prefer O(n) over O(n²) algorithms
- **Implement Efficient Caching**: Reduce redundant database queries and API calls
- **Use Connection Pooling**: Optimize database connections
- **Enable GZIP Compression**: Reduce network payload size
- **Implement Pagination**: Limit data transfer for large datasets
- **Use Lightweight Base Images**: Optimize Docker container size
- **Clean Unused Dependencies**: Reduce application footprint
- **Configure Resource Limits**: Set memory and CPU limits in containers
- **Implement Auto-scaling**: Scale services based on actual demand
- **Schedule Batch Processing**: Run heavy jobs during off-peak hours
- **Monitor Energy Consumption**: Track resource usage metrics
- **Use CDN for Static Assets**: Reduce latency and bandwidth
- **Optimize Database Indexing**: Improve query performance
- **Implement Sleep Modes**: Reduce resource consumption during inactivity
- **Choose Green Hosting Providers**: Prefer carbon-neutral cloud providers

*These practices help reduce the digital carbon footprint while maintaining application performance and reliability.*

---
### 🗂️ Database & 3NF Principles

The ideal database schema aims to comply with **Third Normal Form (3NF)** principles to ensure data integrity.

**Theoretical 3NF Principles (Our Guideline):**
- **Prime Attribute Dependency:** Non-key attributes should depend only on the primary key.
- **Transitive Dependency Removal:** Non-key attributes should be independent of each other.
- **Data Atomicity:** Values stored in columns should be atomic.
- **Referential Integrity:** Relationships should be enforced via foreign keys.
