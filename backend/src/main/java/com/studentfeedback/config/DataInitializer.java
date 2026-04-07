package com.studentfeedback.config;

import com.studentfeedback.model.Course;
import com.studentfeedback.model.Role;
import com.studentfeedback.model.User;
import com.studentfeedback.repository.CourseRepository;
import com.studentfeedback.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, CourseRepository courseRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }

        if (!userRepository.existsByUsername("teacher")) {
            User teacher = new User();
            teacher.setUsername("teacher");
            teacher.setPassword(passwordEncoder.encode("teacher123"));
            teacher.setRole(Role.TEACHER);
            userRepository.save(teacher);
        }

        if (courseRepository.count() == 0) {
            courseRepository.saveAll(List.of(
                    buildCourse("CS101", "Introduction to Programming", "Dr. Smith"),
                    buildCourse("CS205", "Data Structures", "Prof. Patel"),
                    buildCourse("CS310", "Database Systems", "Dr. Chen"),
                    buildCourse("CS402", "Software Engineering", "Prof. Williams")
            ));
        }
    }

    private Course buildCourse(String code, String title, String instructor) {
        Course c = new Course();
        c.setCode(code);
        c.setTitle(title);
        c.setInstructor(instructor);
        return c;
    }
}
