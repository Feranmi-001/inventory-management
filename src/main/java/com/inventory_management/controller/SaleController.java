package com.inventory_management.controller;

import com.inventory_management.model.Sale;
import com.inventory_management.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    // Record a new sale
    @PostMapping
    public ResponseEntity<Sale> recordSale(@Valid @RequestBody Sale sale) {
        Sale recorded = saleService.recordSale(sale);
        return ResponseEntity.status(HttpStatus.CREATED).body(recorded);
    }

    // Get all sales
    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales() {
        return ResponseEntity.ok(saleService.getAllSales());
    }

    // Get sale by ID
    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable String id) {
        return saleService.getSaleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get sales by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<Sale>> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(saleService.getSalesByDateRange(startDate, endDate));
    }

    // Get sales by payment method
    @GetMapping("/payment/{paymentMethod}")
    public ResponseEntity<List<Sale>> getSalesByPaymentMethod(
            @PathVariable Sale.PaymentMethod paymentMethod) {
        return ResponseEntity.ok(saleService.getSalesByPaymentMethod(paymentMethod));
    }

    // Get sales by customer name
    @GetMapping("/customer")
    public ResponseEntity<List<Sale>> getSalesByCustomerName(
            @RequestParam String customerName) {
        return ResponseEntity.ok(saleService.getSalesByCustomerName(customerName));
    }

    // Get total sales count
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalSalesCount() {
        return ResponseEntity.ok(saleService.getTotalSalesCount());
    }
}