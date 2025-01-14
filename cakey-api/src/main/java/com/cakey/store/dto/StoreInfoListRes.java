package com.cakey.store.dto;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record StoreInfoListRes(
        int nextLikesCursor,
        Long lastStoreId,
        long storeCount,
        List<StoreInfo> stores
) {
    public static StoreInfoListRes of(final int nextLikesCursor, final Long lastStoreId, final long storeCount, final List<StoreInfo> stores) {
        return new StoreInfoListRes(nextLikesCursor, lastStoreId, storeCount, stores);
    }
}
