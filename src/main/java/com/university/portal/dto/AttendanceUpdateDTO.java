package com.university.portal.dto;

public class AttendanceUpdateDTO {
    private Long enrollmentId;
    private Double attendancePercentage;

    public Long getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(Long enrollmentId) { this.enrollmentId = enrollmentId; }
    public Double getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(Double attendancePercentage) { this.attendancePercentage = attendancePercentage; }
}
