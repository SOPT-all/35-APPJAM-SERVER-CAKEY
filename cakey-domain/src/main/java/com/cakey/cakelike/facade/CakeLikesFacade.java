package com.cakey.cakelike.facade;

import com.cakey.cakelike.domain.CakeLikes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CakeLikesFacade {
    private final CakeLikesRetriever cakeLikesRetriever;

    public boolean existsCakeLikesByCakeIdAndUserId(Long cakeId, Long userId) {
        return cakeLikesRetriever.existsCakeLikesByCakeIdAndUserId(cakeId, userId);
    }

    public void saveCakeLikes(CakeLikes cakeLikes) {
        cakeLikesRetriever.saveCakeLikes(cakeLikes);
    }
}
