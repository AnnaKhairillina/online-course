package com.onlinecourse.service;

import com.onlinecourse.domain.Course;
import com.onlinecourse.domain.Lesson;
import com.onlinecourse.dto.LessonCreateForm;
import com.onlinecourse.repository.CourseRepository;
import com.onlinecourse.repository.LessonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeacherService {
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    public TeacherService(CourseRepository courseRepository, LessonRepository lessonRepository) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
    }

    public List<Course> findTeacherCourses(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    @Transactional
    public void createLesson(Long teacherId, LessonCreateForm form) {
        Course course = courseRepository.findById(form.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Курс не найден"));

        if (!course.getTeacher().getId().equals(teacherId)) {
            throw new IllegalArgumentException("Нельзя добавлять урок в чужой курс");
        }

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setTitle(form.getTitle().trim());
        lesson.setContent(form.getContent().trim());
        lesson.setVideoUrl(form.getVideoUrl() == null ? null : form.getVideoUrl().trim());
        lesson.setStartsAt(form.getStartsAt());

        lessonRepository.save(lesson);
    }
}
