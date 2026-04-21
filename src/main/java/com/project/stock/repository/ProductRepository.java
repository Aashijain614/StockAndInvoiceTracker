package com.project.stock.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.stock.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>
{
	Optional<Product> findByName(String name);

    List<Product> findByQuantityLessThanEqual(int threshold); // 🔥 low stock alert
}

