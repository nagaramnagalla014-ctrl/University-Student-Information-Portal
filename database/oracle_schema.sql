-- =====================================================================
-- University Student Information Portal - Oracle Schema
-- Duration: Apr 2017 - Feb 2018
-- User: portal_user / portal123
-- =====================================================================

-- Create and grant schema user (run as DBA)
-- CREATE USER portal_user IDENTIFIED BY portal123;
-- GRANT CONNECT, RESOURCE, UNLIMITED TABLESPACE TO portal_user;

-- =====================================================================
-- SEQUENCES
-- =====================================================================
CREATE SEQUENCE DEPT_SEQ    START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE SEM_SEQ     START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE STUDENT_SEQ START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE FACULTY_SEQ START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE COURSE_SEQ  START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE ENROLL_SEQ  START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE NOTIF_SEQ   START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- =====================================================================
-- TABLES
-- =====================================================================

CREATE TABLE DEPARTMENTS (
    DEPT_ID     NUMBER          PRIMARY KEY,
    DEPT_NAME   VARCHAR2(100)   NOT NULL,
    DEPT_CODE   VARCHAR2(10)    UNIQUE NOT NULL,
    DESCRIPTION VARCHAR2(500),
    CREATED_AT  DATE            DEFAULT SYSDATE
);

CREATE TABLE SEMESTERS (
    SEM_ID      NUMBER          PRIMARY KEY,
    SEM_NAME    VARCHAR2(50)    NOT NULL,
    START_DATE  DATE            NOT NULL,
    END_DATE    DATE            NOT NULL,
    IS_ACTIVE   NUMBER(1)       DEFAULT 0,
    CREATED_AT  DATE            DEFAULT SYSDATE
);

CREATE TABLE STUDENTS (
    STUDENT_ID      NUMBER          PRIMARY KEY,
    STUDENT_CODE    VARCHAR2(20)    UNIQUE NOT NULL,
    FIRST_NAME      VARCHAR2(50)    NOT NULL,
    LAST_NAME       VARCHAR2(50)    NOT NULL,
    EMAIL           VARCHAR2(100)   UNIQUE NOT NULL,
    PASSWORD        VARCHAR2(255)   NOT NULL,
    PHONE           VARCHAR2(15),
    DATE_OF_BIRTH   DATE,
    PROGRAM         VARCHAR2(100),
    DEPT_ID         NUMBER          REFERENCES DEPARTMENTS(DEPT_ID),
    SEM_ID          NUMBER          REFERENCES SEMESTERS(SEM_ID),
    ADMISSION_DATE  DATE            DEFAULT SYSDATE,
    GPA             NUMBER(4,2)     DEFAULT 0,
    IS_ACTIVE       NUMBER(1)       DEFAULT 1,
    CREATED_AT      DATE            DEFAULT SYSDATE
);

CREATE TABLE FACULTY (
    FACULTY_ID      NUMBER          PRIMARY KEY,
    FACULTY_CODE    VARCHAR2(20)    UNIQUE NOT NULL,
    FIRST_NAME      VARCHAR2(50)    NOT NULL,
    LAST_NAME       VARCHAR2(50)    NOT NULL,
    EMAIL           VARCHAR2(100)   UNIQUE NOT NULL,
    PASSWORD        VARCHAR2(255)   NOT NULL,
    PHONE           VARCHAR2(15),
    SPECIALIZATION  VARCHAR2(100),
    DEPT_ID         NUMBER          REFERENCES DEPARTMENTS(DEPT_ID),
    DESIGNATION     VARCHAR2(50),
    IS_ACTIVE       NUMBER(1)       DEFAULT 1,
    CREATED_AT      DATE            DEFAULT SYSDATE
);

CREATE TABLE COURSES (
    COURSE_ID       NUMBER          PRIMARY KEY,
    COURSE_CODE     VARCHAR2(20)    UNIQUE NOT NULL,
    COURSE_NAME     VARCHAR2(100)   NOT NULL,
    CREDITS         NUMBER(2)       NOT NULL,
    DEPT_ID         NUMBER          REFERENCES DEPARTMENTS(DEPT_ID),
    FACULTY_ID      NUMBER          REFERENCES FACULTY(FACULTY_ID),
    SEM_ID          NUMBER          REFERENCES SEMESTERS(SEM_ID),
    MAX_STUDENTS    NUMBER(3)       DEFAULT 60,
    DESCRIPTION     VARCHAR2(500),
    IS_ACTIVE       NUMBER(1)       DEFAULT 1
);

