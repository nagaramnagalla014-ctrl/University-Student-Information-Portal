package com.university.portal.controller;

import com.university.portal.model.Course;
import com.university.portal.service.CourseService;
import com.university.portal.service.DepartmentService;
import com.university.portal.service.FacultyService;
import com.university.portal.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired private CourseService courseService;
    @Autowired private DepartmentService departmentService;
    @Autowired private FacultyService facultyService;
    @Autowired private SemesterService semesterService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(courseService.getAll());
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailable() {
        return ResponseEntity.ok(courseService.getAvailable());
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<?> getById(@PathVariable Long courseId) {
        Course course = courseService.getById(courseId);
        if (course == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(course);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String keyword) {
        return ResponseEntity.ok(courseService.search(keyword));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body) {
        try {
            Course course = new Course();
            course.setCourseName((String) body.get("courseName"));
            course.setCourseCode((String) body.get("courseCode"));
            course.setCredits((Integer) body.get("credits"));
            course.setDescription((String) body.get("description"));
            course.setMaxStudents((Integer) body.get("maxStudents"));
            if (body.get("deptId") != null)
                course.setDepartment(departmentService.getById(Long.valueOf(body.get("deptId").toString())));
            if (body.get("facultyId") != null)
                course.setFaculty(facultyService.getById(Long.valueOf(body.get("facultyId").toString())));
            if (body.get("semId") != null)
                course.setSemester(semesterService.getAll().stream()
                    .filter(s -> s.getSemId().equals(Long.valueOf(body.get("semId").toString())))
                    .findFirst().orElse(null));
            return ResponseEntity.ok(courseService.create(course));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<?> update(@PathVariable Long courseId, @RequestBody Map<String, Object> body) {
        Course course = courseService.getById(courseId);
        if (course == null) return ResponseEntity.notFound().build();
        if (body.get("courseName") != null) course.setCourseName((String) body.get("courseName"));
        if (body.get("description") != null) course.setDescription((String) body.get("description"));
        if (body.get("maxStudents") != null) course.setMaxStudents((Integer) body.get("maxStudents"));
        if (body.get("facultyId") != null)
            course.setFaculty(facultyService.getById(Long.valueOf(body.get("facultyId").toString())));
        return ResponseEntity.ok(courseService.update(course));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> delete(@PathVariable Long courseId) {
        courseService.delete(courseId);
        return ResponseEntity.ok(Map.of("success", true, "message", "Course deleted"));
    }
}
