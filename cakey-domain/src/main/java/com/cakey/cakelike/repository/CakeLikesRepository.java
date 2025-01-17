package com.cakey.cakelike.repository;

import com.cakey.cakelike.domain.CakeLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CakeLikesRepository extends JpaRepository<CakeLikes, Long> {
    boolean existsByCakeIdAndUserId(final Long cakeId, final Long userId);

    int countByUserId(final Long userId);

    CakeLikes findByCakeIdAndUserId(final Long cakeId, final Long userId);

}
