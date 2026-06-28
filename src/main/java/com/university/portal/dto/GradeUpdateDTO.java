package com.university.portal.dto;

public class GradeUpdateDTO {
    private Long enrollmentId;
    private String grade;
    private Double gradePoints;
    private String remarks;

    public Long getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(Long enrollmentId) { this.enrollmentId = enrollmentId; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public Double getGradePoints() { return gradePoints; }
    public void setGradePoints(Double gradePoints) { this.gradePoints = gradePoints; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
