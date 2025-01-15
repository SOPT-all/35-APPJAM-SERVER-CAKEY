package com.cakey.store.dto;

import java.util.List;

public record StoreInfoListBylikesRes(
        int nextLikesCursor,
        Long lastStoreId,
        long storeCount,
        List<StoreInfo> stores
) {
    public static StoreInfoListBylikesRes of(final int nextLikesCursor, final Long lastStoreId, final long storeCount, final List<StoreInfo> stores) {
        return new StoreInfoListBylikesRes(nextLikesCursor, lastStoreId, storeCount, stores);
    }
}
