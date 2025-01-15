package com.cakey.storelikes.dto;

import com.cakey.store.dto.StoreInfo;

import java.util.List;

public record StoreLatestLikedByUser(
        Long lastStoreId,
        long storeCount,
        List<StoreInfo> stores
) {
    public static StoreLatestLikedByUser fromStoreInfo(final Long lastStoreId,
                                                       final long storeCount,
                                                       final List<StoreInfo> stores) {
        return new StoreLatestLikedByUser(lastStoreId, storeCount, stores);
    }
}
