package com.cakey.storelike.facade;

import com.cakey.storelike.domain.StoreLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreLikeFacade {
    private final StoreLikeRetriever storeLikeRetriever;
    private final StoreLikesSaver storeLikeCreator;
    private final StoreLikesRemover storeLikesRemover;

    public int countAllLikedStoreByUserId(final long userId) {
        return storeLikeRetriever.coundAllLikedStoreByUserId(userId);
    }

    //스토어 좋아요 등록
    public void saveStoreLikes(final StoreLike storeLike) {
        storeLikeCreator.saveStoreLikes(storeLike);
    }

    //스토어 좋아요 취소
    public void deleteStoreLikes(final StoreLike storeLike) {
        storeLikesRemover.deleteByStoreLike(storeLike);
    }

    //스토어 좋아요 가져오기
    public StoreLike findStoreLikesByUserIdAndStoreId(final long userId, final long storeId) {
        return storeLikeRetriever.findByUserIdAndStoreId(userId, storeId);
    }
}
