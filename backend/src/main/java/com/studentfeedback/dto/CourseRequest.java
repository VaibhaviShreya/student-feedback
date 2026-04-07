package com.studentfeedback.dto;

import jakarta.validation.constraints.NotBlank;

public record CourseRequest(@NotBlank String code, @NotBlank String title, String instructor) {
}
