package com.project.stock.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.stock.entity.Product;
import com.project.stock.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepository;

    // 🔍 Search product by name (case-insensitive)
    public List<Product> searchByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return productRepository.findAll();
        }
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    // 📊 Inventory Report — all products with stock summary
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ❌ Out of Stock — quantity == 0
    public List<Product> getOutOfStockProducts() {
        return productRepository.findByQuantity(0);
    }
}