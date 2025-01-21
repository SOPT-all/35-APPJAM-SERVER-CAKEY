package com.cakey.cakelike.facade;

import com.cakey.cakelike.domain.CakeLikes;
import com.cakey.cakelike.repository.CakeLikesRepository;
import com.cakey.common.exception.NotFoundBaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CakeLikesRetriever {
    private final CakeLikesRepository cakeLikesRepository;

    public boolean existsCakeLikesByCakeIdAndUserId(final long cakeId, final long userId) {
        return cakeLikesRepository.existsByCakeIdAndUserId(cakeId, userId);
    }

    public void saveCakeLikes(final CakeLikes cakeLikes) {
        cakeLikesRepository.save(cakeLikes);
    }

    public int countByUserId(final Long userId) {
        return cakeLikesRepository.countByUserId(userId);
    }

    public CakeLikes findCakeLikesByCakeIdAndUserId(final long cakeId, final long userId) {
        return cakeLikesRepository.findByCakeIdAndUserId(cakeId, userId).orElseThrow(NotFoundBaseException::new);
    }
}
