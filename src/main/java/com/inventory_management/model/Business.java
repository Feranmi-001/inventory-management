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
@Document(collection = "businesses")
public class Business {

    @Id
    private String id;

    @NotBlank(message = "Business name is required")
    private String name;

    @Email(message = "Valid email is required")
    @Indexed(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private String phone;

    private String address;

    private String industry;

    private boolean active = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}