package com.university.portal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "FACULTY")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "faculty_gen")
    @SequenceGenerator(name = "faculty_gen", sequenceName = "FACULTY_SEQ", allocationSize = 1)
    @Column(name = "FACULTY_ID")
    private Long facultyId;

    @Column(name = "FACULTY_CODE", unique = true, nullable = false, length = 20)
    private String facultyCode;

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

    @Column(name = "SPECIALIZATION", length = 100)
    private String specialization;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DEPT_ID")
    private Department department;

    @Column(name = "DESIGNATION", length = 50)
    private String designation;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive = true;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT")
    private Date createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "faculty", fetch = FetchType.LAZY)
    private List<Course> courses;

    @PrePersist
    protected void onCreate() { createdAt = new Date(); }

    public String getFullName() { return firstName + " " + lastName; }

    // Getters and Setters
    public Long getFacultyId() { return facultyId; }
    public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }
    public String getFacultyCode() { return facultyCode; }
    public void setFacultyCode(String facultyCode) { this.facultyCode = facultyCode; }
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
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public List<Course> getCourses() { return courses; }
    public void setCourses(List<Course> courses) { this.courses = courses; }
}
