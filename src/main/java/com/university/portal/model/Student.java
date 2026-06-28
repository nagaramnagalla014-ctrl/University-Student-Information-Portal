package com.university.portal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "STUDENTS")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_gen")
    @SequenceGenerator(name = "student_gen", sequenceName = "STUDENT_SEQ", allocationSize = 1)
    @Column(name = "STUDENT_ID")
    private Long studentId;

    @Column(name = "STUDENT_CODE", unique = true, nullable = false, length = 20)
    private String studentCode;

    @Column(name = "FIRST_NAME", nullable = false, length = 50)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 50)
    private String lastName;

    @Column(name = "EMAIL", unique = true, nullable = false, length = 100)
    private String email;

    @JsonIgnore
    @Column(name = "PASSWORD", nullable = false, length = 255)
    private String password;

    @Column(name = "PHONE", length = 15)
    private String phone;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @Column(name = "PROGRAM", length = 100)
    private String program;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DEPT_ID")
    private Department department;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SEM_ID")
    private Semester currentSemester;

    @Temporal(TemporalType.DATE)
    @Column(name = "ADMISSION_DATE")
    private Date admissionDate;

    @Column(name = "GPA")
    private Double gpa = 0.0;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive = true;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT")
    private Date createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    @PrePersist
    protected void onCreate() { createdAt = new Date(); }

    public String getFullName() { return firstName + " " + lastName; }

    // Getters and Setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public Semester getCurrentSemester() { return currentSemester; }
    public void setCurrentSemester(Semester currentSemester) { this.currentSemester = currentSemester; }
    public Date getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(Date admissionDate) { this.admissionDate = admissionDate; }
    public Double getGpa() { return gpa; }
    public void setGpa(Double gpa) { this.gpa = gpa; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public List<Enrollment> getEnrollments() { return enrollments; }
    public void setEnrollments(List<Enrollment> enrollments) { this.enrollments = enrollments; }
}
