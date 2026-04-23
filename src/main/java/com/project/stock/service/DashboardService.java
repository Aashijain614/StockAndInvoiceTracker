package com.project.stock.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.project.stock.repository.InvoiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
	
     
	private final InvoiceRepository invoiceRepository;

    public Map<String, Object> getMonthlyChartData() {

        List<Object[]> data = invoiceRepository.getMonthlySales();

        List<String> months = new ArrayList<>();
        List<Double> sales = new ArrayList<>();

        String[] monthNames = {"Jan","Feb","Mar","Apr","May","Jun",
                               "Jul","Aug","Sep","Oct","Nov","Dec"};

        for (Object[] row : data) {
            int month = (int) row[0];
            double amount = (double) row[1];

            months.add(monthNames[month - 1]);
            sales.add(amount);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("months", months);
        map.put("sales", sales);

        return map;
    }
}
