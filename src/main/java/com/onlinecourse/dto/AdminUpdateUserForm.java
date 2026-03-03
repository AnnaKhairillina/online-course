package com.onlinecourse.dto;

import com.onlinecourse.domain.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AdminUpdateUserForm {
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @NotNull
    private UserRole role;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}
