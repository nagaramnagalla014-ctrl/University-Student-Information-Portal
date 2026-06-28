package com.university.portal.service.impl;

import com.university.portal.dao.DepartmentDAO;
import com.university.portal.model.Department;
import com.university.portal.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired private DepartmentDAO departmentDAO;

    @Override
    @Transactional(readOnly = true)
    public List<Department> getAll() { return departmentDAO.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public Department getById(Long deptId) { return departmentDAO.findById(deptId); }

    @Override
    public Department create(Department department) { return departmentDAO.save(department); }

    @Override
    public Department update(Department department) { return departmentDAO.update(department); }
}
