package com.coaching.service;

import com.coaching.model.Course;
import com.coaching.model.User;
import com.coaching.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }
    
    public Course saveCourse(Course course, User currentUser) {
        course.setCreatedBy(currentUser);
        return courseRepository.save(course);
    }
    
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found");
        }
        courseRepository.deleteById(id);
    }

    public List<Course> getCoursesByInstructor(User instructor) {
        return courseRepository.findByCreatedBy(instructor);
    }
} 