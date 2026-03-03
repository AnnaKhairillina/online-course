package com.onlinecourse.controller;

import com.onlinecourse.domain.UserRole;
import com.onlinecourse.dto.AdminUpdateUserForm;
import com.onlinecourse.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin-users";
    }

    @GetMapping("/admin/users/{id}/edit")
    public String editUserPage(@PathVariable Long id, Model model) {
        var user = userService.findById(id);
        var form = new AdminUpdateUserForm();
        form.setName(user.getName());
        form.setRole(user.getRole());

        model.addAttribute("user", user);
        model.addAttribute("form", form);
        model.addAttribute("roles", UserRole.values());
        return "admin-user-edit";
    }

    @PostMapping("/admin/users/{id}/edit")
    public String editUser(@PathVariable Long id,
                           @Valid @ModelAttribute("form") AdminUpdateUserForm form,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userService.findById(id));
            model.addAttribute("roles", UserRole.values());
            return "admin-user-edit";
        }
        userService.updateUser(id, form);
        return "redirect:/admin/users?updated";
    }

    @PostMapping("/admin/users/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("okMessage", "Пользователь удален.");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/users";
    }
}
