package com.feedback.controller;

import com.feedback.dto.FeedbackDTOs.*;
import com.feedback.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    
    private final FeedbackService feedbackService;
    
    @PostMapping
    public ResponseEntity<FeedbackResponse> createFeedback(
            @Valid @RequestBody FeedbackRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(feedbackService.createFeedback(request, userDetails.getUsername()));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponse> updateFeedback(
            @PathVariable Long id,
            @Valid @RequestBody FeedbackRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(feedbackService.updateFeedback(id, request, userDetails.getUsername()));
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<FeedbackResponse>> getFeedbackByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByCourse(courseId));
    }
    
    @GetMapping("/course/{courseId}/stats")
    public ResponseEntity<CourseStats> getCourseStats(@PathVariable Long courseId) {
        return ResponseEntity.ok(feedbackService.getCourseStats(courseId));
    }
    
    @GetMapping("/my")
    public ResponseEntity<List<FeedbackResponse>> getMyFeedback(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(feedbackService.getMyFeedback(userDetails.getUsername()));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        feedbackService.deleteFeedback(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
