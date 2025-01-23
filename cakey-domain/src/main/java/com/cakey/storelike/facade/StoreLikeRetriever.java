package com.cakey.storelike.facade;

import com.cakey.common.exception.NotFoundBaseException;
import com.cakey.storelike.domain.StoreLike;
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

    public StoreLike findByUserIdAndStoreId(final long userId, final long storeId) {
        return storeLikeRepository.findByUserIdAndStoreId(userId, storeId).orElseThrow(
                NotFoundBaseException::new
        );
    }



}
