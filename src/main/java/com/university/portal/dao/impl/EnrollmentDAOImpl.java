package com.university.portal.dao.impl;

import com.university.portal.dao.EnrollmentDAO;
import com.university.portal.model.Enrollment;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class EnrollmentDAOImpl implements EnrollmentDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Enrollment findById(Long enrollmentId) {
        return sessionFactory.getCurrentSession().get(Enrollment.class, enrollmentId);
    }

    @Override
    public List<Enrollment> findByStudent(Long studentId) {
        Query<Enrollment> q = sessionFactory.getCurrentSession()
            .createQuery("FROM Enrollment e WHERE e.student.studentId = :s ORDER BY e.enrollmentDate DESC", Enrollment.class);
        q.setParameter("s", studentId);
        return q.list();
    }

    @Override
    public List<Enrollment> findByCourse(Long courseId) {
        Query<Enrollment> q = sessionFactory.getCurrentSession()
            .createQuery("FROM Enrollment e WHERE e.course.courseId = :c ORDER BY e.student.firstName", Enrollment.class);
        q.setParameter("c", courseId);
        return q.list();
    }

    @Override
    public List<Enrollment> findByStudentAndSemester(Long studentId, Long semId) {
        Query<Enrollment> q = sessionFactory.getCurrentSession().createQuery(
            "FROM Enrollment e WHERE e.student.studentId = :s AND e.course.semester.semId = :sem ORDER BY e.course.courseCode",
            Enrollment.class);
        q.setParameter("s", studentId);
        q.setParameter("sem", semId);
        return q.list();
    }

    @Override
    public Enrollment findByStudentAndCourse(Long studentId, Long courseId) {
        Query<Enrollment> q = sessionFactory.getCurrentSession().createQuery(
            "FROM Enrollment e WHERE e.student.studentId = :s AND e.course.courseId = :c",
            Enrollment.class);
        q.setParameter("s", studentId);
        q.setParameter("c", courseId);
        List<Enrollment> list = q.list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Enrollment save(Enrollment enrollment) {
        sessionFactory.getCurrentSession().save(enrollment);
        return enrollment;
    }

    @Override
    public Enrollment update(Enrollment enrollment) {
        sessionFactory.getCurrentSession().update(enrollment);
        return enrollment;
    }

    @Override
    public void drop(Long enrollmentId) {
        Enrollment e = findById(enrollmentId);
        if (e != null) { e.setStatus("DROPPED"); update(e); }
    }

    @Override
    public boolean isAlreadyEnrolled(Long studentId, Long courseId) {
        Query q = sessionFactory.getCurrentSession().createQuery(
            "SELECT COUNT(e) FROM Enrollment e WHERE e.student.studentId = :s AND e.course.courseId = :c AND e.status = 'ENROLLED'");
        q.setParameter("s", studentId);
        q.setParameter("c", courseId);
        return (Long) q.uniqueResult() > 0;
    }

    @Override
    public long countEnrolledInCourse(Long courseId) {
        Query q = sessionFactory.getCurrentSession().createQuery(
            "SELECT COUNT(e) FROM Enrollment e WHERE e.course.courseId = :c AND e.status = 'ENROLLED'");
        q.setParameter("c", courseId);
        return (Long) q.uniqueResult();
    }
}
