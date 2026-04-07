package com.feedback.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

public class FeedbackDTOs {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeedbackRequest {
        @NotNull(message = "Course ID is required")
        private Long courseId;
        
        @NotNull(message = "Rating is required")
        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating must be at most 5")
        private Integer rating;
        
        @Min(1) @Max(5)
        private Integer contentRating;
        
        @Min(1) @Max(5)
        private Integer instructorRating;
        
        @Min(1) @Max(5)
        private Integer difficultyRating;
        
        @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
        private String comment;
        
        private Boolean isAnonymous = false;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FeedbackResponse {
        private Long id;
        private Long courseId;
        private String courseName;
        private String studentName;
        private Integer rating;
        private Integer contentRating;
        private Integer instructorRating;
        private Integer difficultyRating;
        private String comment;
        private Boolean isAnonymous;
        private LocalDateTime createdAt;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CourseStats {
        private Long courseId;
        private String courseName;
        private Double averageRating;
        private Double averageContentRating;
        private Double averageInstructorRating;
        private Double averageDifficultyRating;
        private Long totalFeedbacks;
        private RatingDistribution ratingDistribution;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RatingDistribution {
        private Long oneStar;
        private Long twoStar;
        private Long threeStar;
        private Long fourStar;
        private Long fiveStar;
    }
}
