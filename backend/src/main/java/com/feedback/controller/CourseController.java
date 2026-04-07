package com.feedback.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")  // ✅ matches your frontend URL
public class CourseController {

    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        // temporary hardcoded response to test
        return ResponseEntity.ok(List.of("Course A", "Course B"));
    }
}