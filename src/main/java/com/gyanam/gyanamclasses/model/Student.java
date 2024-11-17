package com.gyanam.gyanamclasses.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "students")
public class Student {

    @Id
    private String id;
    private String name;
    private int age;
    private String contact;
    private double totalOutstandingFees;
    private LocalDate registrationDate;
    private Double registrationFee;
    private String photoUrl; // Field to store the image as binary data
    @DBRef
    private Course course;
    private List<FeeRecord> feeRecords = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();
}