CREATE TABLE ENROLLMENTS (
    ENROLLMENT_ID           NUMBER          PRIMARY KEY,
    STUDENT_ID              NUMBER          NOT NULL REFERENCES STUDENTS(STUDENT_ID),
    COURSE_ID               NUMBER          NOT NULL REFERENCES COURSES(COURSE_ID),
    ENROLLMENT_DATE         DATE            DEFAULT SYSDATE,
    GRADE                   VARCHAR2(5),
    GRADE_POINTS            NUMBER(3,1),
    ATTENDANCE_PERCENTAGE   NUMBER(5,2)     DEFAULT 0,
    STATUS                  VARCHAR2(20)    DEFAULT 'ENROLLED',
    REMARKS                 VARCHAR2(500),
    CONSTRAINT UQ_ENROLLMENT UNIQUE(STUDENT_ID, COURSE_ID)
);

CREATE TABLE NOTIFICATIONS (
    NOTIFICATION_ID     NUMBER          PRIMARY KEY,
    TITLE               VARCHAR2(100)   NOT NULL,
    MESSAGE             VARCHAR2(1000)  NOT NULL,
    RECIPIENT_ID        NUMBER          NOT NULL,
    RECIPIENT_TYPE      VARCHAR2(10)    NOT NULL,
    IS_READ             NUMBER(1)       DEFAULT 0,
    NOTIFICATION_TYPE   VARCHAR2(30),
    CREATED_AT          DATE            DEFAULT SYSDATE
);

-- =====================================================================
-- INDEXES
-- =====================================================================
CREATE INDEX IDX_STUDENTS_DEPT    ON STUDENTS(DEPT_ID);
CREATE INDEX IDX_COURSES_DEPT     ON COURSES(DEPT_ID);
CREATE INDEX IDX_COURSES_FACULTY  ON COURSES(FACULTY_ID);
CREATE INDEX IDX_ENROLL_STUDENT   ON ENROLLMENTS(STUDENT_ID);
CREATE INDEX IDX_ENROLL_COURSE    ON ENROLLMENTS(COURSE_ID);
CREATE INDEX IDX_NOTIF_RECIPIENT  ON NOTIFICATIONS(RECIPIENT_ID, RECIPIENT_TYPE);

-- =====================================================================
-- SEED DATA
-- =====================================================================

-- Departments
INSERT INTO DEPARTMENTS VALUES (DEPT_SEQ.NEXTVAL, 'Computer Science & Engineering', 'CSE', 'Programs in software, systems and AI', SYSDATE);
INSERT INTO DEPARTMENTS VALUES (DEPT_SEQ.NEXTVAL, 'Electronics & Communication',    'ECE', 'Programs in circuits, signals and networks', SYSDATE);
INSERT INTO DEPARTMENTS VALUES (DEPT_SEQ.NEXTVAL, 'Mechanical Engineering',         'ME',  'Programs in design, thermodynamics and manufacturing', SYSDATE);
INSERT INTO DEPARTMENTS VALUES (DEPT_SEQ.NEXTVAL, 'Information Technology',         'IT',  'Programs in databases, web and cloud technologies', SYSDATE);

-- Semesters
INSERT INTO SEMESTERS VALUES (SEM_SEQ.NEXTVAL, 'Spring 2017', DATE '2017-01-01', DATE '2017-05-31', 0, SYSDATE);
INSERT INTO SEMESTERS VALUES (SEM_SEQ.NEXTVAL, 'Fall 2017',   DATE '2017-07-01', DATE '2017-11-30', 1, SYSDATE);
INSERT INTO SEMESTERS VALUES (SEM_SEQ.NEXTVAL, 'Spring 2018', DATE '2018-01-01', DATE '2018-05-31', 0, SYSDATE);

