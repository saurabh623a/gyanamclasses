package com.gyanam.gyanamclasses.controller;

import com.gyanam.gyanamclasses.model.Course;
import com.gyanam.gyanamclasses.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/add")
    public String showAddCourseForm(Model model){
        model.addAttribute("course", new Course());
        return "addCourse";
    }

    @PostMapping("/add")
    public String addCourse(@ModelAttribute Course course) {
        courseService.addCourse(course);
        return "redirect:/courses/list";
    }
    @GetMapping("/list")
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.findAllCourses());
        return "listCourses";
    }
}
