package com.onlinecourse.repository;

import com.onlinecourse.domain.Enrollment;
import com.onlinecourse.dto.StudentCourseView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @Modifying
    @Query("DELETE FROM Enrollment e WHERE e.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Query("""
            SELECT new com.onlinecourse.dto.StudentCourseView(
                e.id,
                c.id,
                c.title,
                c.subject,
                c.price,
                e.progress,
                e.enrolledAt
            )
            FROM Enrollment e
            JOIN e.course c
            WHERE e.user.id = :userId
            ORDER BY e.enrolledAt DESC
            """)
    List<StudentCourseView> findStudentCourses(@Param("userId") Long userId);
}
