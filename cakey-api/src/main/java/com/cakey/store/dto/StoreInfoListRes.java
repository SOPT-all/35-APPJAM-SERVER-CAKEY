package com.cakey.store.dto;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record StoreInfoListRes(
        int nextCursor,
        long storeCount,
        List<StoreInfo> stores
) {
    public static StoreInfoListRes of(final int nextCursor, final long storeCount, final List<StoreInfo> stores) {
        return new StoreInfoListRes(nextCursor, storeCount, stores);
    }
}
