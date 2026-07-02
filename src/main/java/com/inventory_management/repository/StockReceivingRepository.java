package com.inventory_management.repository;

import com.inventory_management.model.StockReceiving;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockReceivingRepository extends MongoRepository<StockReceiving, String> {

    List<StockReceiving> findByBusinessId(String businessId);

    List<StockReceiving> findByBusinessIdAndSupplierId(String businessId, String supplierId);

    List<StockReceiving> findByBusinessIdAndReceivedDateBetween(
            String businessId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}