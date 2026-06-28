package com.university.portal.controller;

import com.university.portal.model.*;
import com.university.portal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private StudentService studentService;
    @Autowired private FacultyService facultyService;
    @Autowired private CourseService courseService;
    @Autowired private DepartmentService departmentService;
    @Autowired private SemesterService semesterService;
    @Autowired private EnrollmentService enrollmentService;

    // ---- Stats ----
    @GetMapping("/stats")
    public ResponseEntity<?> getStats(HttpSession session) {
        if (!isAdmin(session)) return unauthorized();
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", studentService.count());
        stats.put("totalFaculty", facultyService.count());
        stats.put("totalCourses", courseService.count());
        stats.put("departments", departmentService.getAll().size());
        stats.put("activeSemester", semesterService.getActiveSemester());
        return ResponseEntity.ok(stats);
    }

    // ---- Students ----
    @GetMapping("/students")
    public ResponseEntity<?> listStudents(HttpSession session) {
        if (!isAdmin(session)) return unauthorized();
        return ResponseEntity.ok(studentService.getAll());
    }

    @PostMapping("/students")
    public ResponseEntity<?> createStudent(@RequestBody Student student, HttpSession session) {
        if (!isAdmin(session)) return unauthorized();
        try {
            return ResponseEntity.ok(studentService.register(student));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/students/{studentId}")
    public ResponseEntity<?> updateStudent(@PathVariable Long studentId,
                                           @RequestBody Student studentData,
                                           HttpSession session) {
        if (!isAdmin(session)) return unauthorized();
        Student existing = studentService.getById(studentId);
        if (existing == null) return ResponseEntity.notFound().build();
        existing.setFirstName(studentData.getFirstName());
        existing.setLastName(studentData.getLastName());
        existing.setPhone(studentData.getPhone());
        existing.setProgram(studentData.getProgram());
        return ResponseEntity.ok(studentService.update(existing));
    }

    @DeleteMapping("/students/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long studentId, HttpSession session) {
        if (!isAdmin(session)) return unauthorized();
        studentService.deactivate(studentId);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // ---- Faculty ----
    @GetMapping("/faculty")
    public ResponseEntity<?> listFaculty(HttpSession session) {
        if (!isAdmin(session)) return unauthorized();
        return ResponseEntity.ok(facultyService.getAll());
    }

    @PostMapping("/faculty")
    public ResponseEntity<?> createFaculty(@RequestBody Faculty faculty, HttpSession session) {
        if (!isAdmin(session)) return unauthorized();
        try {
            return ResponseEntity.ok(facultyService.register(faculty));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // ---- Departments ----
    @GetMapping("/departments")
    public ResponseEntity<?> listDepartments(HttpSession session) {
        if (!isAdmin(session)) return unauthorized();
        return ResponseEntity.ok(departmentService.getAll());
    }

    @PostMapping("/departments")
    public ResponseEntity<?> createDepartment(@RequestBody Department department, HttpSession session) {
        if (!isAdmin(session)) return unauthorized();
        return ResponseEntity.ok(departmentService.create(department));
    }

    // ---- Semesters ----
    @GetMapping("/semesters")
    public ResponseEntity<?> listSemesters(HttpSession session) {
        if (!isAdmin(session)) return unauthorized();
        return ResponseEntity.ok(semesterService.getAll());
    }

    @PostMapping("/semesters")
    public ResponseEntity<?> createSemester(@RequestBody Semester semester, HttpSession session) {
        if (!isAdmin(session)) return unauthorized();
        return ResponseEntity.ok(semesterService.create(semester));
    }

    @PutMapping("/semesters/{semId}/activate")
    public ResponseEntity<?> activateSemester(@PathVariable Long semId, HttpSession session) {
        if (!isAdmin(session)) return unauthorized();
        semesterService.setActive(semId);
        return ResponseEntity.ok(Map.of("success", true, "message", "Semester activated"));
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("role"));
    }

    private ResponseEntity<?> unauthorized() {
        return ResponseEntity.status(403).body(Map.of("success", false, "message", "Admin access required"));
    }
}
