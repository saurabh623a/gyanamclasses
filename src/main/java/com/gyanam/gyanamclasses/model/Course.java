package com.gyanam.gyanamclasses.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "course")
@Data
public class Course {

    @Id
    private String id;
    private String name;
    private double registrationFee;
    private double courseFee;

}
