package com.gyanam.gyanamclasses.controller;

import com.gyanam.gyanamclasses.model.Course;
import com.gyanam.gyanamclasses.model.FeeRecord;
import com.gyanam.gyanamclasses.model.Student;
import com.gyanam.gyanamclasses.repository.CourseRepository;
import com.gyanam.gyanamclasses.service.CourseService;
import com.gyanam.gyanamclasses.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;



    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentService.getAllStudentsWithOutstandingFees();
        double totalPendingFees = studentService.getTotalPendingFees();
        model.addAttribute("students", students);
        model.addAttribute("totalPendingFees", totalPendingFees);
        return "students";  // Name of the Thymeleaf template
    }

    @GetMapping("/add")
    public String showAddStudentForm(Model model) {
        Student student = new Student();

        model.addAttribute("student", new Student());// Passing an empty student object for the form
        List<Course> courses = courseService.findAllCourses();
        model.addAttribute("courses", courses);  // Passing an empty student object for the form
        return "add-student";
    }

    @PostMapping("/add")
    public String addStudent(@ModelAttribute Student student) {
//        Optional<Course> course= courseService.findCourseById(courseId);
//        Course selectedCourse = null;
//        if (course.isPresent()) {
//             selectedCourse = course.get();
//        }
//        FeeRecord registrationFeeRecord = new FeeRecord();
//        assert selectedCourse != null;
//        registrationFeeRecord.setOutstandingFees(selectedCourse.getRegistrationFee());
//        registrationFeeRecord.setMonth();
//        Course selectedCourse = student.getCourse();
//        student.setRegistrationFee(selectedCourse.getRegistrationFee());
//        student.setTotalOutstandingFees(selectedCourse.getRegistrationFee() + selectedCourse.getCourseFee());
        studentService.addStudent(student);
        return "redirect:/students";  // Redirect to student list page after adding
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable String id){
        studentService.deleteStudent(id);
        return "redirect:/students";
    }
}