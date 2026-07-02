package com.inventory_management.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    private String id;
    private String businessId;

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "SKU is required")
    @Indexed(unique = true)
    private String sku;

    @NotNull(message = "Price is required")
    @PositiveOrZero
    private Double price;

    @NotNull(message = "Quantity is required")
    @PositiveOrZero
    private Integer quantity;

    @PositiveOrZero
    private Integer lowStockThreshold = 10;

    private String supplierId;

    private String unit;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}