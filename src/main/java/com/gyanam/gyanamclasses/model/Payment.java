package com.gyanam.gyanamclasses.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private LocalDate paymentDate;
    private Double amountPaid;
}
