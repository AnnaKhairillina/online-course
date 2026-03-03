package com.onlinecourse.service;

import com.onlinecourse.dto.StudentCourseView;
import com.onlinecourse.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class StudentService {
    private final EnrollmentRepository enrollmentRepository;

    public StudentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public StudentDashboardData getDashboard(Long userId) {
        List<StudentCourseView> courses = enrollmentRepository.findStudentCourses(userId);
        BigDecimal totalPaid = courses.stream()
                .map(StudentCourseView::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double averageProgress = courses.stream()
                .mapToInt(StudentCourseView::progress)
                .average()
                .orElse(0.0);

        return new StudentDashboardData(courses, totalPaid,
                BigDecimal.valueOf(averageProgress).setScale(1, RoundingMode.HALF_UP));
    }

    public record StudentDashboardData(List<StudentCourseView> courses,
                                       BigDecimal totalPaid,
                                       BigDecimal averageProgress) {
    }
}
