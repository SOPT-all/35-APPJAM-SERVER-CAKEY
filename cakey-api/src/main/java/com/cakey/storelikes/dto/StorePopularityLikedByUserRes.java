package com.cakey.storelikes.dto;

import com.cakey.store.dto.StoreInfo;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record StorePopularityLikedByUserRes(
        Integer nextLikesCursor,
        Long nexStoreId,
        long storeCount,
        boolean isLastData,
        List<StoreInfo> stores
) {
    public static StorePopularityLikedByUserRes of(final Integer nextLikesCursor,
                                                   final Long nexStoreId,
                                                   final long storeCount,
                                                   final boolean isLastData,
                                                   final List<StoreInfo> stores) {
        return StorePopularityLikedByUserRes.builder()
                .nextLikesCursor(nextLikesCursor)
                .nexStoreId(nexStoreId)
                .storeCount(storeCount)
                .isLastData(isLastData)
                .stores(stores)
                .build();
    }
}
