package com.university.portal.dao;

import com.university.portal.model.Faculty;
import java.util.List;

public interface FacultyDAO {
    Faculty authenticate(String email, String password);
    Faculty findById(Long facultyId);
    Faculty findByEmail(String email);
    List<Faculty> findAll();
    List<Faculty> findByDepartment(Long deptId);
    Faculty save(Faculty faculty);
    Faculty update(Faculty faculty);
    void delete(Long facultyId);
    long countAll();
    String generateFacultyCode();
}
