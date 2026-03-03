package com.onlinecourse.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StudentCourseView(Long enrollmentId,
                                Long courseId,
                                String title,
                                String subject,
                                BigDecimal price,
                                Integer progress,
                                LocalDateTime enrolledAt) {
}
