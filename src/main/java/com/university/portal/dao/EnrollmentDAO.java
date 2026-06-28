package com.university.portal.dao;

import com.university.portal.model.Enrollment;
import java.util.List;

public interface EnrollmentDAO {
    Enrollment findById(Long enrollmentId);
    List<Enrollment> findByStudent(Long studentId);
    List<Enrollment> findByCourse(Long courseId);
    List<Enrollment> findByStudentAndSemester(Long studentId, Long semId);
    Enrollment findByStudentAndCourse(Long studentId, Long courseId);
    Enrollment save(Enrollment enrollment);
    Enrollment update(Enrollment enrollment);
    void drop(Long enrollmentId);
    boolean isAlreadyEnrolled(Long studentId, Long courseId);
    long countEnrolledInCourse(Long courseId);
}
