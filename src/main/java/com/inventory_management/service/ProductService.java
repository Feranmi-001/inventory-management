package com.inventory_management.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.inventory_management.security.SecurityUtils;
import com.inventory_management.model.Product;
import com.inventory_management.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    // Add a new product
    public Product createProduct(Product product) {
        String businessId = SecurityUtils.getCurrentBusinessId();
        if (productRepository.existsBySku(product.getSku())) {
            throw new RuntimeException("Product with SKU " + product.getSku() + " already exists");
        }
        product.setBusinessId(businessId);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    // Get all products
    public List<Product> getAllProducts() {
        String businessId = SecurityUtils.getCurrentBusinessId();
        return productRepository.findByBusinessId(businessId);
    }



    // Get product by ID
    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    // Get product by SKU
    public Optional<Product> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    // Get products by category
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    // Search products by name
    public List<Product> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    // Get low stock products
    public List<Product> getLowStockProducts() {
        return productRepository.findByQuantityLessThanEqual(10);
    }

    // Update a product
    public Product updateProduct(String id, Product updatedProduct) {
        return productRepository.findById(id).map(existing -> {
            existing.setName(updatedProduct.getName());
            existing.setDescription(updatedProduct.getDescription());
            existing.setCategory(updatedProduct.getCategory());
            existing.setPrice(updatedProduct.getPrice());
            existing.setQuantity(updatedProduct.getQuantity());
            existing.setUnit(updatedProduct.getUnit());
            existing.setLowStockThreshold(updatedProduct.getLowStockThreshold());
            existing.setUpdatedAt(LocalDateTime.now());
            return productRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // Delete a product
    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);

    }
    // Get products with pagination
    public Page<Product> getProductsPaginated(int page, int size) {
        String businessId = SecurityUtils.getCurrentBusinessId();
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByBusinessId(businessId, pageable);
    }
}