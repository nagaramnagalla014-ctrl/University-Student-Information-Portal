package com.university.portal.service.impl;

import com.university.portal.dao.CourseDAO;
import com.university.portal.model.Course;
import com.university.portal.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    @Autowired private CourseDAO courseDAO;

    @Override
    @Transactional(readOnly = true)
    public Course getById(Long courseId) {
        return courseDAO.findById(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> getAll() {
        return courseDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> getAvailable() {
        return courseDAO.findByActiveSemester();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> getByFaculty(Long facultyId) {
        return courseDAO.findByFaculty(facultyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> search(String keyword) {
        return courseDAO.search(keyword);
    }

    @Override
    public Course create(Course course) {
        return courseDAO.save(course);
    }

    @Override
    public Course update(Course course) {
        return courseDAO.update(course);
    }

    @Override
    public void delete(Long courseId) {
        courseDAO.delete(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return courseDAO.countAll();
    }

    @Override
    @Transactional(readOnly = true)
    public long countEnrolled(Long courseId) {
        return courseDAO.countEnrolled(courseId);
    }
}
