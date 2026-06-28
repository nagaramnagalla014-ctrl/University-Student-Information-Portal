package com.university.portal.dao.impl;

import com.university.portal.dao.FacultyDAO;
import com.university.portal.model.Faculty;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class FacultyDAOImpl implements FacultyDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Faculty authenticate(String email, String password) {
        Query<Faculty> q = sessionFactory.getCurrentSession()
            .createQuery("FROM Faculty WHERE email = :e AND password = :p AND isActive = true", Faculty.class);
        q.setParameter("e", email);
        q.setParameter("p", password);
        List<Faculty> list = q.list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Faculty findById(Long facultyId) {
        return sessionFactory.getCurrentSession().get(Faculty.class, facultyId);
    }

    @Override
    public Faculty findByEmail(String email) {
        Query<Faculty> q = sessionFactory.getCurrentSession()
            .createQuery("FROM Faculty WHERE email = :e", Faculty.class);
        q.setParameter("e", email);
        List<Faculty> list = q.list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Faculty> findAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Faculty ORDER BY firstName", Faculty.class).list();
    }

    @Override
    public List<Faculty> findByDepartment(Long deptId) {
        Query<Faculty> q = sessionFactory.getCurrentSession()
            .createQuery("FROM Faculty f WHERE f.department.deptId = :d ORDER BY f.firstName", Faculty.class);
        q.setParameter("d", deptId);
        return q.list();
    }

    @Override
    public Faculty save(Faculty faculty) {
        sessionFactory.getCurrentSession().save(faculty);
        return faculty;
    }

    @Override
    public Faculty update(Faculty faculty) {
        sessionFactory.getCurrentSession().update(faculty);
        return faculty;
    }

    @Override
    public void delete(Long facultyId) {
        Faculty f = findById(facultyId);
        if (f != null) { f.setIsActive(false); update(f); }
    }

    @Override
    public long countAll() {
        return (Long) sessionFactory.getCurrentSession()
            .createQuery("SELECT COUNT(f) FROM Faculty f WHERE f.isActive = true").uniqueResult();
    }

    @Override
    public String generateFacultyCode() {
        Long count = (Long) sessionFactory.getCurrentSession()
            .createQuery("SELECT COUNT(f) FROM Faculty f").uniqueResult();
        return String.format("FAC-%03d", count + 1);
    }
}
