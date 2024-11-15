package com.gyanam.gyanamclasses.service;


import com.gyanam.gyanamclasses.repository.StudentRepository;
import com.gyanam.gyanamclasses.model.FeeRecord;
import com.gyanam.gyanamclasses.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;


    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    public Student getStudentById(String id) {
        return studentRepository.findById(id).orElse(null);
    }

    public void addStudent(Student student) {
        studentRepository.save(student);
    }
    public void addFeesRecord(String studentId, FeeRecord feeRecord){
        Student student = getStudentById(studentId);
        if (student != null ){
            student.getFeeRecords().add(feeRecord);
            studentRepository.save(student);
        }
    }
    public void deleteStudent(String id) {
        studentRepository.deleteById(id);
    }
    public double getTotalPendingFees(){
        return studentRepository.findAll().stream()
                .flatMap(student -> student.getFeeRecords() == null ? Stream.empty():student.getFeeRecords().stream())
                .mapToDouble(FeeRecord::getOutstandingFees)
                .sum();
    }
    public List<Student> getAllStudentsWithOutstandingFees() {
        //        students.forEach(student -> {
//            double totalFees = student.getFeeRecords() == null ? 0:student.getFeeRecords().stream().mapToDouble(FeeRecord::getOutstandingFees).sum();
//            student.setTotalOutstandingFees(totalFees);
//        });
        return studentRepository.findAll();
    }

    public void updateStudent(Student student) {
        studentRepository.save(student);
    }
}
