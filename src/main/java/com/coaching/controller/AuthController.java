package com.coaching.controller;

import com.coaching.model.User;
import com.coaching.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            // Validate role
            if (user.getRole() == null) {
                redirectAttributes.addFlashAttribute("error", "Please select a role");
                return "redirect:/register";
            }

            // Only allow STUDENT and TEACHER roles during registration
            if (user.getRole() != User.Role.STUDENT && user.getRole() != User.Role.TEACHER) {
                redirectAttributes.addFlashAttribute("error", "Invalid role selected");
                return "redirect:/register";
            }

            authService.register(user);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }
} 