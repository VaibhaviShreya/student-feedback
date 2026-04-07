package com.feedback.service;

import com.feedback.dto.FeedbackDTOs.*;
import com.feedback.entity.Course;
import com.feedback.entity.Feedback;
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
public class FeedbackService {
    
    private final FeedbackRepository feedbackRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public FeedbackResponse createFeedback(FeedbackRequest request, String username) {
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        if (feedbackRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
            throw new RuntimeException("You have already submitted feedback for this course");
        }
        
        Feedback feedback = Feedback.builder()
                .student(student)
                .course(course)
                .rating(request.getRating())
                .contentRating(request.getContentRating())
                .instructorRating(request.getInstructorRating())
                .difficultyRating(request.getDifficultyRating())
                .comment(request.getComment())
                .isAnonymous(request.getIsAnonymous())
                .build();
        
        feedbackRepository.save(feedback);
        return mapToResponse(feedback);
    }
    
    @Transactional
    public FeedbackResponse updateFeedback(Long id, FeedbackRequest request, String username) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        
        if (!feedback.getStudent().getUsername().equals(username)) {
            throw new RuntimeException("You can only update your own feedback");
        }
        
        feedback.setRating(request.getRating());
        feedback.setContentRating(request.getContentRating());
        feedback.setInstructorRating(request.getInstructorRating());
        feedback.setDifficultyRating(request.getDifficultyRating());
        feedback.setComment(request.getComment());
        feedback.setIsAnonymous(request.getIsAnonymous());
        
        feedbackRepository.save(feedback);
        return mapToResponse(feedback);
    }
    
    public List<FeedbackResponse> getFeedbackByCourse(Long courseId) {
        return feedbackRepository.findByCourseIdOrderByCreatedAtDesc(courseId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public List<FeedbackResponse> getMyFeedback(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return feedbackRepository.findByStudentIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public CourseStats getCourseStats(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        RatingDistribution distribution = getRatingDistribution(courseId);
        
        return CourseStats.builder()
                .courseId(courseId)
                .courseName(course.getName())
                .averageRating(feedbackRepository.findAverageRatingByCourseId(courseId))
                .averageContentRating(feedbackRepository.findAverageContentRatingByCourseId(courseId))
                .averageInstructorRating(feedbackRepository.findAverageInstructorRatingByCourseId(courseId))
                .averageDifficultyRating(feedbackRepository.findAverageDifficultyRatingByCourseId(courseId))
                .totalFeedbacks(feedbackRepository.countByCourseId(courseId))
                .ratingDistribution(distribution)
                .build();
    }
    
    private RatingDistribution getRatingDistribution(Long courseId) {
        List<Object[]> results = feedbackRepository.findRatingDistributionByCourseId(courseId);
        
        RatingDistribution distribution = RatingDistribution.builder()
                .oneStar(0L).twoStar(0L).threeStar(0L).fourStar(0L).fiveStar(0L)
                .build();
        
        for (Object[] result : results) {
            Integer rating = (Integer) result[0];
            Long count = (Long) result[1];
            
            switch (rating) {
                case 1 -> distribution.setOneStar(count);
                case 2 -> distribution.setTwoStar(count);
                case 3 -> distribution.setThreeStar(count);
                case 4 -> distribution.setFourStar(count);
                case 5 -> distribution.setFiveStar(count);
            }
        }
        
        return distribution;
    }
    
    @Transactional
    public void deleteFeedback(Long id, String username) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!feedback.getStudent().getId().equals(user.getId()) 
                && user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Unauthorized to delete this feedback");
        }
        
        feedbackRepository.delete(feedback);
    }
    
    private FeedbackResponse mapToResponse(Feedback feedback) {
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .courseId(feedback.getCourse().getId())
                .courseName(feedback.getCourse().getName())
                .studentName(feedback.getIsAnonymous() ? "Anonymous" : feedback.getStudent().getFullName())
                .rating(feedback.getRating())
                .contentRating(feedback.getContentRating())
                .instructorRating(feedback.getInstructorRating())
                .difficultyRating(feedback.getDifficultyRating())
                .comment(feedback.getComment())
                .isAnonymous(feedback.getIsAnonymous())
                .createdAt(feedback.getCreatedAt())
                .build();
    }
}
