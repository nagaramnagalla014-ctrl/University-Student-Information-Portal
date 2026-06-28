package com.university.portal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "COURSES")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_gen")
    @SequenceGenerator(name = "course_gen", sequenceName = "COURSE_SEQ", allocationSize = 1)
    @Column(name = "COURSE_ID")
    private Long courseId;

    @Column(name = "COURSE_CODE", unique = true, nullable = false, length = 20)
    private String courseCode;

    @Column(name = "COURSE_NAME", nullable = false, length = 100)
    private String courseName;

    @Column(name = "CREDITS", nullable = false)
    private Integer credits;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DEPT_ID")
    private Department department;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FACULTY_ID")
    private Faculty faculty;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SEM_ID")
    private Semester semester;

    @Column(name = "MAX_STUDENTS")
    private Integer maxStudents = 60;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive = true;

    @JsonIgnore
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    // Getters and Setters
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public Faculty getFaculty() { return faculty; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }
    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { this.semester = semester; }
    public Integer getMaxStudents() { return maxStudents; }
    public void setMaxStudents(Integer maxStudents) { this.maxStudents = maxStudents; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public List<Enrollment> getEnrollments() { return enrollments; }
    public void setEnrollments(List<Enrollment> enrollments) { this.enrollments = enrollments; }
}
