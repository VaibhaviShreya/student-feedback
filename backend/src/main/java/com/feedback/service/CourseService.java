package com.feedback.service;

import com.feedback.dto.CourseDTOs.*;
import com.feedback.entity.Course;
import com.feedback.entity.User;
import com.feedback.repository.CourseRepository;
import com.feedback.repository.FeedbackRepository;
import com.feedback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {
    
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    
    @Transactional
    public CourseResponse createCourse(CourseRequest request) {
        if (request.getCode() != null && courseRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Course code already exists");
        }
        
        Course course = Course.builder()
                .name(request.getName())
                .code(request.getCode())
                .description(request.getDescription())
                .build();
        
        if (request.getInstructorId() != null) {
            User instructor = userRepository.findById(request.getInstructorId())
                    .orElseThrow(() -> new RuntimeException("Instructor not found"));
            course.setInstructor(instructor);
        }
        
        courseRepository.save(course);
        return mapToResponse(course);
    }
    
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAllWithInstructor()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return mapToResponse(course);
    }
    
    @Transactional
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        course.setName(request.getName());
        course.setCode(request.getCode());
        course.setDescription(request.getDescription());
        
        if (request.getInstructorId() != null) {
            User instructor = userRepository.findById(request.getInstructorId())
                    .orElseThrow(() -> new RuntimeException("Instructor not found"));
            course.setInstructor(instructor);
        }
        
        courseRepository.save(course);
        return mapToResponse(course);
    }
    
    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found");
        }
        courseRepository.deleteById(id);
    }
    
    private CourseResponse mapToResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .code(course.getCode())
                .description(course.getDescription())
                .instructorName(course.getInstructor() != null ? course.getInstructor().getFullName() : null)
                .averageRating(feedbackRepository.findAverageRatingByCourseId(course.getId()))
                .feedbackCount(feedbackRepository.countByCourseId(course.getId()))
                .createdAt(course.getCreatedAt())
                .build();
    }
}
