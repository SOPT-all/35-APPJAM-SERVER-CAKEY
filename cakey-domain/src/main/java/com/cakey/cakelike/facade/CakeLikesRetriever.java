package com.cakey.cakelike.facade;

import com.cakey.cakelike.domain.CakeLikes;
import com.cakey.cakelike.repository.CakeLikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CakeLikesRetriever {
    private final CakeLikesRepository cakeLikesRepository;

    public boolean existsCakeLikesByCakeIdAndUserId(Long cakeId, Long userId) {
        return cakeLikesRepository.existsByCakeIdAndUserId(cakeId, userId);
    }

    public void saveCakeLikes(final CakeLikes cakeLikes) {
        cakeLikesRepository.save(cakeLikes);
    }

    public int countByUserId(final Long userId) {
        return cakeLikesRepository.countByUserId(userId);
    }
}
