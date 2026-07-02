package com.inventory_management.repository;

import com.inventory_management.model.Business;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BusinessRepository extends MongoRepository<Business, String> {

    Optional<Business> findByEmail(String email);
    boolean existsByEmail(String email);
}