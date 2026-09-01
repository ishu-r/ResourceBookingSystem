Resource Booking System

A Spring Boot REST API for managing resources and reservations with JWT-based authentication and role-based authorization.

The system allows administrators to manage bookable resources and reservations, while registered users can view resources, create reservations, view their own reservations, and cancel their reservations according to their permissions.

🚀 Project Overview

The Resource Booking System is designed to provide a secure backend system for managing shared resources such as:

Computer laboratories
Meeting rooms
Classrooms
Equipment
Other bookable resources

The application implements authentication and authorization using Spring Security and JSON Web Tokens (JWT).

Users are divided into two roles:

ADMIN – Can manage resources and reservations.
USER – Can access available resources and manage their own reservations.
✨ Features
🔐 Authentication & Security
User login using username and password
JWT-based authentication
Secure API endpoints
Role-based authorization
ADMIN and USER roles
Invalid credentials are rejected
Unauthorized access is restricted
👨‍💼 Admin Features

Administrators can:

View all resources
View a resource by ID
Create resources
Update resources
Delete resources
View all reservations
View reservation details
Cancel reservations
Manage reservation-related operations
👤 User Features

Registered users can:

Login securely
View available resources
Create reservations
View their own reservations
View reservation details
Cancel their reservations according to authorization rules
📅 Reservation Management

Reservations contain:

User
Resource
Price
Start time
End time
Reservation status

Example reservation statuses include:

PENDING
CANCELLED
🛠️ Technologies Used
Technology	Purpose
Java 17	Programming language
Spring Boot	Backend framework
Spring Web	REST API development
Spring Security	Authentication & authorization
JWT	Token-based authentication
Spring Data JPA	Database interaction
Hibernate	ORM
MySQL	Relational database
Maven	Dependency management and build
Postman	API testing
Git & GitHub	Version control
🏗️ Project Architecture

The project follows a layered architecture:

Client / Postman
       │
       ▼
 REST Controllers
       │
       ▼
    Services
       │
       ▼
 Repositories
       │
       ▼
    MySQL

Security is handled through:

Client
  │
  ▼
Login API
  │
  ▼
Authentication
  │
  ▼
JWT Token
  │
  ▼
Protected APIs
  │
  ▼
Role-based Authorization
📂 Project Structure
ResourceBooking/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/resourcebooking/
│   │   │       │
│   │   │       ├── controller/
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── ResourceController.java
│   │   │       │   └── ReservationController.java
│   │   │       │
│   │   │       ├── entity/
│   │   │       │   ├── User.java
│   │   │       │   ├── Role.java
│   │   │       │   ├── Resource.java
│   │   │       │   ├── Reservation.java
│   │   │       │   └── ReservationStatus.java
│   │   │       │
│   │   │       ├── repository/
│   │   │       │   ├── UserRepository.java
│   │   │       │   ├── ResourceRepository.java
│   │   │       │   └── ReservationRepository.java
│   │   │       │
│   │   │       ├── service/
│   │   │       │   ├── UserService.java
│   │   │       │   ├── ResourceService.java
│   │   │       │   └── ReservationService.java
│   │   │       │
│   │   │       ├── security/
│   │   │       │   ├── CustomUserDetailsService.java
│   │   │       │   ├── JwtAuthenticationFilter.java
│   │   │       │   ├── JwtService.java
│   │   │       │   └── SecurityConfig.java
│   │   │       │
│   │   │       └── exception/
│   │   │           ├── ConflictException.java
│   │   │           └── GlobalExceptionHandler.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/
│
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
└── README.md
🔑 Authentication

The application provides a login endpoint:

POST /auth/login
Request
{
    "username": "admin",
    "password": "password"
}

A successful login returns a JWT token:

{
    "token": "YOUR_JWT_TOKEN"
}

The token must then be supplied when accessing protected endpoints.

Authorization Header
Authorization: Bearer YOUR_JWT_TOKEN
👥 Default Users

The application has been tested with the following users:

Username	Role
admin	ADMIN
user1	USER

For security, passwords and JWT secrets should be supplied through environment variables rather than committed to GitHub.

