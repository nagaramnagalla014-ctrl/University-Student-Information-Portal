package com.university.portal.service;

import com.university.portal.model.Course;
import java.util.List;

public interface CourseService {
    Course getById(Long courseId);
    List<Course> getAll();
    List<Course> getAvailable();
    List<Course> getByFaculty(Long facultyId);
    List<Course> search(String keyword);
    Course create(Course course);
    Course update(Course course);
    void delete(Long courseId);
    long count();
    long countEnrolled(Long courseId);
}
