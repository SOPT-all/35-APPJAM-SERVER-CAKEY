package com.cakey.operationtime.repository;

import com.cakey.operationtime.domain.StoreOperationTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreOperationTimeRepository extends JpaRepository<StoreOperationTime, Long> {
    Optional<StoreOperationTime> findByStoreId(Long storeId);
}
