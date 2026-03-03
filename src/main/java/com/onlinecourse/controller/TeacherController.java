package com.onlinecourse.controller;

import com.onlinecourse.domain.User;
import com.onlinecourse.dto.LessonCreateForm;
import com.onlinecourse.repository.UserRepository;
import com.onlinecourse.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TeacherController {
    private final TeacherService teacherService;
    private final UserRepository userRepository;

    public TeacherController(TeacherService teacherService, UserRepository userRepository) {
        this.teacherService = teacherService;
        this.userRepository = userRepository;
    }

    @GetMapping("/teacher/lessons")
    public String teacherLessons(Model model, Authentication authentication) {
        User teacher = userRepository.findByEmail(authentication.getName()).orElseThrow();

        model.addAttribute("courses", teacherService.findTeacherCourses(teacher.getId()));
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new LessonCreateForm());
        }

        return "teacher-lessons";
    }

    @PostMapping("/teacher/lessons")
    public String createLesson(@Valid @ModelAttribute("form") LessonCreateForm form,
                               BindingResult bindingResult,
                               Authentication authentication,
                               Model model) {
        User teacher = userRepository.findByEmail(authentication.getName()).orElseThrow();

        if (bindingResult.hasErrors()) {
            model.addAttribute("courses", teacherService.findTeacherCourses(teacher.getId()));
            return "teacher-lessons";
        }

        try {
            teacherService.createLesson(teacher.getId(), form);
        } catch (IllegalArgumentException ex) {
            bindingResult.reject("lesson", ex.getMessage());
            model.addAttribute("courses", teacherService.findTeacherCourses(teacher.getId()));
            return "teacher-lessons";
        }

        return "redirect:/teacher/lessons?saved";
    }
}
