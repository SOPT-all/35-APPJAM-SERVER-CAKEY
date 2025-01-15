package com.cakey.cake.repository;

import com.cakey.cake.domain.Cake;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CakeRepository extends JpaRepository<Cake, Long>, CakeRepositoryCustom {
    @Query("SELECT c FROM Cake c WHERE c.storeId = :storeId ORDER BY c.id DESC")
    List<Cake> findAllByStoreId(@Param("storeId") final long storeId);
}
