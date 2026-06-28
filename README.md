# University Student Information Portal

A centralized full-stack student information system where students register for courses, view grades, download transcripts, and receive real-time notifications — while faculty manage attendance and upload grades through the same platform.

**Duration:** April 2017 – February 2018

---

## Overview

Built to replace a fragmented paper-based registration system across 4 departments and 3000+ students. The portal eliminated manual transcript generation, reduced grade-upload time from 2 days to minutes, and gave students instant access to their academic records.

---

## Features

### Student Portal
- Self-service course registration with real-time seat availability
- View grades and GPA (auto-calculated after each grade upload)
- Printable academic transcript
- Notification inbox (grade updates, enrollment confirmations)

### Faculty Portal
- Dashboard showing assigned courses and enrolled students
- Grade upload per course per student with instant student notification
- Attendance management with per-student percentage tracking

### Admin Portal
- Dashboard with portal-wide statistics
- Student and faculty registration management
- Course and semester lifecycle management

### REST API (Backend)
- Stateless JSON API consumed by the AngularJS SPA
- Session-based authentication (`/api/auth/login`, `/api/auth/me`, `/api/auth/logout`)
- Role-based authorization (STUDENT / FACULTY / ADMIN) enforced at controller layer

### CI/CD Pipeline (Jenkins)
- `Jenkinsfile` with Checkout → Build → Test → Deploy stages
- Auto-deploys to Apache Tomcat via Tomcat Manager REST API on `main` branch push

---

## Technologies Used

| Layer | Technology |
|-------|-----------|
| Language | Java 8 |
| Backend Framework | Spring MVC 4.3 (REST) |
| ORM | Hibernate 5.2 |
| Database | Oracle 11g / Oracle XE |
| Connection Pool | C3P0 0.9.5 |
| JSON Serialization | Jackson 2.9 |
| Frontend Framework | AngularJS 1.6 |
| Frontend UI | Bootstrap 3.3.7 |
| Build Tool | Maven 3 |
| CI/CD | Jenkins (Declarative Pipeline) |
| App Server | Apache Tomcat 7/8 |
| IDE | Eclipse |
| Version Control | Git |

---

## Architecture

```
REST API Architecture:
  AngularJS SPA ← $http → Spring REST @RestController → @Service → @Repository → Oracle DB

src/main/java/com/university/portal/
├── model/         Hibernate @Entity (7 entities)
├── dao/           DAO interfaces (7)
│   └── impl/      Hibernate HQL implementations
├── service/       Business logic interfaces (7)
│   └── impl/      @Transactional service implementations
├── controller/    @RestController (6 controllers, JSON responses)
└── dto/           Request/response DTOs (LoginRequest, GradeUpdateDTO, AttendanceUpdateDTO)

src/main/webapp/
├── index.html     AngularJS app entry point (ng-app bootstrap)
├── app/
│   ├── app.js                    ng-route config + AuthGuard factory
│   ├── services/api.service.js   $http wrapper + AuthService (localStorage)
│   └── controllers/              Student / Faculty / Admin AngularJS controllers
│       └── views/                HTML partial templates per route
├── css/style.css  Custom theme (blue university palette)
└── WEB-INF/
    ├── web.xml                   Servlet + session config
    ├── dispatcher-servlet.xml    Spring MVC + static resource mapping
    └── applicationContext.xml    Oracle DataSource, Hibernate, Transactions
```

---

## Database Schema (Oracle)

| Table | Description |
|-------|-------------|
| DEPARTMENTS | Academic departments |
| SEMESTERS | Semester records with active flag |
| STUDENTS | Student profiles with auto-codes (S-00001) |
| FACULTY | Faculty profiles with auto-codes (FAC-001) |
| COURSES | Course catalog linked to faculty and semester |
| ENROLLMENTS | Student-course junction with grade and attendance |
| NOTIFICATIONS | Per-user notification inbox |

---

