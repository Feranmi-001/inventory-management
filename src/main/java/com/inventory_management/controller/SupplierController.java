package com.inventory_management.controller;

import com.inventory_management.model.StockReceiving;
import com.inventory_management.model.Supplier;
import com.inventory_management.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    // Add a new supplier
    @PostMapping
    public ResponseEntity<Supplier> createSupplier(@Valid @RequestBody Supplier supplier) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.createSupplier(supplier));
    }

    // Get all suppliers
    @GetMapping
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    // Get supplier by ID
    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable String id) {
        return supplierService.getSupplierById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get active suppliers
    @GetMapping("/active")
    public ResponseEntity<List<Supplier>> getActiveSuppliers() {
        return ResponseEntity.ok(supplierService.getActiveSuppliers());
    }

    // Search suppliers by name
    @GetMapping("/search")
    public ResponseEntity<List<Supplier>> searchSuppliers(@RequestParam String name) {
        return ResponseEntity.ok(supplierService.searchSuppliers(name));
    }

    // Update supplier
    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable String id,
                                                   @Valid @RequestBody Supplier supplier) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, supplier));
    }

    // Deactivate supplier
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Supplier> deactivateSupplier(@PathVariable String id) {
        return ResponseEntity.ok(supplierService.deactivateSupplier(id));
    }

    // Receive stock from supplier
    @PostMapping("/receive-stock")
    public ResponseEntity<StockReceiving> receiveStock(@Valid @RequestBody StockReceiving stockReceiving) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.receiveStock(stockReceiving));
    }

    // Get all stock receiving
    @GetMapping("/receive-stock")
    public ResponseEntity<List<StockReceiving>> getAllStockReceivings() {
        return ResponseEntity.ok(supplierService.getAllStockReceivings());
    }

    // Get stock receiving by supplier
    @GetMapping("/{supplierId}/stock-receiving")
    public ResponseEntity<List<StockReceiving>> getStockReceivingsBySupplier(
            @PathVariable String supplierId) {
        return ResponseEntity.ok(supplierService.getStockReceivingsBySupplier(supplierId));
    }
}