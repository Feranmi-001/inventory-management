package com.inventory_management.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Suppliers")
public class Supplier {
    @Id
    private String id ;
    private String businessId;

    @NotBlank(message = "Supplier name is required")
    private String name;

    private String description;
    @NotBlank(message = " Valid Email is required")
    private String email;

    private String phone;

    private String address;

    private String contactPerson;

    private String productsSupplied;

    private String ProductQuantity;

    private boolean active = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

