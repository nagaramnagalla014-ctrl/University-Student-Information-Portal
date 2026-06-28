package com.university.portal.dao.impl;

import com.university.portal.dao.StudentDAO;
import com.university.portal.model.Student;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class StudentDAOImpl implements StudentDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Student authenticate(String email, String password) {
        Query<Student> q = sessionFactory.getCurrentSession()
            .createQuery("FROM Student WHERE email = :e AND password = :p AND isActive = true", Student.class);
        q.setParameter("e", email);
        q.setParameter("p", password);
        List<Student> list = q.list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Student findById(Long studentId) {
        return sessionFactory.getCurrentSession().get(Student.class, studentId);
    }

    @Override
    public Student findByEmail(String email) {
        Query<Student> q = sessionFactory.getCurrentSession()
            .createQuery("FROM Student WHERE email = :e", Student.class);
        q.setParameter("e", email);
        List<Student> list = q.list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Student findByStudentCode(String studentCode) {
        Query<Student> q = sessionFactory.getCurrentSession()
            .createQuery("FROM Student WHERE studentCode = :c", Student.class);
        q.setParameter("c", studentCode);
        List<Student> list = q.list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Student> findAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Student ORDER BY firstName", Student.class).list();
    }

    @Override
    public List<Student> findByDepartment(Long deptId) {
        Query<Student> q = sessionFactory.getCurrentSession()
            .createQuery("FROM Student s WHERE s.department.deptId = :d ORDER BY s.firstName", Student.class);
        q.setParameter("d", deptId);
        return q.list();
    }

    @Override
    public List<Student> search(String keyword) {
        String kw = "%" + keyword.toLowerCase() + "%";
        Query<Student> q = sessionFactory.getCurrentSession().createQuery(
            "FROM Student s WHERE LOWER(s.firstName) LIKE :k OR LOWER(s.lastName) LIKE :k OR LOWER(s.studentCode) LIKE :k OR LOWER(s.email) LIKE :k ORDER BY s.firstName",
            Student.class);
        q.setParameter("k", kw);
        return q.list();
    }

    @Override
    public Student save(Student student) {
        sessionFactory.getCurrentSession().save(student);
        return student;
    }

    @Override
    public Student update(Student student) {
        sessionFactory.getCurrentSession().update(student);
        return student;
    }

    @Override
    public void delete(Long studentId) {
        Student s = findById(studentId);
        if (s != null) { s.setIsActive(false); update(s); }
    }

    @Override
    public long countAll() {
        return (Long) sessionFactory.getCurrentSession()
            .createQuery("SELECT COUNT(s) FROM Student s WHERE s.isActive = true").uniqueResult();
    }

    @Override
    public String generateStudentCode() {
        Long count = (Long) sessionFactory.getCurrentSession()
            .createQuery("SELECT COUNT(s) FROM Student s").uniqueResult();
        return String.format("S-%05d", count + 1);
    }
}
