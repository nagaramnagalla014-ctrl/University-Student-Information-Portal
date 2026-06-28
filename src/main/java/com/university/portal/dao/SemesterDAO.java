package com.university.portal.dao;

import com.university.portal.model.Semester;
import java.util.List;

public interface SemesterDAO {
    Semester findById(Long semId);
    Semester findActiveSemester();
    List<Semester> findAll();
    Semester save(Semester semester);
    Semester update(Semester semester);
    void setActive(Long semId);
}
