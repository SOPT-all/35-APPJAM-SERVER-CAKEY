package com.cakey.cakelike.facade;

import com.cakey.cakelike.domain.CakeLikes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CakeLikesFacade {
    private final CakeLikesRetriever cakeLikesRetriever;
    private final CakeLikesSaver cakeLikesSaver;
    private final CakeLikesRemover cakeLikesRemover;

    public boolean existsCakeLikesByCakeIdAndUserId(final long cakeId, final long userId) {
        return cakeLikesRetriever.existsCakeLikesByCakeIdAndUserId(cakeId, userId);
    }

    public void saveCakeLikes(final CakeLikes cakeLikes) {
        cakeLikesSaver.saveCakeLikes(cakeLikes);
    }

    public int countByUserId(final Long userId) {
        return cakeLikesRetriever.countByUserId(userId);
    }

    public void removeCakeLikes(CakeLikes cakeLikes) {
        cakeLikesRemover.removeCakeLikes(cakeLikes);
    }

    public CakeLikes getCakeLikesByCakeIdAndUserId(final long cakeId, final long userId) {
        return cakeLikesRetriever.findCakeLikesByCakeIdAndUserId(cakeId, userId);
    }

}
