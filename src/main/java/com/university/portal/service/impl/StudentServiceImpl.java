package com.university.portal.service.impl;

import com.university.portal.dao.StudentDAO;
import com.university.portal.model.Student;
import com.university.portal.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    @Autowired private StudentDAO studentDAO;

    @Override
    public Student login(String email, String password) {
        return studentDAO.authenticate(email, password);
    }

    @Override
    @Transactional(readOnly = true)
    public Student getById(Long studentId) {
        return studentDAO.findById(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Student getByEmail(String email) {
        return studentDAO.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> getAll() {
        return studentDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> search(String keyword) {
        return studentDAO.search(keyword);
    }

    @Override
    public Student register(Student student) {
        student.setStudentCode(studentDAO.generateStudentCode());
        return studentDAO.save(student);
    }

    @Override
    public Student update(Student student) {
        return studentDAO.update(student);
    }

    @Override
    public void deactivate(Long studentId) {
        studentDAO.delete(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return studentDAO.countAll();
    }
}
