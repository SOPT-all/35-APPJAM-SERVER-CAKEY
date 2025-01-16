package com.cakey.store.dto;

import java.util.List;

public record StoreInfoListBylikesRes(
        Integer nextLikesCursor,
        Long lastStoreId,
        long storeCount,
        boolean isLastData,
        List<StoreInfo> stores
) {
    public static StoreInfoListBylikesRes of(final Integer nextLikesCursor,
                                             final Long lastStoreId,
                                             final long storeCount,
                                             final boolean isLastData,
                                             final List<StoreInfo> stores) {
        return new StoreInfoListBylikesRes(
                nextLikesCursor,
                lastStoreId,
                storeCount,
                isLastData,
                stores);
    }
}
