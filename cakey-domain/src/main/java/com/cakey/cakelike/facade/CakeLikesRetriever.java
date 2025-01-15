package com.cakey.cakelike.facade;

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
}
