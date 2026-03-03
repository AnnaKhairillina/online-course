package com.onlinecourse.dto;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class LessonCreateForm {
    @NotNull(message = "Выберите курс")
    private Long courseId;

    @NotBlank(message = "Введите название занятия")
    @Size(max = 255, message = "Название не должно быть длиннее 255 символов")
    private String title;

    @NotBlank(message = "Заполните контент урока")
    private String content;

    @NotBlank(message = "Укажите ссылку на видео")
    @Size(max = 500, message = "Ссылка не должна быть длиннее 500 символов")
    private String videoUrl;

    @NotNull(message = "Укажите дату и время занятия")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startsAt;

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public LocalDateTime getStartsAt() { return startsAt; }
    public void setStartsAt(LocalDateTime startsAt) { this.startsAt = startsAt; }
}
