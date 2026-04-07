package com.studentfeedback.controller;

import com.studentfeedback.dto.FeedbackRequest;
import com.studentfeedback.dto.FeedbackResponse;
import com.studentfeedback.model.Course;
import com.studentfeedback.model.Feedback;
import com.studentfeedback.model.User;
import com.studentfeedback.repository.CourseRepository;
import com.studentfeedback.repository.FeedbackRepository;
import com.studentfeedback.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    private final FeedbackRepository feedbackRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public FeedbackController(FeedbackRepository feedbackRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<FeedbackResponse> allFeedback(@RequestParam(required = false) Long courseId) {
        List<Feedback> feedbackList = (courseId != null)
                ? feedbackRepository.findByCourseId(courseId)
                : feedbackRepository.findAll();

        return feedbackList.stream().map(this::toResponse).toList();
    }

    @PostMapping
    public ResponseEntity<FeedbackResponse> submitFeedback(@Valid @RequestBody FeedbackRequest request, Authentication authentication) {
        Course course = courseRepository.findById(request.courseId()).orElseThrow();
        User student = userRepository.findByUsername(authentication.getName()).orElseThrow();

        Feedback feedback = new Feedback();
        feedback.setCourse(course);
        feedback.setStudent(student);
        feedback.setRating(request.rating());
        feedback.setComment(request.comment());

        return ResponseEntity.ok(toResponse(feedbackRepository.save(feedback)));
    }

    private FeedbackResponse toResponse(Feedback feedback) {
        return new FeedbackResponse(
                feedback.getId(),
                feedback.getCourse().getId(),
                feedback.getCourse().getCode(),
                feedback.getCourse().getTitle(),
                feedback.getStudent().getUsername(),
                feedback.getRating(),
                feedback.getComment()
        );
    }
}
