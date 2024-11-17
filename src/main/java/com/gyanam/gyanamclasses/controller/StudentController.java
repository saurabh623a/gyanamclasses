package com.gyanam.gyanamclasses.controller;

import com.gyanam.gyanamclasses.model.Course;
import com.gyanam.gyanamclasses.model.Student;
import com.gyanam.gyanamclasses.repository.StudentRepository;
import com.gyanam.gyanamclasses.service.CourseService;
import com.gyanam.gyanamclasses.service.StudentService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseService courseService;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/images";

    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentService.getAllStudentsWithOutstandingFees();
        double totalPendingFees = studentService.getTotalPendingFees();
        model.addAttribute("students", students);
        model.addAttribute("totalPendingFees", totalPendingFees);
        return "students";
    }

    @GetMapping("/add")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new Student());
        List<Course> courses = courseService.findAllCourses();
        model.addAttribute("courses", courses);
        return "add-student";
    }

    @PostMapping("/add")
    public String addStudent(@ModelAttribute Student student,
                             @RequestParam("photo") MultipartFile photo, Model model) {

        if (!photo.isEmpty()) {
            try {
                // Create directories if they don't exist
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Save the file
                String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(photo.getInputStream(), filePath);

                // Set the file URL in the student object (store as a string)
                student.setPhotoUrl("/" + UPLOAD_DIR + fileName);
            } catch (IOException e) {
                model.addAttribute("errorMessage", "File upload failed.");
                return "add-student";
            }
        }

        studentService.addStudent(student);
        return "redirect:/students";
    }



    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }

    @PostMapping("/payFees")
    public String payFees(@RequestParam String studentId, @RequestParam double amountPaid, Model model) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();

            // Validate that the payment amount does not exceed outstanding fees
            if (amountPaid > student.getTotalOutstandingFees()) {
                model.addAttribute("errorMessage", "Payment amount cannot exceed the total outstanding fees.");
                model.addAttribute("studentId", studentId); // Retain studentId for the form
                return "payFee";
            }

            // Deduct the paid amount from total outstanding fees
            double newOutstandingFees = student.getTotalOutstandingFees() - amountPaid;
            student.setTotalOutstandingFees(newOutstandingFees < 0 ? 0 : newOutstandingFees); // Ensure no negative fees

            // Save the updated student to the database
            studentRepository.save(student);

            // Optionally, create a receipt record
//            PaymentReceipt receipt = new PaymentReceipt();
//            PaymentReceipt.setStudentId(studentId);
//            PaymentReceipt.setAmountPaid(amountPaid);
//            PaymentReceipt.setPaymentDate(LocalDate.now());
//            paymentReceiptRepository.save(receipt);
        } else {
            model.addAttribute("errorMessage", "Student not found.");
            return "payFee";
        }

        // Redirect back to the Students page
        return "redirect:/students";
    }


    @GetMapping("/payFees")
    public String showPayFeesForm(@RequestParam(required = false) String studentId, Model model) {
        model.addAttribute("studentId", studentId); // Prepopulate Student ID
        return "payFee";
    }
    @GetMapping("/generateReceipt/{id}")
    public void generateReceipt(@PathVariable String id, HttpServletResponse response) throws Exception {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            throw new IllegalArgumentException("Student not found");
        }

        // Set response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=receipt.pdf");

        // Generate PDF
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Payment Receipt"));
        document.add(new Paragraph("Student Name: " + student.getName()));
        document.add(new Paragraph("Course: " + student.getCourse().getName()));
        document.add(new Paragraph("Total Outstanding Fees: " + student.getTotalOutstandingFees()));
        document.close();
    }
    @GetMapping("/outstanding")
    public String viewStudentsWithOutstandingFees(Model model) {
        List<Student> studentsWithOutstandingFees = studentRepository.findByTotalOutstandingFeesGreaterThan(0.0);
        double totalOutstandingAmount = studentsWithOutstandingFees.stream()
                .mapToDouble(Student::getTotalOutstandingFees)
                .sum();  // Sum of all outstanding fees

        model.addAttribute("students", studentsWithOutstandingFees);
        model.addAttribute("totalOutstandingAmount", totalOutstandingAmount);  // Add the total amount to the model
        return "studentsWithOutstandingFees";
    }
    @GetMapping("/edit/{id}")
    public String editStudentForm(@PathVariable String id, Model model) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            List<Course> courses = courseService.findAllCourses(); // Fetch all courses for dropdown
            model.addAttribute("student", student);
            model.addAttribute("courses", courses);
            return "editStudent";
        } else {
            model.addAttribute("errorMessage", "Student not found.");
            return "redirect:/students";
        }
    }
    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable String id, @ModelAttribute("student") Student updatedStudent, Model model) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();

            // Update fields
            student.setName(updatedStudent.getName());
            student.setCourse(updatedStudent.getCourse());
            student.setRegistrationDate(updatedStudent.getRegistrationDate());
            student.setRegistrationFee(updatedStudent.getRegistrationFee());
            student.setTotalOutstandingFees(updatedStudent.getTotalOutstandingFees());

            studentRepository.save(student); // Save updated student
            return "redirect:/students";
        } else {
            model.addAttribute("errorMessage", "Student not found.");
            return "redirect:/students";
        }
    }
}