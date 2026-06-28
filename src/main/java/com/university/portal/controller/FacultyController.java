package com.university.portal.controller;

import com.university.portal.dto.AttendanceUpdateDTO;
import com.university.portal.dto.GradeUpdateDTO;
import com.university.portal.model.Course;
import com.university.portal.model.Enrollment;
import com.university.portal.model.Faculty;
import com.university.portal.service.CourseService;
import com.university.portal.service.EnrollmentService;
import com.university.portal.service.FacultyService;
import com.university.portal.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/faculty")
public class FacultyController {

    @Autowired private FacultyService facultyService;
    @Autowired private CourseService courseService;
    @Autowired private EnrollmentService enrollmentService;
    @Autowired private NotificationService notificationService;

    @GetMapping("/{facultyId}")
    public ResponseEntity<?> getProfile(@PathVariable Long facultyId, HttpSession session) {
        if (!isAuthorized(session, facultyId)) return unauthorized();
        Faculty faculty = facultyService.getById(facultyId);
        if (faculty == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/{facultyId}/courses")
    public ResponseEntity<?> getCourses(@PathVariable Long facultyId, HttpSession session) {
        if (!isAuthorized(session, facultyId)) return unauthorized();
        List<Course> courses = courseService.getByFaculty(facultyId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/courses/{courseId}/students")
    public ResponseEntity<?> getEnrolledStudents(@PathVariable Long courseId, HttpSession session) {
        if (!"FACULTY".equals(session.getAttribute("role")) && !"ADMIN".equals(session.getAttribute("role")))
            return unauthorized();
        List<Enrollment> enrollments = enrollmentService.getByCourse(courseId);
        return ResponseEntity.ok(enrollments);
    }

    @PutMapping("/grades")
    public ResponseEntity<?> uploadGrade(@RequestBody GradeUpdateDTO dto, HttpSession session) {
        if (!"FACULTY".equals(session.getAttribute("role")) && !"ADMIN".equals(session.getAttribute("role")))
            return unauthorized();
        try {
            Enrollment updated = enrollmentService.updateGrade(
                dto.getEnrollmentId(), dto.getGrade(), dto.getGradePoints(), dto.getRemarks());
            // Notify student
            notificationService.send(
                updated.getStudent().getStudentId(), "STUDENT",
                "Grade Updated",
                "Your grade for " + updated.getCourse().getCourseName() + " has been updated to: " + dto.getGrade(),
                "GRADE_UPDATED");
            return ResponseEntity.ok(Map.of("success", true, "enrollment", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/attendance")
    public ResponseEntity<?> updateAttendance(@RequestBody AttendanceUpdateDTO dto, HttpSession session) {
        if (!"FACULTY".equals(session.getAttribute("role")) && !"ADMIN".equals(session.getAttribute("role")))
            return unauthorized();
        try {
            Enrollment updated = enrollmentService.updateAttendance(
                dto.getEnrollmentId(), dto.getAttendancePercentage());
            return ResponseEntity.ok(Map.of("success", true, "enrollment", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    private boolean isAuthorized(HttpSession session, Long facultyId) {
        String role = (String) session.getAttribute("role");
        Long userId = (Long) session.getAttribute("userId");
        return "ADMIN".equals(role) || ("FACULTY".equals(role) && facultyId.equals(userId));
    }

    private ResponseEntity<?> unauthorized() {
        return ResponseEntity.status(403).body(Map.of("success", false, "message", "Access denied"));
    }
}
