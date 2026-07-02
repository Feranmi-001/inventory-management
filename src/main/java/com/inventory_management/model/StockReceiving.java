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
@Document(collection = "stock_receiving")
public class StockReceiving {

    @Id
    private String id;

    private String businessId;

    @NotBlank(message = "Supplier ID is required")
    private String supplierId;

    private String supplierName;

    private String referenceNumber;

    @NotNull(message = "Items are required")
    private List<StockItem> items;

    private Double totalCost;

    private String notes;

    private LocalDateTime receivedDate;

    private LocalDateTime createdAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockItem {

        @NotBlank(message = "Product ID is required")
        private String productId;

        private String productName;

        private String sku;

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        private Integer quantity;

        private Double costPrice;

        private Double subtotal;
    }
}