🧘‍♀️ Wellness Marketplace – Backend

Spring Boot backend for the Wellness Marketplace for Alternative Therapies.
This project provides a secure, scalable REST API to support therapy booking, wellness product marketplace, AI-based recommendations, real-time communication, and admin management.

🚀 Features
🔐 Authentication & Authorization

User registration & login using JWT

Role-based access control:

PATIENT

PRACTITIONER

ADMIN

Secure password hashing using BCrypt

👩‍⚕️ Practitioner & Patient Management

Practitioner profile creation & updates

Patient profile management

Admin user management (list, delete users)

📅 Therapy Booking & Scheduling

Book therapy sessions

Reschedule & cancel appointments

Validation rules:

Only Mon–Sat

8:00 AM – 10:00 PM

Upcoming & history sessions

Calendar-ready APIs

Session reminders (scheduler)

🧠 AI Recommendation Engine

Rule-based AI for therapy suggestions

Symptom → category → therapy mapping

Integration with OpenFDA (drug warnings)

Recommendation history per user

Ethical AI (no diagnosis, no prescriptions)

🛒 Product Marketplace

Product listing with categories

Search & advanced filters

Pagination & sorting

Stock management

Admin product management (add/update/delete)

🛍️ Cart & Orders

Add/update/remove cart items

Quantity & category limits

Place orders from cart

Order lifecycle:

PLACED → SHIPPED → DELIVERED

RETURN / REPLACEMENT / REFUND flows

Order tracking API

💳 Payments (Mock Integration)

Payment entity & status tracking

Payment ↔ order synchronization

Refund handling

💬 Real-Time Features

Real-time chat using WebSockets + STOMP

Live order updates

Secure, authenticated messaging

📊 Admin Module

Order management

Product management

User management

Analytics-ready architecture

🧰 Tech Stack
Layer	Technology
Backend	Spring Boot (Java 17)
Security	Spring Security, JWT
ORM	JPA / Hibernate
Database	MySQL
Real-time	WebSockets + STOMP
AI / APIs	OpenFDA, Rule-based AI
Build Tool	Maven
API Testing	Postman
📂 Project Structure (Simplified)
src/main/java/com/wellness/backend
├── controller
│   ├── auth
│   ├── therapy
│   ├── product
│   ├── order
│   ├── admin
│   ├── recommendation
│   └── chat
├── service
├── repository
├── model
├── dto
├── security
├── integration
│   └── openfda
├── scheduler
└── config

⚙️ Quick Start
✅ Prerequisites

Java 17

Maven

MySQL (local / RDS / Docker)

Postman (for API testing)

🗄️ Database Setup

Create the database:

CREATE DATABASE wellness_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

🔧 Configure Application

Update src/main/resources/application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/wellness_db
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=your_jwt_secret_key
jwt.expiration=3600000

▶️ Run the Application
mvn spring-boot:run


Server starts at:

http://localhost:8080

🧪 API Testing (Postman)
Authentication

POST /api/auth/register

POST /api/auth/login

Therapy

POST /api/sessions/book

PUT /api/sessions/{id}/reschedule

PUT /api/sessions/{id}/cancel

GET /api/sessions/upcoming

GET /api/sessions/history

Products

GET /api/products

GET /api/products/category

GET /api/products/search

Cart

POST /api/cart/add

GET /api/cart

PUT /api/cart/update

DELETE /api/cart/remove/{productId}

Orders

POST /api/orders/place

GET /api/orders/my

GET /api/orders/{id}/status

AI Recommendations

POST /api/recommendations/generate

GET /api/recommendations/history

Admin (ADMIN role)

/api/admin/products/**

/api/admin/orders/**

/api/admin/users/**

🔐 Security Notes

All protected APIs require:

Authorization: Bearer <JWT_TOKEN>


Role-based access enforced using Spring Security

🌐 Frontend

Built using React.js + Tailwind CSS

Deployed on Vercel

Communicates with backend via REST APIs & WebSockets

📈 Future Enhancements

ML-based AI recommendations

Video consultation

Wearable device integration

Multi-language support

Advanced analytics dashboard

👨‍💻 Author

Sarvesh Kumar Roy
B.Tech CSE (Data Science) – 2025
Backend & Full-Stack Developer
