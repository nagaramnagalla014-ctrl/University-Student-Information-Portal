package com.university.portal.dao.impl;

import com.university.portal.dao.CourseDAO;
import com.university.portal.model.Course;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class CourseDAOImpl implements CourseDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Course findById(Long courseId) {
        return sessionFactory.getCurrentSession().get(Course.class, courseId);
    }

    @Override
    public Course findByCourseCode(String courseCode) {
        Query<Course> q = sessionFactory.getCurrentSession()
            .createQuery("FROM Course WHERE courseCode = :c", Course.class);
        q.setParameter("c", courseCode);
        List<Course> list = q.list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Course> findAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Course ORDER BY courseCode", Course.class).list();
    }

    @Override
    public List<Course> findByActiveSemester() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Course c WHERE c.semester.isActive = true AND c.isActive = true ORDER BY c.courseCode", Course.class).list();
    }

    @Override
    public List<Course> findByFaculty(Long facultyId) {
        Query<Course> q = sessionFactory.getCurrentSession()
            .createQuery("FROM Course c WHERE c.faculty.facultyId = :f ORDER BY c.courseCode", Course.class);
        q.setParameter("f", facultyId);
        return q.list();
    }

    @Override
    public List<Course> findByDepartment(Long deptId) {
        Query<Course> q = sessionFactory.getCurrentSession()
            .createQuery("FROM Course c WHERE c.department.deptId = :d ORDER BY c.courseCode", Course.class);
        q.setParameter("d", deptId);
        return q.list();
    }

    @Override
    public List<Course> search(String keyword) {
        String kw = "%" + keyword.toLowerCase() + "%";
        Query<Course> q = sessionFactory.getCurrentSession().createQuery(
            "FROM Course c WHERE LOWER(c.courseName) LIKE :k OR LOWER(c.courseCode) LIKE :k ORDER BY c.courseCode",
            Course.class);
        q.setParameter("k", kw);
        return q.list();
    }

    @Override
    public Course save(Course course) {
        sessionFactory.getCurrentSession().save(course);
        return course;
    }

    @Override
    public Course update(Course course) {
        sessionFactory.getCurrentSession().update(course);
        return course;
    }

    @Override
    public void delete(Long courseId) {
        Course c = findById(courseId);
        if (c != null) { c.setIsActive(false); update(c); }
    }

    @Override
    public long countAll() {
        return (Long) sessionFactory.getCurrentSession()
            .createQuery("SELECT COUNT(c) FROM Course c WHERE c.isActive = true").uniqueResult();
    }

    @Override
    public long countEnrolled(Long courseId) {
        Query q = sessionFactory.getCurrentSession()
            .createQuery("SELECT COUNT(e) FROM Enrollment e WHERE e.course.courseId = :c AND e.status = 'ENROLLED'");
        q.setParameter("c", courseId);
        return (Long) q.uniqueResult();
    }
}
