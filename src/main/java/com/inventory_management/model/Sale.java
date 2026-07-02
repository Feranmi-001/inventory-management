package com.inventory_management.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sales")
public class Sale {

    @Id
    private String id;

    private String businessId;

    private String invoiceNumber;

    @NotNull(message = "Sale items are required")
    private List<SaleItem> items;

    private Double totalAmount;

    private String customerName;

    private String customerPhone;

    private PaymentMethod paymentMethod;

    private String notes;

    private LocalDateTime saleDate;

    private LocalDateTime createdAt;

    public enum PaymentMethod {
        CASH,
        TRANSFER,
        POS
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaleItem {

        @NotBlank(message = "Product ID is required")
        private String productId;

        private String productName;

        private String sku;

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        private Integer quantity;

        private Double unitPrice;

        private Double subtotal;
    }
}