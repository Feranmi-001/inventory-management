package com.inventory_management.repository;

import com.inventory_management.model.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SupplierRepository extends MongoRepository<Supplier, String> {

    List<Supplier> findByBusinessId(String businessId);

    List<Supplier> findByBusinessIdAndActive(String businessId, boolean active);

    List<Supplier> findByBusinessIdAndNameContainingIgnoreCase(String businessId, String name);

    boolean existsByBusinessIdAndName(String businessId, String name);
}