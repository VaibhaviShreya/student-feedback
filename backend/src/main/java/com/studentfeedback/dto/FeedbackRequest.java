package com.studentfeedback.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record FeedbackRequest(long courseId, @Min(1) @Max(5) int rating, String comment) {
}
