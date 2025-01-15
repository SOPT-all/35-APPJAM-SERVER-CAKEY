package com.cakey.storelikes.dto;

import com.cakey.store.dto.StoreInfo;

import java.util.List;

public record StoreLatestLikedByUserRes(
        Long lastStoreId,
        long storeCount,
        List<StoreInfo> stores
) {
    public static StoreLatestLikedByUserRes fromStoreInfo(final Long lastStoreId,
                                                          final long storeCount,
                                                          final List<StoreInfo> stores) {
        return new StoreLatestLikedByUserRes(lastStoreId, storeCount, stores);
    }
}
