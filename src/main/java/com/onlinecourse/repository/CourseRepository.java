package com.onlinecourse.repository;

import com.onlinecourse.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTeacherId(Long teacherId);
    Optional<Course> findByTitle(String title);

    @Query("SELECT DISTINCT c.subject FROM Course c ORDER BY c.subject")
    List<String> findDistinctSubjects();
}
