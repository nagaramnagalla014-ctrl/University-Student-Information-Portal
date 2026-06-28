package com.university.portal.controller;

import com.university.portal.dto.LoginRequest;
import com.university.portal.model.Faculty;
import com.university.portal.model.Student;
import com.university.portal.service.FacultyService;
import com.university.portal.service.StudentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = Logger.getLogger(AuthController.class);

    @Autowired private StudentService studentService;
    @Autowired private FacultyService facultyService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String role = loginRequest.getRole();
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        try {
            if ("STUDENT".equalsIgnoreCase(role)) {
                Student student = studentService.login(email, password);
                if (student == null) {
                    response.put("success", false);
                    response.put("message", "Invalid credentials");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
                session.setAttribute("userId", student.getStudentId());
                session.setAttribute("role", "STUDENT");
                session.setAttribute("name", student.getFullName());
                response.put("success", true);
                response.put("role", "STUDENT");
                response.put("userId", student.getStudentId());
                response.put("name", student.getFullName());
                response.put("email", student.getEmail());
                log.info("Student login: " + email);

            } else if ("FACULTY".equalsIgnoreCase(role)) {
                Faculty faculty = facultyService.login(email, password);
                if (faculty == null) {
                    response.put("success", false);
                    response.put("message", "Invalid credentials");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
                session.setAttribute("userId", faculty.getFacultyId());
                session.setAttribute("role", "FACULTY");
                session.setAttribute("name", faculty.getFullName());
                response.put("success", true);
                response.put("role", "FACULTY");
                response.put("userId", faculty.getFacultyId());
                response.put("name", faculty.getFullName());
                response.put("email", faculty.getEmail());
                log.info("Faculty login: " + email);

            } else if ("ADMIN".equalsIgnoreCase(role)) {
                // Hardcoded admin for demo purposes
                if ("admin@university.edu".equals(email) && "admin123".equals(password)) {
                    session.setAttribute("userId", 0L);
                    session.setAttribute("role", "ADMIN");
                    session.setAttribute("name", "Admin");
                    response.put("success", true);
                    response.put("role", "ADMIN");
                    response.put("userId", 0);
                    response.put("name", "Admin");
                } else {
                    response.put("success", false);
                    response.put("message", "Invalid admin credentials");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            } else {
                response.put("success", false);
                response.put("message", "Invalid role. Choose STUDENT, FACULTY or ADMIN");
                return ResponseEntity.badRequest().body(response);
            }
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Login error", e);
            response.put("success", false);
            response.put("message", "Server error during login");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role  = (String) session.getAttribute("role");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("authenticated", false));
        }
        Map<String, Object> info = new HashMap<>();
        info.put("authenticated", true);
        info.put("userId", userId);
        info.put("role", role);
        info.put("name", session.getAttribute("name"));
        return ResponseEntity.ok(info);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }
}
