package com.university.portal.service.impl;

import com.university.portal.dao.SemesterDAO;
import com.university.portal.model.Semester;
import com.university.portal.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SemesterServiceImpl implements SemesterService {

    @Autowired private SemesterDAO semesterDAO;

    @Override
    @Transactional(readOnly = true)
    public Semester getActiveSemester() { return semesterDAO.findActiveSemester(); }

    @Override
    @Transactional(readOnly = true)
    public List<Semester> getAll() { return semesterDAO.findAll(); }

    @Override
    public Semester create(Semester semester) { return semesterDAO.save(semester); }

    @Override
    public void setActive(Long semId) { semesterDAO.setActive(semId); }
}
