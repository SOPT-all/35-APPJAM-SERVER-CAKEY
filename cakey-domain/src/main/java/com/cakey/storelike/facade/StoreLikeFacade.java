package com.cakey.storelike.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreLikeFacade {
    private final StoreLikeRetriever storeLikeRetriever;

    public int countAllLikedStoreByUserId(final long userId) {
        return storeLikeRetriever.coundAllLikedStoreByUserId(userId);
    }
}
