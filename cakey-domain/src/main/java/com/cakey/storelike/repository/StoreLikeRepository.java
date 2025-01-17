package com.cakey.storelike.repository;

import com.cakey.storelike.domain.StoreLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreLikeRepository extends JpaRepository<StoreLike, Long> {
    // 특정 유저가 찜한 Store 수를 카운트
    @Query("SELECT COUNT(sl) FROM StoreLike sl WHERE sl.userId = :userId")
    int countByUserId(final long userId);

    int countByStoreId(final long storeId);

}
