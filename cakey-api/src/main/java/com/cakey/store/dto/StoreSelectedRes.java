package com.cakey.store.dto;

import com.cakey.store.domain.Station;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record StoreSelectedRes(
        long storeId,
        String storeName,
        String address,
        Station station,
        boolean isLiked,
        String imageUrl,
        int StoreLikesCount
) {
    public static StoreSelectedRes of(final long storeId,
                                      final String storeName,
                                      final String address,
                                      final Station station,
                                      final boolean isLiked,
                                      final String imageUrl,
                                      final int StoreLikesCount) {
        return StoreSelectedRes.builder()
                .storeId(storeId)
                .storeName(storeName)
                .address(address)
                .station(station)
                .isLiked(isLiked)
                .imageUrl(imageUrl)
                .StoreLikesCount(StoreLikesCount)
                .build();
    }
}
