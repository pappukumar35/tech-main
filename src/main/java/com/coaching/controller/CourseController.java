package com.coaching.controller;

import com.coaching.model.Course;
import com.coaching.model.User;
import com.coaching.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public String listCourses(Model model) {
        try {
            model.addAttribute("courses", courseService.getAllCourses());
            return "courses";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading courses. Please try again later.");
            return "courses";
        }
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public String showCreateCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "course-form";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public String saveCourse(@Valid @ModelAttribute Course course, 
                           BindingResult bindingResult,
                           @AuthenticationPrincipal User currentUser,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Please fill all required fields correctly");
            return "course-form";
        }

        try {
            // Set the instructor name to the current user's full name if not provided
            if (course.getInstructor() == null || course.getInstructor().trim().isEmpty()) {
                course.setInstructor(currentUser.getFullName());
            }
            
            courseService.saveCourse(course, currentUser);
            redirectAttributes.addFlashAttribute("success", "Course saved successfully!");
            return "redirect:/courses";
        } catch (Exception e) {
            model.addAttribute("error", "Error saving course: " + e.getMessage());
            return "course-form";
        }
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public String showEditCourseForm(@PathVariable Long id, 
                                   @AuthenticationPrincipal User currentUser,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        try {
            Course course = courseService.getCourseById(id);
            
            // Check if user is authorized to edit the course
            if (currentUser.getRole() != User.Role.ADMIN && 
                !course.getCreatedBy().getId().equals(currentUser.getId())) {
                redirectAttributes.addFlashAttribute("error", "You are not authorized to edit this course");
                return "redirect:/courses";
            }
            
            model.addAttribute("course", course);
            return "course-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Course not found");
            return "redirect:/courses";
        }
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public String deleteCourse(@PathVariable Long id, 
                             @AuthenticationPrincipal User currentUser,
                             RedirectAttributes redirectAttributes) {
        try {
            Course course = courseService.getCourseById(id);
            
            // Check if user is authorized to delete the course
            if (currentUser.getRole() != User.Role.ADMIN && 
                !course.getCreatedBy().getId().equals(currentUser.getId())) {
                redirectAttributes.addFlashAttribute("error", "You are not authorized to delete this course");
                return "redirect:/courses";
            }
            
            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute("success", "Course deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting course: " + e.getMessage());
        }
        return "redirect:/courses";
    }
} 