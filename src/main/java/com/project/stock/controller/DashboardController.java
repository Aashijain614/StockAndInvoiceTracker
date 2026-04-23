package com.project.stock.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.stock.repository.InvoiceRepository;
import com.project.stock.repository.ProductRepository;
import com.project.stock.service.DashboardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DashboardController {
	
	private final DashboardService dashboardService;
	
	private final InvoiceRepository invoiceRepository;
	
	private final ProductRepository productRepository;
	@GetMapping("/admin/dashboard")
	public String adminDashboard(Model model) {
		Map<String, Object> chartData = dashboardService.getMonthlyChartData();

        model.addAttribute("months", chartData.get("months"));
        System.out.println("Months: " + chartData.get("months")); // 🔥 debug log
        model.addAttribute("sales", chartData.get("sales"));
        System.out.println("Sales: " + chartData.get("sales")); // 🔥 debug log

        // 🔥 Stats
        model.addAttribute("totalInvoices", invoiceRepository.count());
        model.addAttribute("totalProducts", productRepository.count());

        // 🔥 Dummy (replace later with real logic)
        model.addAttribute("totalSales", 125000);
        model.addAttribute("lowStock", 3);

        // 🔥 Tables
        model.addAttribute("recentInvoices",
                invoiceRepository.findTop5ByOrderByIdDesc());

        model.addAttribute("lowStockProducts",
                productRepository.findLowStock());

        model.addAttribute("userName", "Admin");

        return "admin-dashboard";
	}

	@GetMapping("/cashier/dashboard")
	public String cashierDashboard() {
	    return "cashier-dashboard";
	}
	
	
}
