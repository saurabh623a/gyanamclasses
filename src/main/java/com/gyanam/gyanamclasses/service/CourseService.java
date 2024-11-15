package com.gyanam.gyanamclasses.service;

import com.gyanam.gyanamclasses.model.Course;
import com.gyanam.gyanamclasses.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public void addCourse(Course course){
         courseRepository.save(course);
    }

    public List<Course> findAllCourses(){
        return courseRepository.findAll();
    }

    public Optional<Course> findCourseById(String courseId) {
        return courseRepository.findById(courseId);
    }
//    public Course removeCourse(String id) {
//        return courseRepository.existsById(id) ? courseRepository.deleteById(id) : ;
//    }
}
