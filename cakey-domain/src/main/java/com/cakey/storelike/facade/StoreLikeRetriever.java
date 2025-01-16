package com.cakey.storelike.facade;

import com.cakey.storelike.repository.StoreLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreLikeRetriever {
    private final StoreLikeRepository storeLikeRepository;

    public int coundAllLikedStoreByUserId(final long userId) {
        return storeLikeRepository.countByUserId(userId);
    }

}
