package com.inventory_management.repository;

import com.inventory_management.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    Page<Product> findByBusinessId(String businessId, Pageable pageable);
    // Find product by SKU
    Optional<Product> findBySku(String sku);

    // Find products by category
    List<Product> findByCategory(String category);

    // Find products with low stock
    List<Product> findByQuantityLessThanEqual(Integer threshold);

    // Search products by name
    List<Product> findByNameContainingIgnoreCase(String name);

    // Find products by supplier
    List<Product> findBySupplierId(String supplierId);

    List<Product> findByBusinessId(String businessId);
    // Check if SKU already exists
    boolean existsBySku(String sku);
}
