package com.feedback.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

public class CourseDTOs {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CourseRequest {
        @NotBlank(message = "Course name is required")
        @Size(max = 100)
        private String name;
        
        @Size(max = 20)
        private String code;
        
        @Size(max = 500)
        private String description;
        
        private Long instructorId;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CourseResponse {
        private Long id;
        private String name;
        private String code;
        private String description;
        private String instructorName;
        private Double averageRating;
        private Long feedbackCount;
        private LocalDateTime createdAt;
    }
}
