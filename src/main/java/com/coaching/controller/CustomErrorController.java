package com.coaching.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        
        model.addAttribute("status", statusCode);
        model.addAttribute("error", exception != null ? exception.getClass().getSimpleName() : "Error");
        model.addAttribute("message", exception != null ? exception.getMessage() : "An unexpected error occurred");
        
        return "error";
    }
} 