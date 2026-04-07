package com.studentfeedback.controller;

import com.studentfeedback.dto.CourseRequest;
import com.studentfeedback.model.Course;
import com.studentfeedback.repository.CourseRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public List<Course> allCourses() {
        return courseRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Course> createCourse(@Valid @RequestBody CourseRequest request) {
        Course course = new Course();
        course.setCode(request.code());
        course.setTitle(request.title());
        course.setInstructor(request.instructor());
        return ResponseEntity.ok(courseRepository.save(course));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable long id) {
        courseRepository.deleteById(Long.valueOf(id));
        return ResponseEntity.noContent().build();
    }
}
