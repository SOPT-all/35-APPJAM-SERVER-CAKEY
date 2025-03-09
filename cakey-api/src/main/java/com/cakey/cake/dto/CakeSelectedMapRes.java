package com.cakey.cake.dto;

import com.cakey.store.domain.Station;
import lombok.Builder;

@Builder
public record CakeSelectedMapRes(
        long storeId,
        String storeName,
        String address,
        Station station,
        boolean isLiked,
        String imageUrl
) {

    public static CakeSelectedMapRes from(final long storeId,
                                 final String storeName,
                                 final String address,
                                 final Station station,
                                 final boolean isLiked,
                                 final String imageUrl) {
        return CakeSelectedMapRes.builder()
                .storeId(storeId)
                .storeName(storeName)
                .address(address)
                .station(station)
                .isLiked(isLiked)
                .imageUrl(imageUrl)
                .build();
    }
}
