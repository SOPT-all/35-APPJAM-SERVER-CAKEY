package com.cakey.storelike.facade;

import com.cakey.storelike.domain.StoreLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreLikeFacade {
    private final StoreLikeRetriever storeLikeRetriever;
    private final StoreLikesSaver storeLikeCreator;

    public int countAllLikedStoreByUserId(final long userId) {
        return storeLikeRetriever.coundAllLikedStoreByUserId(userId);
    }

    //스토어 좋아요 등록
    public void saveStoreLikes(final long userId, final long storeId) {
        final StoreLike newStoreLikes = StoreLike.createStoreLike(storeId, userId);
        storeLikeCreator.saveStoreLikes(newStoreLikes);
    }
}
