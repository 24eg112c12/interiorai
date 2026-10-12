# InteriorAI — AI-Powered Interior Design & Renovation Platform

> **Upload. Set Your Budget. See Your Room Transformed.**  
> Design your dream space with AI, receive itemized cost estimations in Indian Rupees (INR), and connect with verified renovation professionals to bring your project to life.

---

## 📑 Table of Contents
1. [Overview](#-overview)
2. [Key Features](#-key-features)
3. [Technology Stack](#-technology-stack)
4. [Project Architecture](#-project-architecture)
5. [Prerequisites](#-prerequisites)
6. [Database Setup (MySQL)](#-database-setup-mysql)
7. [Running the Application](#-running-the-application)
   - [Running via Spring Tools Suite (STS) / Java IDE](#option-a-importing-into-spring-tools-suite-sts)
   - [Running via Maven Command Line](#option-b-running-via-maven-cli)
   - [Running with Dev Profile (In-Memory Fallback)](#option-c-zero-configuration-dev-mode)
8. [Accessing the Frontend](#-accessing-the-frontend)
9. [REST API Documentation](#-rest-api-documentation)
10. [AI Image Generation & Demonstration Mode](#-ai-image-generation--demo-mode)
11. [Sample Test Credentials](#-sample-test-credentials)
12. [Troubleshooting Guide](#-troubleshooting-guide)

---

## 🌟 Overview
**InteriorAI** is a full-stack, enterprise-grade interior architectural design and renovation platform specifically engineered for Indian homeowners and skilled tradesmen. It bridges the gap between conceptual spatial design and construction reality by pairing generative room transformation images with realistic room dimension calculations, material grade multipliers, and trade-specific cost breakdowns.

---

## 🚀 Key Features

1. **User Authentication & Authorization**:
   - Secure registration, login, profile management, and stateless JWT token authentication with BCrypt password hashing.
   - Strict resource ownership verification to ensure users only access their own rooms, designs, budgets, and renovation projects.

2. **Multi-Step Room Redesign Studio**:
   - **Step 1 (Upload)**: Drag & drop photo upload supporting JPEG, PNG, and WebP (up to 10 MB) with server-side MIME type and size validation.
   - **Step 2 (Room Details)**: Room type selector, length, width, ceiling height, and live automatic floor area calculation ($Area = Length \times Width$).
   - **Step 3 (Budget & Preferences)**: Budget selection in INR (₹25,000, ₹50,000, ₹1,00,000, ₹2,00,000+), 8 curated interior styles, color palettes, and material preferences.
   - **Step 4 (AI Generation)**: Generative rendering with external AI integration and high-resolution demonstration fallback.

3. **Interactive Before-and-After Slider**:
   - Real-time comparison slider allowing users to slide horizontally to compare their original room with the AI transformed design.

4. **Intelligent Budget Estimation Engine**:
   - Calculates itemized costs across 8 core categories: Furniture, Painting, Lighting, Flooring, False Ceiling, Decorative items, Labor, and Miscellaneous.
   - Calibrated for Indian metro and Tier-2 pricing benchmarks with style-specific multipliers (Luxury, Traditional Indian, Minimalist, etc.).
   - Provides proactive cost-saving recommendations when estimates exceed the user's allocated budget (e.g., HDHMR laminates vs. solid teak, peripheral cove ceilings).

5. **Verified Professional Directory**:
   - Directory across 8 specialized trade categories: Interior Designers, Painters, Carpenters, Electricians, Lighting Specialists, False-Ceiling Workers, Sofa Makers, and Plumbers.
   - Search by name, service category, and Indian metro locations (Bengaluru, Mumbai, Delhi-NCR, Pune, Hyderabad, Chennai, Kolkata, Ahmedabad).

6. **Booking & Consultation System**:
   - Logged-in users can schedule appointments, attach renovation projects, and track status (`PENDING`, `ACCEPTED`, `REJECTED`, `COMPLETED`, `CANCELLED`).
   - One-click cancellation for pending appointments.

7. **Project Management & Renovation Tracker**:
   - Convert any saved design into an active renovation project.
   - Interactive progress bar and percentage slider (0% to 100%) with automatic status transitions (`PLANNING`, `IN_PROGRESS`, `COMPLETED`).
   - Target completion date tracking and milestone management.

8. **Personalized Dashboard**:
   - Overview metrics: Total Rooms, Saved Designs, Active Projects, and Pending Bookings.
   - Quick-access galleries of recent designs and renovation projects.

---

## 🛠 Technology Stack

### Backend
- **Java 17 / Java 21 / Java 27**
- **Spring Boot 3.3.4**
- **Spring Web** (RESTful APIs)
- **Spring Data JPA** & **Hibernate** (ORM)
- **Spring Security** & **JJWT 0.12.5** (Stateless Bearer token auth)
- **Spring Boot Validation** (Hibernate Validator)
- **MySQL Connector/J** & **H2 Database** (Testing & fallback)
- **Maven** (Build lifecycle and dependency management)

### Frontend
- **HTML5 & Vanilla CSS3** (Curated custom design system: warm beige, charcoal, muted sage green, and soft gold)
- **Modern Typography** (Google Fonts: *Outfit* & *Plus Jakarta Sans*)
- **JavaScript (ES6+)** with **Fetch API**
- **Interactive Components**: Before/after image slider, multi-step stepper, toast notifications, modals, and file dropzones

### Database
- **MySQL 8.0+** (7 core normalized relational tables + supporting item table, foreign keys, and indexes)

---

## 🏗 Project Architecture

```
interiorAI-1/
├── backend/
│   ├── pom.xml
│   ├── interiorai_db.sql                    # Standalone MySQL DDL & Seed script
│   └── src/
│       ├── main/
│       │   ├── java/com/interiorai/
│       │   │   ├── InteriorAiApplication.java
│       │   │   ├── config/                  # SecurityConfig, JwtTokenProvider, WebConfig
│       │   │   ├── controller/              # REST Controllers (Auth, Room, Design, Budget, etc.)
│       │   │   ├── dto/                     # Request and Response Data Transfer Objects
│       │   │   ├── entity/                  # JPA Entities (User, Room, Design, Budget, etc.)
│       │   │   ├── exception/               # GlobalExceptionHandler, Custom Exceptions
│       │   │   ├── repository/              # Spring Data JPA Repositories
│       │   │   └── service/                 # Business logic, Budget calculations, AI service
│       │   └── resources/
│       │       ├── application.properties   # Production/MySQL configuration
│       │       ├── application.properties.example
│       │       ├── application-dev.properties
│       │       ├── schema.sql               # Database DDL
│       │       ├── data.sql                 # Demonstration seed data
│       │       └── static/                  # Packaged frontend assets (served by Spring Boot)
│       └── test/
│           ├── java/com/interiorai/service/ # Unit tests (Budget, User, Room, Booking, Project)
│           └── resources/application-test.properties
├── frontend/                                # Decoupled frontend source files
│   ├── css/style.css                        # Master design system
│   ├── js/api.js                            # Fetch API client
│   ├── js/auth.js                           # Session and navbar manager
│   ├── js/components.js                     # Slider, toast, currency formatters
│   ├── index.html                           # Homepage
│   ├── register.html                        # Registration
│   ├── login.html                           # Login
│   ├── design.html                          # 4-step Room redesign studio
│   ├── design-results.html                  # Before/After slider & cost breakdown
│   ├── styles.html                          # 8 interior styles catalog
│   ├── professionals.html                   # Professional directory & search
│   ├── professional-details.html            # Profile & consultation booking
│   ├── bookings.html                        # Booking schedule & cancellation
│   ├── dashboard.html                       # User dashboard & metrics
│   ├── my-projects.html                     # Saved renovation projects
│   ├── project-details.html                 # Progress tracker & timeline
│   └── profile.html                         # User profile & settings
└── README.md
```

---

## 📋 Prerequisites
- **Java**: JDK 17 or higher (tested with Oracle JDK 17, 21, and 27)
- **Maven**: 3.8+ (included in standard Java development tools)
- **MySQL**: 8.0 or higher (optional if running in dev profile)

---

## 🗄 Database Setup (MySQL)

1. Open your terminal or MySQL Workbench:
   ```sql
   CREATE DATABASE interiorai_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
2. Run the provided database initialization script located in `backend/interiorai_db.sql`:
   ```bash
   mysql -u root -p interiorai_db < backend/interiorai_db.sql
   ```
3. Update your credentials in `backend/src/main/resources/application.properties` (or set environment variables `DB_USERNAME` and `DB_PASSWORD`):
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/interiorai_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=root
   ```

---

## 🚀 Running the Application

### Option A: Importing into Spring Tools Suite (STS) / Eclipse
1. Launch **Spring Tools Suite (STS)**.
2. Select **File** &rarr; **Import...** &rarr; **Existing Maven Projects**.
3. Browse to `c:\Users\racer\Desktop\interiorAI-1\backend` and click **Finish**.
4. Allow Maven to update project dependencies.
5. Right-click `InteriorAiApplication.java` &rarr; **Run As** &rarr; **Spring Boot App**.

### Option B: Running via Maven CLI
Open a terminal in the `backend/` folder and run:
```bash
mvn spring-boot:run
```

### Option C: Zero-Configuration Dev Mode (Embedded Database)
If your local MySQL server is currently stopped or you want to test immediately with zero external setup, run with the `dev` profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
This launches Spring Boot with an in-memory MySQL-compatible database pre-seeded with all styles and professionals.

---

## 🌐 Accessing the Frontend

You can access InteriorAI through either of the following methods:

1. **Directly via Spring Boot** (Recommended):  
   Open your browser and navigate to:  
   **[http://localhost:8080](http://localhost:8080)**  
   Spring Boot serves the full web interface, uploads, and APIs on a single unified port.

2. **Via Live Server / Independent Web Server**:  
   Open `frontend/index.html` with VS Code Live Server (port 5500 or 3000) or any local static server. Cross-Origin Resource Sharing (CORS) is enabled on the backend for standard local origins.

---

## 📡 REST API Documentation

### 1. Authentication
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/auth/register` | Public | Register new user account |
| `POST` | `/api/auth/login` | Public | Authenticate user & receive JWT |
| `POST` | `/api/auth/logout` | Public | End session confirmation |
| `GET` | `/api/auth/me` | Authenticated | Retrieve authenticated user profile |
| `PUT` | `/api/auth/profile` | Authenticated | Update full name and mobile number |

### 2. Rooms & Photo Uploads
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/rooms` | Authenticated | Save room details & dimensions |
| `GET` | `/api/rooms` | Authenticated | List all rooms created by current user |
| `GET` | `/api/rooms/{id}` | Authenticated | Get room details by ID (Ownership verified) |
| `PUT` | `/api/rooms/{id}` | Authenticated | Update room dimensions or requirements |
| `DELETE` | `/api/rooms/{id}` | Authenticated | Delete room and remove uploaded photo |
| `POST` | `/api/rooms/{id}/photo` | Authenticated | Upload room photo (`MultipartFile photo`, max 10MB) |

### 3. AI Design Generation
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/designs/generate` | Authenticated | Generate room transformation & budget |
| `GET` | `/api/designs` | Authenticated | List all saved designs of user |
| `GET` | `/api/designs/{id}` | Authenticated | Retrieve specific design details |
| `GET` | `/api/rooms/{roomId}/designs` | Authenticated | List all designs for a specific room |
| `DELETE` | `/api/designs/{id}` | Authenticated | Delete saved design |

### 4. Budgets & Cost Estimations
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/budgets/estimate` | Public | Interactive calculation based on dimensions & style |
| `POST` | `/api/budgets` | Authenticated | Save budget breakdown record |
| `GET` | `/api/budgets/{id}` | Authenticated | Retrieve budget by ID |
| `GET` | `/api/rooms/{roomId}/budgets` | Authenticated | List budgets associated with room |
| `PUT` | `/api/budgets/{id}` | Authenticated | Update total budget or actual expenses |

### 5. Professional Directory
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/professionals` | Public | List all verified trade professionals |
| `GET` | `/api/professionals/{id}` | Public | Get professional profile by ID |
| `GET` | `/api/professionals/search` | Public | Search by `query`, `category`, and `location` |
| `GET` | `/api/professionals/category/{category}` | Public | Filter professionals by specific trade |

### 6. Bookings
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/bookings` | Authenticated | Submit consultation booking request |
| `GET` | `/api/bookings/my` | Authenticated | List current user's booking appointments |
| `GET` | `/api/bookings/{id}` | Authenticated | Retrieve booking details |
| `PUT` | `/api/bookings/{id}/cancel` | Authenticated | Cancel pending booking |

### 7. Renovation Projects
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/projects` | Authenticated | Create project from saved design |
| `GET` | `/api/projects` | Authenticated | List user's active projects |
| `GET` | `/api/projects/{id}` | Authenticated | Get project details & timeline |
| `PUT` | `/api/projects/{id}` | Authenticated | Update project information |
| `PATCH` | `/api/projects/{id}/progress`| Authenticated | Update progress percentage (0-100%) and status |

### 8. Dashboard
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/dashboard/summary` | Authenticated | Aggregated metrics, recent designs & projects |

---

## 🤖 AI Image Generation & Demo Mode

InteriorAI supports both external image-generation API services and a high-resolution demonstration fallback:

### Configuring a Real Image API
Set the following environment variables (or configure in `application.properties`):
```bash
export AI_API_KEY="your-openai-api-key"
export AI_API_URL="https://api.openai.com/v1/images/generations"
export AI_DEMO_MODE="false"
```
When configured, the backend sends an architectural prompt containing room dimensions, style, color palette, and furniture preferences directly to the image generation endpoint.

### Demonstration Fallback Mode
When `AI_API_KEY` is not configured or `AI_DEMO_MODE=true` (the default), InteriorAI operates in **Demonstration Mode**:
- Uses curated high-resolution room transformations paired specifically to your room type (Living room, Bedroom, Kitchen, Office, etc.) and selected interior style (Modern, Minimalist, Traditional Indian, Scandinavian, Industrial, Luxury, Bohemian).
- Results are clearly identified with demonstration notices.

---

## 🔑 Sample Test Credentials

A demonstration homeowner account is pre-seeded in the database:
- **Email**: `demo@interiorai.com`
- **Password**: `password123`

*(You can also use the one-click **"Auto-Fill"** button on `login.html` to instantly populate these credentials).*

---

## ❓ Troubleshooting Guide

1. **Port 8080 already in use**:
   Change `server.port=8081` in `backend/src/main/resources/application.properties`.
2. **Access denied for MySQL root user**:
   Verify your MySQL username and password in `application.properties`, or launch the project with `mvn spring-boot:run -Dspring-boot.run.profiles=dev` to use the embedded database.
3. **File uploads exceeding size limit**:
   The maximum image upload size is configured to 10 MB per photograph in `spring.servlet.multipart.max-file-size=10MB`.
4. **Running tests**:
   Execute `mvn test` in the `backend/` directory to run all unit tests.

---

© 2026 InteriorAI Technologies India. All rights reserved.
