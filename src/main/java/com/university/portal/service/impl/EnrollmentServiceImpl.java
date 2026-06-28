package com.university.portal.service.impl;

import com.university.portal.dao.CourseDAO;
import com.university.portal.dao.EnrollmentDAO;
import com.university.portal.dao.StudentDAO;
import com.university.portal.model.Course;
import com.university.portal.model.Enrollment;
import com.university.portal.model.Student;
import com.university.portal.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired private EnrollmentDAO enrollmentDAO;
    @Autowired private StudentDAO studentDAO;
    @Autowired private CourseDAO courseDAO;

    @Override
    public Enrollment enroll(Long studentId, Long courseId) {
        if (enrollmentDAO.isAlreadyEnrolled(studentId, courseId)) {
            throw new RuntimeException("Student is already enrolled in this course");
        }
        Course course = courseDAO.findById(courseId);
        if (course == null) throw new RuntimeException("Course not found");
        long enrolled = courseDAO.countEnrolled(courseId);
        if (enrolled >= course.getMaxStudents()) {
            throw new RuntimeException("Course is full. Maximum capacity reached");
        }
        Student student = studentDAO.findById(studentId);
        if (student == null) throw new RuntimeException("Student not found");

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus("ENROLLED");
        return enrollmentDAO.save(enrollment);
    }

    @Override
    public void drop(Long enrollmentId) {
        enrollmentDAO.drop(enrollmentId);
    }

    @Override
    public Enrollment updateGrade(Long enrollmentId, String grade, Double gradePoints, String remarks) {
        Enrollment e = enrollmentDAO.findById(enrollmentId);
        if (e == null) throw new RuntimeException("Enrollment not found");
        e.setGrade(grade);
        e.setGradePoints(gradePoints);
        e.setRemarks(remarks);
        Enrollment updated = enrollmentDAO.update(e);
        recalculateGPA(e.getStudent().getStudentId());
        return updated;
    }

    @Override
    public Enrollment updateAttendance(Long enrollmentId, Double attendancePercentage) {
        Enrollment e = enrollmentDAO.findById(enrollmentId);
        if (e == null) throw new RuntimeException("Enrollment not found");
        e.setAttendancePercentage(attendancePercentage);
        return enrollmentDAO.update(e);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enrollment> getByStudent(Long studentId) {
        return enrollmentDAO.findByStudent(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enrollment> getByCourse(Long courseId) {
        return enrollmentDAO.findByCourse(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public Enrollment findByStudentAndCourse(Long studentId, Long courseId) {
        return enrollmentDAO.findByStudentAndCourse(studentId, courseId);
    }

    @Override
    public void recalculateGPA(Long studentId) {
        List<Enrollment> enrollments = enrollmentDAO.findByStudent(studentId);
        double totalPoints = 0;
        int gradedCourses = 0;
        for (Enrollment e : enrollments) {
            if (e.getGradePoints() != null && "ENROLLED".equals(e.getStatus())) {
                totalPoints += e.getGradePoints();
                gradedCourses++;
            }
        }
        double gpa = gradedCourses > 0 ? totalPoints / gradedCourses : 0.0;
        Student student = studentDAO.findById(studentId);
        if (student != null) {
            student.setGpa(Math.round(gpa * 100.0) / 100.0);
            studentDAO.update(student);
        }
    }
}
