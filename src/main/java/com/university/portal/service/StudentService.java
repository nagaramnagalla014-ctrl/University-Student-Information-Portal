package com.university.portal.service;

import com.university.portal.model.Student;
import java.util.List;

public interface StudentService {
    Student login(String email, String password);
    Student getById(Long studentId);
    Student getByEmail(String email);
    List<Student> getAll();
    List<Student> search(String keyword);
    Student register(Student student);
    Student update(Student student);
    void deactivate(Long studentId);
    long count();
}
