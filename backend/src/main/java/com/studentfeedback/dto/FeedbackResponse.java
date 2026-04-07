package com.studentfeedback.dto;

public record FeedbackResponse(
        Long id,
        Long courseId,
        String courseCode,
        String courseTitle,
        String studentUsername,
        Integer rating,
        String comment
) {
}
