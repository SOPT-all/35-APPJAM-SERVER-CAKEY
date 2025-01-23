package com.cakey.cake.repository;

import com.cakey.cake.domain.Cake;
import com.cakey.cake.dto.CakeByPopularityDto;
import java.util.List;

import com.cakey.store.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CakeRepository extends JpaRepository<Cake, Long>, CakeRepositoryCustom {
    //스토어에 해당하는 케이크들 조회
    @Query("SELECT c FROM Cake c WHERE c.storeId = :storeId ORDER BY c.id DESC")
    List<Cake> findAllByStoreId(@Param("storeId") final long storeId);

    //해당 지하철역 스토어의 케이크들 개수
    @Query("SELECT COUNT(DISTINCT c.id) FROM Cake c JOIN Store s ON c.storeId = s.id WHERE s.station = :station")
    int countCakesByStation(@Param("station") final Station station);

    @Query("SELECT new com.cakey.cake.dto.CakeByPopularityDto(c.id, c.storeId, c.imageUrl, s.name, COUNT(cl.id), s.station, " +
            "COALESCE(CASE WHEN EXISTS (SELECT 1 FROM CakeLikes cl2 WHERE cl2.cakeId = c.id AND cl2.userId = :userId) THEN true ELSE false END, false)) " +
            "FROM Cake c " +
            "LEFT JOIN CakeLikes cl ON c.id = cl.cakeId " +
            "JOIN Store s ON c.storeId = s.id " +
            "GROUP BY c.id, c.storeId, c.imageUrl, s.name, s.station " +
            "ORDER BY COUNT(cl.id) DESC, c.id ASC LIMIT 10")
    List<CakeByPopularityDto> findCakesByRank(@Param("userId") final Long userId);

    //찜한 스토어들의 전체 디자인 개수
    @Query("SELECT COUNT(DISTINCT c.id) FROM Cake c JOIN StoreLike sl ON c.storeId = sl.storeId WHERE sl.userId = :userId")
    int countAllDesignsLikedByUser(@Param("userId") final Long userId);

}
