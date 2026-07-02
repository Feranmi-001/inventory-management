package com.inventory_management.repository;

import com.inventory_management.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    List<Category> findByBusinessId(String businessId);

    List<Category> findByBusinessIdAndActive(String businessId, boolean active);

    Optional<Category> findByBusinessIdAndName(String businessId, String name);

    boolean existsByBusinessIdAndName(String businessId, String name);
}