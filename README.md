# E-Commerce Microservices

A Spring Boot Microservices-based E-Commerce application built using Spring Cloud, Eureka Service Discovery, API Gateway, Config Server, Kafka, MySQL, and Docker.

---

# Architecture

```text
                        Client
                           |
                           |
                    API Gateway
                        :8080
                           |
                           |
                    Eureka Server
                        :8761
                           |
        ---------------------------------------
        |                 |                  |
        |                 |                  |
 Product Service    Order Service    Notification Service
      :8081              :8082              :8083
        |                 |
        |                 |
      MySQL            MySQL

                    Config Server
                        :8888
                           |
                           |
                     Git Repository
```

---

# Implemented Components

## 1. Eureka Service Registry

Service discovery mechanism used to register and discover all microservices dynamically.

### Registered Services

* API Gateway
* Product Service
* Order Service
* Notification Service
* Config Server

### Eureka Dashboard

```
http://localhost:8761
```

---

## 2. Spring Cloud Config Server

Centralized configuration management for all services.

### Config Server Port

```
8888
```

### Configuration Source

GitHub Repository

### Config Structure

```text
configs/
├── application.yml
├── api-gateway.yml
├── product-service.yml
├── order-service.yml
└── notification-service.yml
```

### Verify Config

```text
http://localhost:8888/product-service/default
```

---

## 3. API Gateway

Single entry point for all client requests.

### Port

8080

### Routes

#### Product Service

/products/**

Routes to:

PRODUCT-SERVICE

#### Order Service

/orders/**

Routes to:

ORDER-SERVICE

#### Notification Service

/notifications/**

Routes to:

NOTIFICATION-SERVICE

### Example

http://localhost:8080/products/1


# Services

## Product Service

### Port

8081

### Responsibilities

* Product Management
* Product CRUD Operations
* Product Information Retrieval

### Database

MySQL
---

## Order Service

### Port

8082

### Responsibilities

* Order Placement
* Order Management
* Product Validation (Upcoming via Feign Client)

### Database

MySQL
---

## Notification Service

### Port

8083

### Responsibilities

* Consume Order Events
* Save Notifications in DataBase

### Database

MySQL
---

# Configuration Management

Each service contains only:

```yaml
spring:
  application:
    name: service-name

  config:
    import: optional:configserver:http://localhost:8888
```

All other configurations are fetched from Config Server.

---
# Kafka Setup

Kafka is running locally using Docker.

### Future Flow

Order Service
      |
      |
Publish Event
      |
      V
Kafka Topic
      |
      V
Notification Service

Example Event:

OrderPlacedEvent

---

# Technology Stack

## Backend

* Java 21
* Spring Boot
* Spring Cloud

## Spring Cloud Components

* Eureka Server
* Config Server
* API Gateway
* OpenFeign 
* Resilience4j

## Database

* MySQL

## Messaging

* Apache Kafka

## Containerization

* Docker
* Docker Compose

## Documentation

* Swagger/OpenAPI

## Build Tool

* Maven
---

# Repository Structure

ecommerce-microservices

├── configs
│   ├── application.yml
│   ├── product-service.yml
│   ├── order-service.yml
│   ├── notification-service.yml
│   └── api-gateway.yml
│
├── service-registry
├── config-server
├── api-gateway
├── product-service
├── order-service
├── notification-service
│
├── docs
└── README.md

---

# Local Startup Order

### 1. Start Docker

```bash
docker compose up -d

### 2. Start Eureka Server

http://localhost:8761

### 3. Start Config Server

http://localhost:8888


### 4. Start Product Service

Port 8081

### 5. Start Order Service

Port 8082

### 6. Start Notification Service

Port 8083

### 7. Start API Gateway

Port 8080

---

# Current Status

## Completed

* Eureka Service Discovery
* API Gateway Routing
* Centralized Configuration using Config Server
* GitHub Config Repository
* Product Service Registration
* Order Service Registration
* Notification Service Registration
* Gateway Routing via Service Discovery
* Local Kafka Setup
* MySQL Integration
* OpenFeign Integration
* Inter-Service Communication
* Resilience4j Circuit Breaker
* Kafka Event Publishing
* Kafka Event Consumption
* Swagger Aggregation

---

# Author

Kolle Anil Kumar

Java | Spring Boot | Microservices | REST API's | Kafka | System Design | Angular
