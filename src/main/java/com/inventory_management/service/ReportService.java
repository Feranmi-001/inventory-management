package com.inventory_management.service;

import com.inventory_management.model.Product;
import com.inventory_management.model.Sale;
import com.inventory_management.repository.ProductRepository;
import com.inventory_management.repository.SaleRepository;
import com.inventory_management.repository.StockReceivingRepository;
import com.inventory_management.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final StockReceivingRepository stockReceivingRepository;

    // Get sales summary for a date range
    public Map<String, Object> getSalesSummary(LocalDateTime startDate, LocalDateTime endDate) {
        String businessId = SecurityUtils.getCurrentBusinessId();
        List<Sale> sales = saleRepository.findByBusinessIdAndSaleDateBetween(
                businessId, startDate, endDate);

        double totalRevenue = sales.stream()
                .mapToDouble(Sale::getTotalAmount)
                .sum();

        double cashRevenue = sales.stream()
                .filter(s -> s.getPaymentMethod() == Sale.PaymentMethod.CASH)
                .mapToDouble(Sale::getTotalAmount)
                .sum();

        double transferRevenue = sales.stream()
                .filter(s -> s.getPaymentMethod() == Sale.PaymentMethod.TRANSFER)
                .mapToDouble(Sale::getTotalAmount)
                .sum();

        double posRevenue = sales.stream()
                .filter(s -> s.getPaymentMethod() == Sale.PaymentMethod.POS)
                .mapToDouble(Sale::getTotalAmount)
                .sum();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalSales", sales.size());
        summary.put("totalRevenue", totalRevenue);
        summary.put("cashRevenue", cashRevenue);
        summary.put("transferRevenue", transferRevenue);
        summary.put("posRevenue", posRevenue);
        summary.put("startDate", startDate);
        summary.put("endDate", endDate);
        return summary;
    }

    // Get best_selling products
    public List<Map<String, Object>> getBestSellingProducts(
            LocalDateTime startDate, LocalDateTime endDate) {
        String businessId = SecurityUtils.getCurrentBusinessId();
        List<Sale> sales = saleRepository.findByBusinessIdAndSaleDateBetween(
                businessId, startDate, endDate);

        Map<String, Integer> productSales = new HashMap<>();
        Map<String, Double> productRevenue = new HashMap<>();

        for (Sale sale : sales) {
            for (Sale.SaleItem item : sale.getItems()) {
                productSales.merge(item.getProductName(), item.getQuantity(), Integer::sum);
                productRevenue.merge(item.getProductName(), item.getSubtotal(), Double::sum);
            }
        }

        return productSales.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .map(entry -> {
                    Map<String, Object> product = new HashMap<>();
                    product.put("productName", entry.getKey());
                    product.put("quantitySold", entry.getValue());
                    product.put("revenue", productRevenue.get(entry.getKey()));
                    return product;
                })
                .collect(Collectors.toList());
    }

    // Get low stock products
    public List<Product> getLowStockReport() {
        String businessId = SecurityUtils.getCurrentBusinessId();
        return productRepository.findByBusinessId(businessId).stream()
                .filter(p -> p.getQuantity() <= p.getLowStockThreshold())
                .collect(Collectors.toList());
    }

    // Get daily sales report
    public Map<String, Object> getDailySalesReport() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return getSalesSummary(startOfDay, endOfDay);
    }

    // Get weekly sales report
    public Map<String, Object> getWeeklySalesReport() {
        LocalDateTime startOfWeek = LocalDateTime.now().minusDays(7);
        LocalDateTime endOfWeek = LocalDateTime.now();
        return getSalesSummary(startOfWeek, endOfWeek);
    }

    // Get monthly sales report
    public Map<String, Object> getMonthlySalesReport() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = LocalDateTime.now();
        return getSalesSummary(startOfMonth, endOfMonth);
    }
// Get monthly sales report
    public Map<String, Object> getYearlySalesReport(){
        LocalDateTime startOfYear = LocalDateTime.now().withMonth(1)
                .withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfYear = LocalDateTime.now();
        return getSalesSummary(startOfYear, endOfYear);
    }

    // Get inventory value
    public Map<String, Object> getInventoryValue() {
        String businessId = SecurityUtils.getCurrentBusinessId();
        List<Product> products = productRepository.findByBusinessId(businessId);

        double totalValue = products.stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();

        Map<String, Object> report = new HashMap<>();
        report.put("totalProducts", products.size());
        report.put("totalInventoryValue", totalValue);
        report.put("products", products);
        return report;
    }
}