## REST API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | Login (student/faculty/admin) |
| GET | `/api/auth/me` | Current session info |
| POST | `/api/auth/logout` | Logout |
| GET | `/api/students/{id}` | Student profile |
| GET | `/api/students/{id}/enrollments` | Student's enrolled courses |
| POST | `/api/students/{id}/enroll/{courseId}` | Enroll in a course |
| DELETE | `/api/students/{id}/enroll/{enrollmentId}` | Drop a course |
| GET | `/api/faculty/{id}/courses` | Faculty's courses |
| GET | `/api/faculty/courses/{courseId}/students` | Students in a course |
| PUT | `/api/faculty/grades` | Upload a grade |
| PUT | `/api/faculty/attendance` | Update attendance |
| GET | `/api/courses/available` | Active semester courses |
| GET | `/api/admin/stats` | Portal-wide statistics |
| POST | `/api/admin/students` | Register student |
| GET | `/api/notifications` | User's notifications |
| PUT | `/api/notifications/{id}/read` | Mark notification read |

---

## Setup Instructions

### Prerequisites
- JDK 8+, Maven 3.x, Apache Tomcat 7/8
- Oracle 11g or Oracle XE

### Database Setup
```sql
CREATE USER portal_user IDENTIFIED BY portal123;
GRANT CONNECT, RESOURCE, UNLIMITED TABLESPACE TO portal_user;
-- Then run:
-- sqlplus portal_user/portal123@localhost:1521:XE @database/oracle_schema.sql
```

### Configure DB
Edit `src/main/webapp/WEB-INF/applicationContext.xml`:
```xml
<property name="jdbcUrl"  value="jdbc:oracle:thin:@localhost:1521:XE"/>
<property name="user"     value="portal_user"/>
<property name="password" value="portal123"/>
```

### Build & Deploy
```bash
mvn clean package
cp target/student-portal.war $TOMCAT_HOME/webapps/
```
Access: `http://localhost:8080/student-portal/`

### Demo Credentials
| Role | Email | Password |
|------|-------|----------|
| Student | rahul.verma@student.edu | student123 |
| Faculty | ravi.kumar@university.edu | faculty123 |
| Admin | admin@university.edu | admin123 |

---

## Jenkins CI/CD

The `Jenkinsfile` at the project root defines a declarative pipeline:

```
Stage 1: Checkout  → git checkout
Stage 2: Build     → mvn clean package -DskipTests
Stage 3: Tests     → mvn test + JUnit report
Stage 4: Quality   → mvn verify (plug in SonarQube)
Stage 5: Deploy    → curl Tomcat Manager API (main branch only)
```

Configure Jenkins:
1. Create a new Pipeline job
2. Point it to this repository
3. Set "Script Path" to `Jenkinsfile`
4. Set Tomcat credentials in Jenkins Credentials Store

---

## Key Technical Decisions

- **AngularJS SPA + Spring REST** separation: frontend makes `$http` calls to `/api/**`; Spring serves static files via `mvc:default-servlet-handler`.
- **Session-based auth stored in `localStorage`**: AngularJS reads user info from `localStorage` on route change; server session validates each API call independently.
- **`@JsonIgnore` on bidirectional Hibernate relationships**: prevents Jackson serialization cycles on `@OneToMany` collections.
- **GPA auto-recalculation** in `EnrollmentServiceImpl.recalculateGPA()`: triggered every time a grade is updated, keeping the `STUDENTS.GPA` column always current.
- **AuthGuard factory** in AngularJS: injects into route `resolve` to redirect unauthenticated users to `/login` before the view loads.
- **Oracle SEQUENCES** for all primary keys: Hibernate `@SequenceGenerator` with `allocationSize=1` to match Oracle sequence increment.

---

## Key Learnings

- Building REST APIs with Spring MVC `@RestController` and consuming them via AngularJS `$http`
- AngularJS routing with `ngRoute`, `$routeProvider`, and route guards via `resolve`
- Role-based access control enforced at both the Angular route level and the Spring controller level
- Oracle Hibernate integration: sequences, Oracle10gDialect, C3P0 connection pooling
- Jenkins declarative pipeline for CI/CD: build, test, archive artifacts, deploy to Tomcat
- Preventing Jackson circular serialization with `@JsonIgnore` on Hibernate `@OneToMany` mappings
