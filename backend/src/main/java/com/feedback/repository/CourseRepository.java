package com.feedback.repository;

import com.feedback.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    
    List<Course> findByInstructorId(Long instructorId);
    
    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.instructor")
    List<Course> findAllWithInstructor();
    
    boolean existsByCode(String code);
}
