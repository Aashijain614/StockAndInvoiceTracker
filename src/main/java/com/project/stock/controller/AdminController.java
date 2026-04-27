package com.project.stock.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.stock.dto.InvoiceRequest;
import com.project.stock.entity.Invoice;
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
		// 🔥 Always regenerate invoice number fresh from DB so it's always unique
		model.addAttribute("invoiceNumber", invoiceService.generateInvoiceNumber());
		model.addAttribute("todayDate", LocalDate.now());
		return "invoice/generateInvoice";
	}

	@PostMapping("/invoice")
	public String createInvoice(@ModelAttribute InvoiceRequest request,
								HttpSession session,
								RedirectAttributes redirectAttributes) {
		try {
			User user = (User) session.getAttribute("loggedInUser");
			invoiceService.generateInvoice(request, user.getEmail());
			redirectAttributes.addFlashAttribute("success", "Invoice " + request.getInvoiceNumber() + " generated successfully!");
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

	// 🔥 Supports optional keyword search — no wrong path anymore
	@GetMapping("/invoice-history")
	public String invoiceHistory(@RequestParam(required = false, defaultValue = "") String keyword,
								 Model model) {
		List<Invoice> all = invoiceService.getAllInvoices();

		if (!keyword.isBlank()) {
			String kw = keyword.toLowerCase();
			all = all.stream()
					.filter(inv ->
							(inv.getCustomerName() != null && inv.getCustomerName().toLowerCase().contains(kw)) ||
									(inv.getInvoiceNumber() != null && inv.getInvoiceNumber().toLowerCase().contains(kw))
					)
					.collect(Collectors.toList());
		}

		model.addAttribute("invoices", all);
		model.addAttribute("keyword", keyword);
		return "invoice/invoiceHistory";
	}

	@GetMapping("/manage-inventory")
	public String inventoryDashboard() {
		return "Inventory/ManageInventory";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
}