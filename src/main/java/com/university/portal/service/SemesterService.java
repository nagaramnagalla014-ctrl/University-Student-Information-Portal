package com.university.portal.service;

import com.university.portal.model.Semester;
import java.util.List;

public interface SemesterService {
    Semester getActiveSemester();
    List<Semester> getAll();
    Semester create(Semester semester);
    void setActive(Long semId);
}