🔗 Main API Endpoints
Authentication
Method	Endpoint	Purpose
POST	/auth/login	Authenticate user and obtain JWT
Resources
Method	Endpoint	Purpose
GET	/api/resources	Get all resources
GET	/api/resources/{id}	Get resource by ID
POST	/api/resources	Create resource
PUT	/api/resources/{id}	Update resource
DELETE	/api/resources/{id}	Delete resource
Reservations
Method	Endpoint	Purpose
GET	/api/reservations	Get reservations
GET	/api/reservations/{id}	Get reservation by ID
GET	/api/reservations/my	Get current user's reservations
POST	/api/reservations	Create reservation
DELETE	/api/reservations/{id}	Cancel/delete reservation

Exact access permissions depend on the role-based security configuration implemented in the application.

🧪 API Testing

The APIs were tested using Postman.

Testing covered:

Authentication Testing
Valid ADMIN login
Valid USER login
Invalid password
Non-existing username
Resource Testing
Get all resources
Get resource by ID
Create resource
Update resource
Delete resource
Authorization testing for ADMIN and USER
Reservation Testing
Create reservation
Get all reservations
Get reservation by ID
Get current user's reservations
Cancel reservation
Authorization testing
Invalid reservation access

HTTP responses such as:

200 OK
204 No Content
403 Forbidden
404 Not Found

were verified during API testing.

🗄️ Database

The application uses MySQL.

Database:

resourcebooking

The database connection is configured through Spring Boot properties.

For security, sensitive values should be provided through environment variables:

spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

jwt.secret=${JWT_SECRET}
⚙️ Environment Variables

Before running the application, configure:

DB_USERNAME
DB_PASSWORD
JWT_SECRET

Example for Windows/Git Bash:

export DB_USERNAME=root
export DB_PASSWORD=your_database_password
export JWT_SECRET=your_secure_jwt_secret

Do not commit real database passwords or JWT secrets to GitHub.

▶️ How to Run the Project
1. Clone the repository
git clone https://github.com/ishu-r/ResourceBookingSystem.git
2. Open the project

Open the project in:

Spring Tools for Eclipse
Eclipse
IntelliJ IDEA
VS Code
3. Configure MySQL

Create the database:

CREATE DATABASE resourcebooking;

Configure your database credentials using environment variables.

4. Run the application

Using Maven:

./mvnw spring-boot:run

On Windows:

mvnw.cmd spring-boot:run

The application runs on:

http://localhost:8082
🔒 Security Design

The application uses:

Spring Security
       +
JWT Authentication
       +
Role-Based Authorization

The authentication flow is:

Username + Password
        │
        ▼
   /auth/login
        │
        ▼
 Authentication
        │
        ▼
    JWT Token
        │
        ▼
 Authorization Header
        │
        ▼
 Protected API
        │
        ▼
 Role Verification

This prevents unauthorized users from accessing restricted operations.

📊 Example Resource

Example resource:

{
    "name": "Computer Lab 2",
    "type": "LAB",
    "description": "Updated computer laboratory",
    "available": true,
    "id": 2
}
📅 Example Reservation

Example reservation:

{
    "resourceId": 2,
    "price": 400.00,
    "startTime": "2026-10-02T10:00:00",
    "endTime": "2026-10-02T11:00:00"
}

A successful reservation contains information such as:

User
Resource
Status
Price
Start Time
End Time
Reservation ID
🎯 Project Objectives

The main objectives of the project are:

Develop a RESTful resource booking system.
Implement secure user authentication.
Implement JWT-based authorization.
Implement role-based access control.
Provide resource management functionality.
Provide reservation management functionality.
Store application data using MySQL.
Validate and test APIs using Postman.
Follow a layered Spring Boot architecture.
Maintain the project using Git and GitHub.
🔮 Future Enhancements

Possible future improvements include:

Frontend application using React/Angular
Admin dashboard
User dashboard
Resource availability calendar
Advanced search and filtering
Email notifications
Reservation approval workflow
Payment integration
Automatic conflict detection
Reservation history
Reporting and analytics
Docker deployment
Cloud deployment
Swagger/OpenAPI documentation
Automated unit and integration testing
