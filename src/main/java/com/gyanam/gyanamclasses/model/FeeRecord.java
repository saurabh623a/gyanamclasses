package com.gyanam.gyanamclasses.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "fees")
public class FeeRecord {

    private String month;
    private double totalFees;
    private double feesPaid;
    private double outstandingFees;
}
