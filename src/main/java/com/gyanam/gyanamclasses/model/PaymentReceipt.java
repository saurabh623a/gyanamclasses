package com.gyanam.gyanamclasses.model;

import java.time.LocalDate;

public record PaymentReceipt() {
    private static String studentId;
    private static double amountPaid;

    public static String getStudentId() {
        return studentId;
    }

    public static void setStudentId(String studentId) {
        PaymentReceipt.studentId = studentId;
    }

    public static double getAmountPaid() {
        return amountPaid;
    }

    public static void setAmountPaid(double amountPaid) {
        PaymentReceipt.amountPaid = amountPaid;
    }

    public static LocalDate getPaymentDate() {
        return paymentDate;
    }

    public static void setPaymentDate(LocalDate paymentDate) {
        PaymentReceipt.paymentDate = paymentDate;
    }

    private static LocalDate paymentDate;

}
