package com.inventory_management.repository;

import com.inventory_management.model.Sale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends MongoRepository<Sale, String> {

    // Get all sales for a business
    List<Sale> findByBusinessId(String businessId);

    // Get sale by invoice number
    Optional<Sale> findByInvoiceNumberAndBusinessId(String invoiceNumber, String businessId);

    // Get sales between two dates
    List<Sale> findByBusinessIdAndSaleDateBetween(
            String businessId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // Get sales by payment method
    List<Sale> findByBusinessIdAndPaymentMethod(
            String businessId,
            Sale.PaymentMethod paymentMethod
    );

    // Get sales by customer phone
    List<Sale> findByBusinessIdAndCustomerNameContainingIgnoreCase(
            String businessId,
            String customerName
    );

    // Count total sales for a business
    Long countByBusinessId(String businessId);
}