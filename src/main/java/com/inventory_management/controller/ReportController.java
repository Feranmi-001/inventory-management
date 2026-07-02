package com.inventory_management.controller;

import com.inventory_management.model.Product;
import com.inventory_management.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // Get sales summary by date range
    @GetMapping("/sales-summary")
    public ResponseEntity<Map<String, Object>> getSalesSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(reportService.getSalesSummary(startDate, endDate));
    }

    // Get best_selling products
    @GetMapping("/best-sellers")
    public ResponseEntity<List<Map<String, Object>>> getBestSellingProducts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(reportService.getBestSellingProducts(startDate, endDate));
    }

    // Get low stock report
    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockReport() {
        return ResponseEntity.ok(reportService.getLowStockReport());
    }

    // Get daily sales report
    @GetMapping("/daily")
    public ResponseEntity<Map<String, Object>> getDailySalesReport() {
        return ResponseEntity.ok(reportService.getDailySalesReport());
    }

    // Get weekly sales report
    @GetMapping("/weekly")
    public ResponseEntity<Map<String, Object>> getWeeklySalesReport() {
        return ResponseEntity.ok(reportService.getWeeklySalesReport());
    }

    // Get monthly sales report
    @GetMapping("/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlySalesReport() {
        return ResponseEntity.ok(reportService.getMonthlySalesReport());
    }

    // Get yearly sales report
    @GetMapping("/yearly")
    public ResponseEntity<Map<String, Object>> getYearlySalesReport() {
        return ResponseEntity.ok(reportService.getYearlySalesReport());
    }

    // Get inventory value
    @GetMapping("/inventory-value")
    public ResponseEntity<Map<String, Object>> getInventoryValue() {
        return ResponseEntity.ok(reportService.getInventoryValue());
    }
}