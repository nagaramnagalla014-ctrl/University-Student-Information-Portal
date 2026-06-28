package com.university.portal.service;

import com.university.portal.model.Enrollment;
import java.util.List;

public interface EnrollmentService {
    Enrollment enroll(Long studentId, Long courseId);
    void drop(Long enrollmentId);
    Enrollment updateGrade(Long enrollmentId, String grade, Double gradePoints, String remarks);
    Enrollment updateAttendance(Long enrollmentId, Double attendancePercentage);
    List<Enrollment> getByStudent(Long studentId);
    List<Enrollment> getByCourse(Long courseId);
    Enrollment findByStudentAndCourse(Long studentId, Long courseId);
    void recalculateGPA(Long studentId);
}
