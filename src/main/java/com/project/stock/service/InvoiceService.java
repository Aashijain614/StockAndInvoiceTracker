package com.project.stock.service;

import org.springframework.stereotype.Service;

import com.project.stock.dto.InvoiceRequest;
import com.project.stock.entity.Invoice;
import com.project.stock.entity.InvoiceItem;
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

        // 🔥 user fetch
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 create invoice
        Invoice invoice = new Invoice();
        invoice.setCustomerName(request.getCustomerName());
        invoice.setInvoiceNumber(request.getInvoiceNumber());
        invoice.setDate(request.getDate());
        invoice.setCreatedBy(user);

        double grandTotal = 0;

        invoice = invoiceRepository.save(invoice); // save first

        // 🔥 loop items
        for (int i = 0; i < request.getProductIds().size(); i++) {

            Long productId = request.getProductIds().get(i);
            int qty = request.getQuantities().get(i);

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // 🔥 VALIDATION
            if (product.getQuantity() < qty) {
                throw new RuntimeException("Insufficient stock for: " + product.getName());
            }

            double price = product.getPrice(); // 🔥 backend price
            double total = price * qty;

            // 🔥 create invoice item
            InvoiceItem item = new InvoiceItem();
            item.setInvoice(invoice);
            item.setProduct(product);
            item.setQuantity(qty);
            item.setPrice(price);
            item.setTotal(total);

            invoiceItemRepository.save(item);

            // 🔥 STOCK DEDUCTION
       
            product.setQuantity(product.getQuantity() - qty);
            productRepository.save(product);

            // 🔥 LOW STOCK ALERT (optional)
            if (product.getQuantity() <= product.getLowStockThreshold()) {
                System.out.println("⚠ Low stock: " + product.getName());
            }

            grandTotal += total;
        }

        // 🔥 update total
        invoice.setTotalAmount(grandTotal);
        invoiceRepository.save(invoice);
    }

	public String generateInvoiceNumber() {
	
		 Long maxId = invoiceRepository.getMaxId();

		    long nextId = (maxId == null) ? 1 : maxId + 1;

		    return String.format("INV-%03d", nextId);
	}
}