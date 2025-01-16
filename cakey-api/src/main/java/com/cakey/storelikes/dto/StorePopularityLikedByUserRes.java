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
        List<StoreInfo> stores
) {
    public static StorePopularityLikedByUserRes of(final Integer nextLikesCursor,
                                                   final Long nexStoreId,
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
