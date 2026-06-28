package com.university.portal.dao;

import com.university.portal.model.Student;
import java.util.List;

public interface StudentDAO {
    Student authenticate(String email, String password);
    Student findById(Long studentId);
    Student findByEmail(String email);
    Student findByStudentCode(String studentCode);
    List<Student> findAll();
    List<Student> findByDepartment(Long deptId);
    List<Student> search(String keyword);
    Student save(Student student);
    Student update(Student student);
    void delete(Long studentId);
    long countAll();
    String generateStudentCode();
}
