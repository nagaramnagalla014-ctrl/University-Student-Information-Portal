package com.university.portal.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ENROLLMENTS",
       uniqueConstraints = @UniqueConstraint(columnNames = {"STUDENT_ID", "COURSE_ID"}))
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "enroll_gen")
    @SequenceGenerator(name = "enroll_gen", sequenceName = "ENROLL_SEQ", allocationSize = 1)
    @Column(name = "ENROLLMENT_ID")
    private Long enrollmentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STUDENT_ID", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COURSE_ID", nullable = false)
    private Course course;

    @Temporal(TemporalType.DATE)
    @Column(name = "ENROLLMENT_DATE")
    private Date enrollmentDate;

    @Column(name = "GRADE", length = 5)
    private String grade;

    @Column(name = "GRADE_POINTS")
    private Double gradePoints;

    @Column(name = "ATTENDANCE_PERCENTAGE")
    private Double attendancePercentage = 0.0;

    @Column(name = "STATUS", length = 20)
    private String status = "ENROLLED";

    @Column(name = "REMARKS", length = 500)
    private String remarks;

    @PrePersist
    protected void onCreate() { enrollmentDate = new Date(); }

    // Getters and Setters
    public Long getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(Long enrollmentId) { this.enrollmentId = enrollmentId; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public Date getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(Date enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public Double getGradePoints() { return gradePoints; }
    public void setGradePoints(Double gradePoints) { this.gradePoints = gradePoints; }
    public Double getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(Double attendancePercentage) { this.attendancePercentage = attendancePercentage; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
