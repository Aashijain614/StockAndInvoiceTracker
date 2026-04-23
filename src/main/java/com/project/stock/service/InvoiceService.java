package com.project.stock.service;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import com.project.stock.dto.InvoiceRequest;
import com.project.stock.entity.Invoice;
import com.project.stock.entity.InvoiceItem;
import com.project.stock.entity.PaymentStatus;
import com.project.stock.entity.Product;
import com.project.stock.entity.User;
import com.project.stock.repository.InvoiceItemRepository;
import com.project.stock.repository.InvoiceRepository;
import com.project.stock.repository.ProductRepository;
import com.project.stock.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class InvoiceService {

    private final ProductRepository productRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final UserRepository userRepository;

    public InvoiceService(ProductRepository productRepository,
                          InvoiceRepository invoiceRepository,
                          InvoiceItemRepository invoiceItemRepository,
                          UserRepository userRepository) {
        this.productRepository = productRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceItemRepository = invoiceItemRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public void generateInvoice(InvoiceRequest request, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Invoice invoice = new Invoice();
        invoice.setCustomerName(request.getCustomerName());
        invoice.setInvoiceNumber(request.getInvoiceNumber());
        invoice.setDate(request.getDate());
        invoice.setCreatedBy(user);
        invoice.setPaymentStatus(PaymentStatus.PENDING);

        double grandTotal = 0;

        List<InvoiceItem> items = new ArrayList<>();

        for (int i = 0; i < request.getProductIds().size(); i++) {

            Long productId = request.getProductIds().get(i);
            int qty = request.getQuantities().get(i);

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // 🔥 STOCK CHECK
            if (product.getQuantity() < qty) {
                throw new RuntimeException("Insufficient stock for: " + product.getName());
            }

            double price = product.getPrice();
            double total = price * qty;

            InvoiceItem item = new InvoiceItem();
            item.setInvoice(invoice);
            item.setProduct(product);
            item.setQuantity(qty);
            item.setPrice(price);
            item.setTotal(total);

            items.add(item);

            // 🔥 STOCK UPDATE
            product.setQuantity(product.getQuantity() - qty);
            productRepository.save(product);

            grandTotal += total;
        }

        // 🔥 FINAL SAVE
        invoice.setItems(items);
        invoice.setTotalAmount(grandTotal);

        invoiceRepository.save(invoice); // ✅ only once
    }

	public String generateInvoiceNumber() {
	
		 Long maxId = invoiceRepository.getMaxId();

		    long nextId = (maxId == null) ? 1 : maxId + 1;

		    return String.format("INV-%03d", nextId);
	}
	public List<Invoice> getAllInvoices() {
		// TODO Auto-generated method stub
		return invoiceRepository.findAll();
	}
}