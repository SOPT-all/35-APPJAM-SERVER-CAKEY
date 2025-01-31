package com.cakey.store.dto;


import java.util.List;

public record StoreInfoListByLatestRes(
        Long nextStoreIdCursor,
        long storeCount,
        boolean isLastData,
        List<StoreInfo> stores
) {
    public static StoreInfoListByLatestRes of(final Long nextStoreIdCursor,
                                              final long storeCount,
                                              final boolean isLastData,
                                              final List<StoreInfo> stores) {
        return new StoreInfoListByLatestRes(
                nextStoreIdCursor,
                storeCount,
                isLastData,
                stores);
    }
}
