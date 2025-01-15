package com.cakey.store.repository;

import com.cakey.store.domain.Station;
import com.cakey.store.domain.Store;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {

    // 전체 스토어 개수
    @Query("SELECT COUNT(s) FROM Store s")
    int countAllStores();

    // 특정 지하철역의 스토어 개수
    @Query("SELECT COUNT(s) FROM Store s WHERE s.station = :stationName")
    int countStoresByStation(Station stationName);

    Optional<Store> findById(Long storeId);
}
