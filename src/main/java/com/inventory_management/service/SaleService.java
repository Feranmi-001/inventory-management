package com.inventory_management.service;

import com.inventory_management.model.Product;
import com.inventory_management.model.Sale;
import com.inventory_management.repository.ProductRepository;
import com.inventory_management.repository.SaleRepository;
import com.inventory_management.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;


    public Sale recordSale(Sale sale) {
        String businessId = SecurityUtils.getCurrentBusinessId();
        sale.setBusinessId(businessId);


        sale.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        sale.setSaleDate(LocalDateTime.now());
        sale.setCreatedAt(LocalDateTime.now());

        double totalAmount = 0;

        // Process each item in the sale
        for (Sale.SaleItem item : sale.getItems()) {
            // Find the product
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

            // Check if product belongs to this business
            if (product.getBusinessId() == null || !product.getBusinessId().equals(businessId)){
                throw new RuntimeException("Product does not belong to your business");
            }

            // Check if enough stock is available
            if (product.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName()
                        + ". Available: " + product.getQuantity());
            }

            // Set item details from product
            item.setProductName(product.getName());
            item.setSku(product.getSku());
            item.setUnitPrice(product.getPrice());
            item.setSubtotal(product.getPrice() * item.getQuantity());

            product.setQuantity(product.getQuantity() - item.getQuantity());
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);


            if (product.getQuantity() <= product.getLowStockThreshold()) {
                System.out.println("LOW STOCK ALERT: " + product.getName()
                        + " only " + product.getQuantity() + " left!");
            }

            totalAmount += item.getSubtotal();
        }

        sale.setTotalAmount(totalAmount);
        return saleRepository.save(sale);
    }

    // Get all sales
    public List<Sale> getAllSales() {
        String businessId = SecurityUtils.getCurrentBusinessId();
        return saleRepository.findByBusinessId(businessId);
    }

    // Get sale by ID
    public Optional<Sale> getSaleById(String id) {
        return saleRepository.findById(id);
    }

    // Get sales by date range
    public List<Sale> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String businessId = SecurityUtils.getCurrentBusinessId();
        return saleRepository.findByBusinessIdAndSaleDateBetween(businessId, startDate, endDate);
    }

    // Get sales by payment method
    public List<Sale> getSalesByPaymentMethod(Sale.PaymentMethod paymentMethod) {
        String businessId = SecurityUtils.getCurrentBusinessId();
        return saleRepository.findByBusinessIdAndPaymentMethod(businessId, paymentMethod);
    }

    // Get sales by customer name
    public List<Sale> getSalesByCustomerName(String customerName) {
        String businessId = SecurityUtils.getCurrentBusinessId();
        return saleRepository.findByBusinessIdAndCustomerNameContainingIgnoreCase(businessId, customerName);
    }

    // Get total sales count
    public Long getTotalSalesCount() {
        String businessId = SecurityUtils.getCurrentBusinessId();
        return saleRepository.countByBusinessId(businessId);
    }
}