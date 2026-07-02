package com.inventory_management.service;

import com.inventory_management.model.Product;
import com.inventory_management.model.Sale;
import com.inventory_management.repository.ProductRepository;
import com.inventory_management.repository.SaleRepository;
import com.inventory_management.repository.SupplierRepository;
import com.inventory_management.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    public Map<String, Object> getDashboardSummary() {
        String businessId = SecurityUtils.getCurrentBusinessId();

        // Today's date range
        LocalDateTime startOfDay = LocalDateTime.now()
                .withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now()
                .withHour(23).withMinute(59).withSecond(59);

        // This month's date range
        LocalDateTime startOfMonth = LocalDateTime.now()
                .withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        // Get data
        List<Sale> todaySales = saleRepository
                .findByBusinessIdAndSaleDateBetween(businessId, startOfDay, endOfDay);
        List<Sale> monthlySales = saleRepository
                .findByBusinessIdAndSaleDateBetween(businessId, startOfMonth, LocalDateTime.now());
        List<Product> allProducts = productRepository.findByBusinessId(businessId);
        List<Product> lowStockProducts = allProducts.stream()
                .filter(p -> p.getQuantity() <= p.getLowStockThreshold())
                .collect(Collectors.toList());

        // Calculate revenues
        double todayRevenue = todaySales.stream()
                .mapToDouble(Sale::getTotalAmount).sum();
        double monthlyRevenue = monthlySales.stream()
                .mapToDouble(Sale::getTotalAmount).sum();
        double inventoryValue = allProducts.stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();

        // Get recent sales (last 5)
        List<Sale> allSales = saleRepository.findByBusinessId(businessId);
        List<Sale> recentSales = allSales.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .collect(Collectors.toList());

        // Build summary
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalProducts", allProducts.size());
        summary.put("totalSuppliers", supplierRepository.findByBusinessId(businessId).size());
        summary.put("totalSalesCount", allSales.size());
        summary.put("lowStockCount", lowStockProducts.size());
        summary.put("lowStockProducts", lowStockProducts);
        summary.put("todaySalesCount", todaySales.size());
        summary.put("todayRevenue", todayRevenue);
        summary.put("monthlyRevenue", monthlyRevenue);
        summary.put("inventoryValue", inventoryValue);
        summary.put("recentSales", recentSales);

        return summary;
    }

    // Sales trend for line graph (last 7 days)
    public List<Map<String, Object>> getSalesTrend() {
        String businessId = SecurityUtils.getCurrentBusinessId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Map<String, Object>> trend = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            LocalDateTime start = LocalDateTime.now().minusDays(i)
                    .withHour(0).withMinute(0).withSecond(0);
            LocalDateTime end = LocalDateTime.now().minusDays(i)
                    .withHour(23).withMinute(59).withSecond(59);

            List<Sale> daySales = saleRepository
                    .findByBusinessIdAndSaleDateBetween(businessId, start, end);

            double revenue = daySales.stream()
                    .mapToDouble(Sale::getTotalAmount).sum();

            Map<String, Object> day = new HashMap<>();
            day.put("date", start.format(formatter));
            day.put("revenue", revenue);
            day.put("salesCount", daySales.size());
            trend.add(day);
        }

        return trend;
    }

    // Revenue by payment method for pie chart
    public Map<String, Double> getRevenueByPaymentMethod() {
        String businessId = SecurityUtils.getCurrentBusinessId();
        LocalDateTime startOfMonth = LocalDateTime.now()
                .withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        List<Sale> sales = saleRepository
                .findByBusinessIdAndSaleDateBetween(businessId, startOfMonth, LocalDateTime.now());

        Map<String, Double> revenueByMethod = new HashMap<>();
        revenueByMethod.put("CASH", sales.stream()
                .filter(s -> s.getPaymentMethod() == Sale.PaymentMethod.CASH)
                .mapToDouble(Sale::getTotalAmount).sum());
        revenueByMethod.put("TRANSFER", sales.stream()
                .filter(s -> s.getPaymentMethod() == Sale.PaymentMethod.TRANSFER)
                .mapToDouble(Sale::getTotalAmount).sum());
        revenueByMethod.put("POS", sales.stream()
                .filter(s -> s.getPaymentMethod() == Sale.PaymentMethod.POS)
                .mapToDouble(Sale::getTotalAmount).sum());

        return revenueByMethod;
    }

    // Monthly revenue for bar chart
    public List<Map<String, Object>> getMonthlyRevenueTrend() {
        String businessId = SecurityUtils.getCurrentBusinessId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        List<Map<String, Object>> trend = new ArrayList<>();

        for (int i = 5; i >= 0; i--) {
            LocalDateTime start = LocalDateTime.now().minusMonths(i)
                    .withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime end = i == 0 ? LocalDateTime.now() :
                    LocalDateTime.now().minusMonths(i - 1)
                            .withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

            List<Sale> monthlySales = saleRepository
                    .findByBusinessIdAndSaleDateBetween(businessId, start, end);

            double revenue = monthlySales.stream()
                    .mapToDouble(Sale::getTotalAmount).sum();

            Map<String, Object> month = new HashMap<>();
            month.put("month", start.format(formatter));
            month.put("revenue", revenue);
            month.put("salesCount", monthlySales.size());
            trend.add(month);
        }

        return trend;
    }
}