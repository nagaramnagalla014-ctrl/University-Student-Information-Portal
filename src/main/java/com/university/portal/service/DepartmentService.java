package com.university.portal.service;

import com.university.portal.model.Department;
import java.util.List;

public interface DepartmentService {
    List<Department> getAll();
    Department getById(Long deptId);
    Department create(Department department);
    Department update(Department department);
}
