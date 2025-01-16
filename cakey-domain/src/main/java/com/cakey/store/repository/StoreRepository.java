package com.cakey.store.repository;

import com.cakey.store.domain.Station;
import com.cakey.store.domain.Store;
import com.cakey.store.dto.StoreByPopularityDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {

    // 전체 스토어 개수
    @Query("SELECT COUNT(s) FROM Store s")
    int countAllStores();

    // 특정 지하철역의 스토어 개수
    @Query("SELECT COUNT(s) FROM Store s WHERE s.station = :stationName")
    int countStoresByStation(Station stationName);

    @Query("SELECT new com.cakey.store.dto.StoreByPopularityDto(s.id, s.name, COUNT(sl.id), s.station) " +
            "FROM Store s " +
            "LEFT JOIN StoreLike sl ON s.id = sl.storeId " +
            "GROUP BY s.id, s.name, s.station " +
            "ORDER BY COUNT(sl.id) DESC, s.createdDate ASC " +
            "LIMIT 5")
    List<StoreByPopularityDto> findStoresByLikeCount();
}

