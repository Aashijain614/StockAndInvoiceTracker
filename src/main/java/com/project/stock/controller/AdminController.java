package com.project.stock.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.stock.dto.InvoiceRequest;
import com.project.stock.entity.Product;
import com.project.stock.entity.User;
import com.project.stock.repository.ProductRepository;
import com.project.stock.service.InvoiceService;
import com.project.stock.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {
  
	private final InvoiceService invoiceService;
	private final ProductRepository productRepository;
	private final UserService userService;
	
	@GetMapping("/invoice-page")
	public String invoicePage(Model model) {
	    model.addAttribute("products", productRepository.findAll());
	    String invoiceNumber = invoiceService.
generateInvoiceNumber();
	    model.addAttribute("invoiceNumber",invoiceNumber);
	    model.addAttribute("todayDate",LocalDate.now());
	    return "invoice/generateInvoice"; 
	}
	
	@PostMapping("/invoice")
	public String createInvoice(@ModelAttribute InvoiceRequest request,
	                            HttpSession session,
	                            RedirectAttributes redirectAttributes) {
		System.out.println("Received Invoice Request: " + request); // 🔥 debug log
		System.out.println("Customer: " + request.getCustomerName());
		System.out.println("Invoice No: " + request.getInvoiceNumber());
		System.out.println("Date: " + request.getDate());
		System.out.println("Products: " + request.getProductIds());
		System.out.println("Qty: " + request.getQuantities());
	    try {
	    	User user = (User) session.getAttribute("loggedInUser");
	        invoiceService.generateInvoice(request, user.getEmail());

	        redirectAttributes.addFlashAttribute("success", "Invoice generated successfully!");
	        return "redirect:/invoice-page";

	    } catch (Exception e) {

	        redirectAttributes.addFlashAttribute("error", e.getMessage());
	        return "redirect:/invoice-page";
	    }
	}
	
	@GetMapping("/manage-users")
	public String manageUsers(Model model) {
	    model.addAttribute("users", userService.getAllUsers());
	    return "User/manageUser";
	}
	
	@GetMapping("/invoice-history")
	public String invoiceHistory(Model model) {
	    model.addAttribute("invoices", invoiceService.getAllInvoices());
	    return "invoice/invoiceHistory";
	}
	

	@GetMapping("/manage-inventory")
	public String inventoryDashboard() {
	    return "Inventory/ManageInventory";
	}
	

@GetMapping("/logout")
public String logout(HttpSession session) {
    session.invalidate(); // 🔥 clear session
    return "redirect:/login";
 }
}
