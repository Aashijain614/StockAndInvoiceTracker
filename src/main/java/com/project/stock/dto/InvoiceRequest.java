package com.project.stock.dto;

import java.time.LocalDate;
import java.util.List;

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
    private LocalDate date;

    private List<Long> productIds;
    private List<Integer> quantities;
}
