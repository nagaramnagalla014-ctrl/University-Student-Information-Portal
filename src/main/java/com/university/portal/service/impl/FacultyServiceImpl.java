package com.university.portal.service.impl;

import com.university.portal.dao.FacultyDAO;
import com.university.portal.model.Faculty;
import com.university.portal.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class FacultyServiceImpl implements FacultyService {

    @Autowired private FacultyDAO facultyDAO;

    @Override
    public Faculty login(String email, String password) {
        return facultyDAO.authenticate(email, password);
    }

    @Override
    @Transactional(readOnly = true)
    public Faculty getById(Long facultyId) {
        return facultyDAO.findById(facultyId);
    }

    @Override
    @Transactional(readOnly = true)
    public Faculty getByEmail(String email) {
        return facultyDAO.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Faculty> getAll() {
        return facultyDAO.findAll();
    }

    @Override
    public Faculty register(Faculty faculty) {
        faculty.setFacultyCode(facultyDAO.generateFacultyCode());
        return facultyDAO.save(faculty);
    }

    @Override
    public Faculty update(Faculty faculty) {
        return facultyDAO.update(faculty);
    }

    @Override
    public void deactivate(Long facultyId) {
        facultyDAO.delete(facultyId);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return facultyDAO.countAll();
    }
}
