package com.university.portal.controller;

import com.university.portal.dto.AttendanceUpdateDTO;
import com.university.portal.dto.GradeUpdateDTO;
import com.university.portal.model.Enrollment;
import com.university.portal.model.Student;
import com.university.portal.service.EnrollmentService;
import com.university.portal.service.NotificationService;
import com.university.portal.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired private StudentService studentService;
    @Autowired private EnrollmentService enrollmentService;
    @Autowired private NotificationService notificationService;

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getProfile(@PathVariable Long studentId, HttpSession session) {
        if (!isAuthorized(session, studentId)) return unauthorized();
        Student student = studentService.getById(studentId);
        if (student == null) return notFound("Student not found");
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<?> updateProfile(@PathVariable Long studentId,
                                           @RequestBody Student studentData,
                                           HttpSession session) {
        if (!isAuthorized(session, studentId)) return unauthorized();
        Student existing = studentService.getById(studentId);
        if (existing == null) return notFound("Student not found");
        existing.setPhone(studentData.getPhone());
        existing.setProgram(studentData.getProgram());
        return ResponseEntity.ok(studentService.update(existing));
    }

    @GetMapping("/{studentId}/enrollments")
    public ResponseEntity<?> getEnrollments(@PathVariable Long studentId, HttpSession session) {
        if (!isAuthorized(session, studentId)) return unauthorized();
        List<Enrollment> enrollments = enrollmentService.getByStudent(studentId);
        return ResponseEntity.ok(enrollments);
    }

    @PostMapping("/{studentId}/enroll/{courseId}")
    public ResponseEntity<?> enroll(@PathVariable Long studentId,
                                    @PathVariable Long courseId,
                                    HttpSession session) {
        if (!isAuthorized(session, studentId)) return unauthorized();
        Map<String, Object> response = new HashMap<>();
        try {
            Enrollment enrollment = enrollmentService.enroll(studentId, courseId);
            notificationService.send(studentId, "STUDENT",
                "Course Enrollment Confirmed",
                "You have been enrolled in: " + enrollment.getCourse().getCourseName(),
                "ENROLLMENT");
            response.put("success", true);
            response.put("enrollment", enrollment);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{studentId}/enroll/{enrollmentId}")
    public ResponseEntity<?> drop(@PathVariable Long studentId,
                                  @PathVariable Long enrollmentId,
                                  HttpSession session) {
        if (!isAuthorized(session, studentId)) return unauthorized();
        enrollmentService.drop(enrollmentId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Course dropped successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{studentId}/notifications")
    public ResponseEntity<?> getNotifications(@PathVariable Long studentId, HttpSession session) {
        if (!isAuthorized(session, studentId)) return unauthorized();
        return ResponseEntity.ok(notificationService.getByRecipient(studentId, "STUDENT"));
    }

    @PutMapping("/{studentId}/notifications/read-all")
    public ResponseEntity<?> markAllRead(@PathVariable Long studentId, HttpSession session) {
        if (!isAuthorized(session, studentId)) return unauthorized();
        notificationService.markAllAsRead(studentId, "STUDENT");
        return ResponseEntity.ok(Map.of("success", true));
    }

    private boolean isAuthorized(HttpSession session, Long studentId) {
        String role = (String) session.getAttribute("role");
        Long userId = (Long) session.getAttribute("userId");
        return "ADMIN".equals(role) || ("STUDENT".equals(role) && studentId.equals(userId));
    }

    private ResponseEntity<?> unauthorized() {
        return ResponseEntity.status(403).body(Map.of("success", false, "message", "Access denied"));
    }

    private ResponseEntity<?> notFound(String msg) {
        return ResponseEntity.status(404).body(Map.of("success", false, "message", msg));
    }
}
