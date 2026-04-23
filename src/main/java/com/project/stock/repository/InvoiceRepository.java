package com.project.stock.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.stock.entity.Invoice;
import com.project.stock.entity.User;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
	 Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

	    List<Invoice> findByCreatedBy(User user); // 🔥 cashier ke invoices

	    List<Invoice> findByCustomerName(String customerName);

	    @Query("SELECT MAX(i.id) FROM Invoice i")
		Long getMaxId();
	    
        
	    @Query("SELECT MONTH(i.date), SUM(i.totalAmount) " +
	            "FROM Invoice i GROUP BY MONTH(i.date) ORDER BY MONTH(i.date)")
	     List<Object[]> getMonthlySales();

	     // Recent invoices
	     List<Invoice> findTop5ByOrderByIdDesc();
}
