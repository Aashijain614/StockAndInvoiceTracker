package com.project.stock.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.stock.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>
{
	Optional<Product> findByName(String name);

    List<Product> findByQuantityLessThanEqual(int threshold);// 🔥 low stock alert
    
    @Query("SELECT p FROM Product p WHERE p.quantity <= p.lowStockThreshold")
    List<Product> findLowStock();
}






