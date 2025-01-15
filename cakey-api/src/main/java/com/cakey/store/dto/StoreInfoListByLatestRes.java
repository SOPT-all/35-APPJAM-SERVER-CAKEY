package com.cakey.store.dto;


import java.util.List;

public record StoreInfoListByLatest(
        Long nextStoreIdCursor,
        long storeCount,
        List<StoreInfo> stores
) {
    public static StoreInfoListByLatest of(final Long nextStoreIdCursor,
                                     final long storeCount,
                                     final List<StoreInfo> stores) {
        return new StoreInfoListByLatest(nextStoreIdCursor, storeCount, stores);
    }
}
