package com.project.stock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.stock.entity.Invoice;
import com.project.stock.entity.InvoiceItem;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long>

{
	List<InvoiceItem> findByInvoice(Invoice invoice);

}