-- Faculty (password: 'faculty123')
INSERT INTO FACULTY VALUES (FACULTY_SEQ.NEXTVAL, 'FAC-001', 'Ravi',    'Kumar',   'ravi.kumar@university.edu',   'faculty123', '9876543210', 'Data Structures & Algorithms', 1, 'Professor',          1, SYSDATE);
INSERT INTO FACULTY VALUES (FACULTY_SEQ.NEXTVAL, 'FAC-002', 'Priya',   'Sharma',  'priya.sharma@university.edu', 'faculty123', '9876543211', 'Database Management Systems',  1, 'Associate Professor', 1, SYSDATE);
INSERT INTO FACULTY VALUES (FACULTY_SEQ.NEXTVAL, 'FAC-003', 'Suresh',  'Reddy',   'suresh.reddy@university.edu', 'faculty123', '9876543212', 'Web Technologies',             4, 'Assistant Professor', 1, SYSDATE);
INSERT INTO FACULTY VALUES (FACULTY_SEQ.NEXTVAL, 'FAC-004', 'Anjali',  'Patel',   'anjali.patel@university.edu', 'faculty123', '9876543213', 'Machine Learning',             1, 'Professor',           1, SYSDATE);

-- Courses (Fall 2017 = SEM_ID 2)
INSERT INTO COURSES VALUES (COURSE_SEQ.NEXTVAL, 'CSE301', 'Data Structures',         4, 1, 1, 2, 60, 'Arrays, linked lists, trees and graphs', 1);
INSERT INTO COURSES VALUES (COURSE_SEQ.NEXTVAL, 'CSE302', 'Database Management',     3, 1, 2, 2, 60, 'Relational databases, SQL and normalization', 1);
INSERT INTO COURSES VALUES (COURSE_SEQ.NEXTVAL, 'IT301',  'Web Development',         3, 4, 3, 2, 60, 'HTML, CSS, JavaScript, AngularJS and REST APIs', 1);
INSERT INTO COURSES VALUES (COURSE_SEQ.NEXTVAL, 'CSE401', 'Machine Learning',        4, 1, 4, 2, 50, 'Supervised and unsupervised learning algorithms', 1);

-- Students (password: 'student123')
INSERT INTO STUDENTS VALUES (STUDENT_SEQ.NEXTVAL, 'S-00001', 'Rahul',  'Verma',  'rahul.verma@student.edu',  'student123', '9123456701', DATE '1997-04-15', 'B.Tech', 1, 2, DATE '2015-07-01', 0, 1, SYSDATE);
INSERT INTO STUDENTS VALUES (STUDENT_SEQ.NEXTVAL, 'S-00002', 'Neha',   'Singh',  'neha.singh@student.edu',   'student123', '9123456702', DATE '1997-08-22', 'B.Tech', 1, 2, DATE '2015-07-01', 0, 1, SYSDATE);
INSERT INTO STUDENTS VALUES (STUDENT_SEQ.NEXTVAL, 'S-00003', 'Aditya', 'Rao',    'aditya.rao@student.edu',   'student123', '9123456703', DATE '1998-01-10', 'B.Tech', 4, 2, DATE '2016-07-01', 0, 1, SYSDATE);

-- Enrollments (students enrolled in Fall 2017 courses)
INSERT INTO ENROLLMENTS VALUES (ENROLL_SEQ.NEXTVAL, 1, 1, SYSDATE, null, null, 0, 'ENROLLED', null);
INSERT INTO ENROLLMENTS VALUES (ENROLL_SEQ.NEXTVAL, 1, 2, SYSDATE, null, null, 0, 'ENROLLED', null);
INSERT INTO ENROLLMENTS VALUES (ENROLL_SEQ.NEXTVAL, 1, 3, SYSDATE, null, null, 0, 'ENROLLED', null);
INSERT INTO ENROLLMENTS VALUES (ENROLL_SEQ.NEXTVAL, 2, 1, SYSDATE, null, null, 0, 'ENROLLED', null);
INSERT INTO ENROLLMENTS VALUES (ENROLL_SEQ.NEXTVAL, 2, 4, SYSDATE, null, null, 0, 'ENROLLED', null);
INSERT INTO ENROLLMENTS VALUES (ENROLL_SEQ.NEXTVAL, 3, 3, SYSDATE, null, null, 0, 'ENROLLED', null);

COMMIT;
