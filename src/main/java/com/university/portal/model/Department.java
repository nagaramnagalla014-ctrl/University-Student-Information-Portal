package com.university.portal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "DEPARTMENTS")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dept_gen")
    @SequenceGenerator(name = "dept_gen", sequenceName = "DEPT_SEQ", allocationSize = 1)
    @Column(name = "DEPT_ID")
    private Long deptId;

    @Column(name = "DEPT_NAME", nullable = false, length = 100)
    private String deptName;

    @Column(name = "DEPT_CODE", unique = true, nullable = false, length = 10)
    private String deptCode;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT")
    private Date createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Student> students;

    @JsonIgnore
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Faculty> faculty;

    @JsonIgnore
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Course> courses;

    @PrePersist
    protected void onCreate() { createdAt = new Date(); }

    // Getters and Setters
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
    public String getDeptCode() { return deptCode; }
    public void setDeptCode(String deptCode) { this.deptCode = deptCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }
    public List<Faculty> getFaculty() { return faculty; }
    public void setFaculty(List<Faculty> faculty) { this.faculty = faculty; }
    public List<Course> getCourses() { return courses; }
    public void setCourses(List<Course> courses) { this.courses = courses; }
}
