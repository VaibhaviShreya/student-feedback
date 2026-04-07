package com.studentfeedback.config;

import com.studentfeedback.model.Course;
import com.studentfeedback.model.Role;
import com.studentfeedback.model.User;
import com.studentfeedback.repository.CourseRepository;
import com.studentfeedback.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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

        if (courseRepository.count() == 0) {
            Course c1 = new Course();
            c1.setCode("CS101");
            c1.setTitle("Introduction to Programming");
            c1.setInstructor("Dr. Smith");
            courseRepository.save(c1);
        }
    }
}
