package com.project.stock.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.stock.entity.Product;
import com.project.stock.service.InventoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // 🔍 Search Product
    @GetMapping("/search")
    public String searchProduct(@RequestParam(required = false, defaultValue = "") String keyword, Model model) {
        List<Product> results = inventoryService.searchByName(keyword);
        model.addAttribute("products", results);
        model.addAttribute("keyword", keyword);
        return "Inventory/searchProduct";
    }

    // 📊 Inventory Report
    @GetMapping("/report")
    public String inventoryReport(Model model) {
        List<Product> products = inventoryService.getAllProducts();
        long totalProducts = products.size();
        long outOfStock = products.stream().filter(p -> p.getQuantity() == 0).count();
        long lowStock = products.stream().filter(p -> p.getQuantity() > 0 && p.getQuantity() <= p.getLowStockThreshold()).count();
        double totalValue = products.stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();

        model.addAttribute("products", products);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("outOfStock", outOfStock);
        model.addAttribute("lowStock", lowStock);
        model.addAttribute("totalValue", String.format("%.2f", totalValue));
        return "Inventory/inventoryReport";
    }

    // ❌ Out of Stock
    @GetMapping("/out-of-stock")
    public String outOfStock(Model model) {
        List<Product> products = inventoryService.getOutOfStockProducts();
        model.addAttribute("products", products);
        return "Inventory/outOfStock";
    }
}