package com.project.stock.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Invoice {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNumber;

    private String customerName;

    private LocalDate date;

    private double totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

}
