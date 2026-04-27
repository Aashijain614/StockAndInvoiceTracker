package com.project.stock.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest {

    private String customerName;
    private String invoiceNumber;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  // 🔥 FIX: binds "yyyy-MM-dd" from HTML date input
    private LocalDate date;

    private List<Long> productIds;
    private List<Integer> quantities;
}