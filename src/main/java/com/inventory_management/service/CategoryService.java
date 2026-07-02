package com.inventory_management.service;

import com.inventory_management.model.Category;
import com.inventory_management.repository.CategoryRepository;
import com.inventory_management.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Create a new category
    public Category createCategory(Category category) {
        String businessId = SecurityUtils.getCurrentBusinessId();
        if (categoryRepository.existsByBusinessIdAndName(businessId, category.getName())) {
            throw new RuntimeException("Category with this name already exists");
        }
        category.setBusinessId(businessId);
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    // Get all categories
    public List<Category> getAllCategories() {
        String businessId = SecurityUtils.getCurrentBusinessId();
        return categoryRepository.findByBusinessId(businessId);
    }

    // Get active categories
    public List<Category> getActiveCategories() {
        String businessId = SecurityUtils.getCurrentBusinessId();
        return categoryRepository.findByBusinessIdAndActive(businessId, true);
    }

    // Get category by ID
    public Optional<Category> getCategoryById(String id) {
        return categoryRepository.findById(id);
    }

    // Update category
    public Category updateCategory(String id, Category updatedCategory) {
        return categoryRepository.findById(id).map(existing -> {
            existing.setName(updatedCategory.getName());
            existing.setDescription(updatedCategory.getDescription());
            existing.setUpdatedAt(LocalDateTime.now());
            return categoryRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    // Deactivate category
    public Category deactivateCategory(String id) {
        return categoryRepository.findById(id).map(existing -> {
            existing.setActive(false);
            existing.setUpdatedAt(LocalDateTime.now());
            return categoryRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    // Delete category
    public void deleteCategory(String id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}