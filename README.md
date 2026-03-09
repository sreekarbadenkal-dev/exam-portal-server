# ⚙️ Online Examination System - Backend API

The robust, scalable server-side engine for the Online Examination System. Built using **Java** and the **Spring Boot** ecosystem.

---

## 🛠️ Tech Stack
- **Language:** Java 17
- **Framework:** Spring Boot 3.x
- **Security:** Custom Authentication & Role-Based Access Control
- **ORM:** Spring Data JPA / Hibernate
- **Database:** PostgreSQL
- **Build Tool:** Maven
- **Containerization:** Docker

## ✨ Key Features
- **RESTful API:** Clean endpoints for Student and Exam management.
- **Database Persistence:** Efficient data handling with PostgreSQL and JPA.
- **Security:** Implemented secure login logic and input validation.
- **Deployment:** Containerized with Docker and deployed on **Render**.

## 📊 Database Schema
The project uses a relational schema in PostgreSQL to manage:
- **Students:** Registration details and login credentials.
- **Exams:** Question banks, timing, and evaluation logic.

## 🔗 Project Architecture
This API powers the React frontend. You can view the user interface code here:
👉 **[View Frontend Repository](https://github.com/sreekarbadenkal-dev/exam-portal-client)**

## 💻 How to Run Locally
1. Ensure you have **PostgreSQL** installed and running.
2. Clone the repo: `git clone https://github.com/sreekarbadenkal-dev/your-backend-repo.git`
3. Update `application.properties` with your DB credentials.
4. Run the application: `./mvnw spring-boot:run`
