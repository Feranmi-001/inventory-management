package com.inventory_management.controller;

import com.inventory_management.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // Get dashboard summary
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        return ResponseEntity.ok(dashboardService.getDashboardSummary());
    }

    // Get sales trend for line graph
    @GetMapping("/sales-trend")
    public ResponseEntity<List<Map<String, Object>>> getSalesTrend() {
        return ResponseEntity.ok(dashboardService.getSalesTrend());
    }

    // Get revenue by payment method for pie chart
    @GetMapping("/payment-breakdown")
    public ResponseEntity<Map<String, Double>> getRevenueByPaymentMethod() {
        return ResponseEntity.ok(dashboardService.getRevenueByPaymentMethod());
    }

    // Get monthly revenue trend for bar chart
    @GetMapping("/monthly-trend")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyRevenueTrend() {
        return ResponseEntity.ok(dashboardService.getMonthlyRevenueTrend());
    }
}