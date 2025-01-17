package com.cakey.cake.dto;

import com.cakey.store.domain.Station;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CakeInfo(
        long cakeId,
        long storeId,
        String storeName,
        Station station,
        boolean isLiked,
        String imageUrl,
        int cakeLikesCount
) {
    public static CakeInfo of(final long cakeId,
                              final long storeId,
                              final String storeName,
                              final Station station,
                              final boolean isLiked,
                              final String imageUrl,
                              final int cakeLikesCount
    ) {
        return CakeInfo.builder()
                .cakeId(cakeId)
                .storeId(storeId)
                .storeName(storeName)
                .station(station)
                .isLiked(isLiked)
                .imageUrl(imageUrl)
                .cakeLikesCount(cakeLikesCount)
                .build();
    }
}
