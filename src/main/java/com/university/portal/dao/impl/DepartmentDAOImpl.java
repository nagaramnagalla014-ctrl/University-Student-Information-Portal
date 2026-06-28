package com.university.portal.dao.impl;

import com.university.portal.dao.DepartmentDAO;
import com.university.portal.model.Department;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class DepartmentDAOImpl implements DepartmentDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Department findById(Long deptId) {
        return sessionFactory.getCurrentSession().get(Department.class, deptId);
    }

    @Override
    public List<Department> findAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Department ORDER BY deptName", Department.class).list();
    }

    @Override
    public Department save(Department department) {
        sessionFactory.getCurrentSession().save(department);
        return department;
    }

    @Override
    public Department update(Department department) {
        sessionFactory.getCurrentSession().update(department);
        return department;
    }

    @Override
    public void delete(Long deptId) {
        Department d = findById(deptId);
        if (d != null) sessionFactory.getCurrentSession().delete(d);
    }
}
