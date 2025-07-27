package com.coaching.repository;

import com.coaching.model.Course;
import com.coaching.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCreatedBy(User instructor);
} 