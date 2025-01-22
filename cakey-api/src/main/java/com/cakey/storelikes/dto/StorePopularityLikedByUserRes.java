package com.cakey.storelikes.dto;

import com.cakey.store.dto.StoreInfo;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record StorePopularityLikedByUserRes(
        Integer nextLikesCursor,
        Long nextStoreId,
        long storeCount,
        boolean isLastData,
        List<StoreInfo> stores
) {
    public static StorePopularityLikedByUserRes of(final Integer nextLikesCursor,
                                                   final Long nextStoreId,
                                                   final long storeCount,
                                                   final boolean isLastData,
                                                   final List<StoreInfo> stores) {
        return StorePopularityLikedByUserRes.builder()
                .nextLikesCursor(nextLikesCursor)
                .nextStoreId(nextStoreId)
                .storeCount(storeCount)
                .isLastData(isLastData)
                .stores(stores)
                .build();
    }
}
