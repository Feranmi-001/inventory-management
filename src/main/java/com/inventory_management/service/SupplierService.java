package com.inventory_management.service;

import com.inventory_management.model.Product;
import com.inventory_management.model.StockReceiving;
import com.inventory_management.model.Supplier;
import com.inventory_management.repository.ProductRepository;
import com.inventory_management.repository.StockReceivingRepository;
import com.inventory_management.repository.SupplierRepository;
import com.inventory_management.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final StockReceivingRepository stockReceivingRepository;
    private final ProductRepository productRepository;

    // Add a new supplier
    public Supplier createSupplier(Supplier supplier) {
        String businessId = SecurityUtils.getCurrentBusinessId();
        if (supplierRepository.existsByBusinessIdAndName(businessId, supplier.getName())) {
            throw new RuntimeException("Supplier with this name already exists");
        }
        supplier.setBusinessId(businessId);
        supplier.setCreatedAt(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());
        return supplierRepository.save(supplier);
    }

    // Get all suppliers
    public List<Supplier> getAllSuppliers() {
        String businessId = SecurityUtils.getCurrentBusinessId();
        return supplierRepository.findByBusinessId(businessId);
    }

    // Get supplier by ID
    public Optional<Supplier> getSupplierById(String id) {
        return supplierRepository.findById(id);
    }

    // Get active suppliers
    public List<Supplier> getActiveSuppliers() {
        String businessId = SecurityUtils.getCurrentBusinessId();
        return supplierRepository.findByBusinessIdAndActive(businessId, true);
    }

    // Search suppliers by name
    public List<Supplier> searchSuppliers(String name) {
        String businessId = SecurityUtils.getCurrentBusinessId();
        return supplierRepository.findByBusinessIdAndNameContainingIgnoreCase(businessId, name);
    }

    // Update supplier
    public Supplier updateSupplier(String id, Supplier updatedSupplier) {
        return supplierRepository.findById(id).map(existing -> {
            existing.setName(updatedSupplier.getName());
            existing.setEmail(updatedSupplier.getEmail());
            existing.setPhone(updatedSupplier.getPhone());
            existing.setAddress(updatedSupplier.getAddress());
            existing.setContactPerson(updatedSupplier.getContactPerson());
            existing.setProductsSupplied(updatedSupplier.getProductsSupplied());
            existing.setProductQuantity(updatedSupplier.getProductQuantity());
            existing.setUpdatedAt(LocalDateTime.now());
            return supplierRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
    }

    // Deactivate supplier
    public Supplier deactivateSupplier(String id) {
        return supplierRepository.findById(id).map(existing -> {
            existing.setActive(false);
            existing.setUpdatedAt(LocalDateTime.now());
            return supplierRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
    }

    // Receive stock from supplier
    public StockReceiving receiveStock(StockReceiving stockReceiving) {
        String businessId = SecurityUtils.getCurrentBusinessId();
        stockReceiving.setBusinessId(businessId);

        // Generate reference number
        stockReceiving.setReferenceNumber("REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        stockReceiving.setReceivedDate(LocalDateTime.now());
        stockReceiving.setCreatedAt(LocalDateTime.now());

        // Find supplier
        Supplier supplier = supplierRepository.findById(stockReceiving.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        stockReceiving.setSupplierName(supplier.getName());

        double totalCost = 0;

        // Process each item
        for (StockReceiving.StockItem item : stockReceiving.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

            // Check product belongs to this business
            if (!product.getBusinessId().equals(businessId)) {
                throw new RuntimeException("Product does not belong to your business");
            }

            // Set item details
            item.setProductName(product.getName());
            item.setSku(product.getSku());
            item.setSubtotal(item.getCostPrice() * item.getQuantity());

            // Add stock to inventory
            product.setQuantity(product.getQuantity() + item.getQuantity());
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);

            totalCost += item.getSubtotal();
        }

        stockReceiving.setTotalCost(totalCost);
        return stockReceivingRepository.save(stockReceiving);
    }

    // Get all stock receiving
    public List<StockReceiving> getAllStockReceivings() {
        String businessId = SecurityUtils.getCurrentBusinessId();
        return stockReceivingRepository.findByBusinessId(businessId);
    }

    // Get stock receiving by supplier
    public List<StockReceiving> getStockReceivingsBySupplier(String supplierId) {
        String businessId = SecurityUtils.getCurrentBusinessId();
        return stockReceivingRepository.findByBusinessIdAndSupplierId(businessId, supplierId);
    }
}