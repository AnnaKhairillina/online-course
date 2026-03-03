package com.onlinecourse.controller;

import com.onlinecourse.domain.User;
import com.onlinecourse.repository.UserRepository;
import com.onlinecourse.service.StudentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {
    private final UserRepository userRepository;
    private final StudentService studentService;

    public StudentController(UserRepository userRepository, StudentService studentService) {
        this.userRepository = userRepository;
        this.studentService = studentService;
    }

    @GetMapping("/student/home")
    public String studentHome(Authentication authentication, Model model) {
        User student = userRepository.findByEmail(authentication.getName()).orElseThrow();
        var data = studentService.getDashboard(student.getId());
        model.addAttribute("data", data);
        return "student-home";
    }

    @GetMapping("/student/courses")
    public String studentCourses(Authentication authentication, Model model) {
        User student = userRepository.findByEmail(authentication.getName()).orElseThrow();
        var data = studentService.getDashboard(student.getId());
        model.addAttribute("data", data);
        return "student-courses";
    }

    @GetMapping("/student/progress")
    public String studentProgress(Authentication authentication, Model model) {
        User student = userRepository.findByEmail(authentication.getName()).orElseThrow();
        var data = studentService.getDashboard(student.getId());
        model.addAttribute("data", data);
        return "student-progress";
    }
}
