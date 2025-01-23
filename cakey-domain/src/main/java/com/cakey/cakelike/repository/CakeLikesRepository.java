package com.cakey.cakelike.repository;

import com.cakey.cakelike.domain.CakeLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CakeLikesRepository extends JpaRepository<CakeLikes, Long> {
    boolean existsByCakeIdAndUserId(final long cakeId, final long userId);

    int countByUserId(final Long userId);

    Optional<CakeLikes> findByCakeIdAndUserId(final long cakeId, final long userId);
}
