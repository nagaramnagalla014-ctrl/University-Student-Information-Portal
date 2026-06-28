package com.university.portal.service;

import com.university.portal.model.Faculty;
import java.util.List;

public interface FacultyService {
    Faculty login(String email, String password);
    Faculty getById(Long facultyId);
    Faculty getByEmail(String email);
    List<Faculty> getAll();
    Faculty register(Faculty faculty);
    Faculty update(Faculty faculty);
    void deactivate(Long facultyId);
    long count();
}
