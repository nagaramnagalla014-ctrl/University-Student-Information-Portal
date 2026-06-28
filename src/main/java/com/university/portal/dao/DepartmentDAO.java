package com.university.portal.dao;

import com.university.portal.model.Department;
import java.util.List;

public interface DepartmentDAO {
    Department findById(Long deptId);
    List<Department> findAll();
    Department save(Department department);
    Department update(Department department);
    void delete(Long deptId);
}
