package com.project.stock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
	@GetMapping("/admin/dashboard")
	public String adminDashboard() {
	    return "admin-dashboard";
	}

	@GetMapping("/cashier/dashboard")
	public String cashierDashboard() {
	    return "cashier-dashboard";
	}
}
