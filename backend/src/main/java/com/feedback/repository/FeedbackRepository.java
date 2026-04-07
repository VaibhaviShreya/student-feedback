package com.feedback.repository;

import com.feedback.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    List<Feedback> findByCourseIdOrderByCreatedAtDesc(Long courseId);
    
    List<Feedback> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    
    Optional<Feedback> findByStudentIdAndCourseId(Long studentId, Long courseId);
    
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.course.id = :courseId")
    Double findAverageRatingByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.course.id = :courseId")
    Long countByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT f.rating, COUNT(f) FROM Feedback f WHERE f.course.id = :courseId GROUP BY f.rating")
    List<Object[]> findRatingDistributionByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT AVG(f.contentRating) FROM Feedback f WHERE f.course.id = :courseId")
    Double findAverageContentRatingByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT AVG(f.instructorRating) FROM Feedback f WHERE f.course.id = :courseId")
    Double findAverageInstructorRatingByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT AVG(f.difficultyRating) FROM Feedback f WHERE f.course.id = :courseId")
    Double findAverageDifficultyRatingByCourseId(@Param("courseId") Long courseId);
}
