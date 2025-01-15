package com.cakey.storelikes.dto;

import com.cakey.store.dto.StoreInfo;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record StorePopularityLikedByUserRes(
        long nextLikesCursor,
        Long nexStoreId,
        long storeCount,
        List<StoreInfo> stores
) {
    public static StorePopularityLikedByUserRes of(final long nextLikesCursor,
                                                   final long nexStoreId,
                                                   final long storeCount,
                                                   final List<StoreInfo> stores) {
        return StorePopularityLikedByUserRes.builder()
                .nextLikesCursor(nextLikesCursor)
                .nexStoreId(nexStoreId)
                .storeCount(storeCount)
                .stores(stores)
                .build();
    }
}
