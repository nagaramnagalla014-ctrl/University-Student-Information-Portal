package com.university.portal.dao;

import com.university.portal.model.Course;
import java.util.List;

public interface CourseDAO {
    Course findById(Long courseId);
    Course findByCourseCode(String courseCode);
    List<Course> findAll();
    List<Course> findByActiveSemester();
    List<Course> findByFaculty(Long facultyId);
    List<Course> findByDepartment(Long deptId);
    List<Course> search(String keyword);
    Course save(Course course);
    Course update(Course course);
    void delete(Long courseId);
    long countAll();
    long countEnrolled(Long courseId);
}
