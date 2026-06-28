package com.university.portal.dao.impl;

import com.university.portal.dao.SemesterDAO;
import com.university.portal.model.Semester;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class SemesterDAOImpl implements SemesterDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Semester findById(Long semId) {
        return sessionFactory.getCurrentSession().get(Semester.class, semId);
    }

    @Override
    public Semester findActiveSemester() {
        Query<Semester> q = sessionFactory.getCurrentSession()
            .createQuery("FROM Semester WHERE isActive = true", Semester.class);
        List<Semester> list = q.list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Semester> findAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Semester ORDER BY startDate DESC", Semester.class).list();
    }

    @Override
    public Semester save(Semester semester) {
        sessionFactory.getCurrentSession().save(semester);
        return semester;
    }

    @Override
    public Semester update(Semester semester) {
        sessionFactory.getCurrentSession().update(semester);
        return semester;
    }

    @Override
    public void setActive(Long semId) {
        sessionFactory.getCurrentSession()
            .createQuery("UPDATE Semester SET isActive = false").executeUpdate();
        Semester s = findById(semId);
        if (s != null) { s.setIsActive(true); update(s); }
    }
}
