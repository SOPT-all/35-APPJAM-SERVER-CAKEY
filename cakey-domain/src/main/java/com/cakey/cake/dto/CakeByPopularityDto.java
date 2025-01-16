package com.cakey.cake.dto;

import com.cakey.store.domain.Station;

public record CakeByPopularityDto(
        Long cakeId,
        Long storeId,
        String imageUrl,
        String storeName,
        Long cakeLike,
        Station station,
        boolean isLiked
) {
    public static CakeByPopularityDto from(final Long cakeId,
                                           final Long storeId,
                                           final String imageUrl,
                                           final String storeName,
                                           final Long cakeLike,
                                           final Station station,
                                           final boolean isLiked
                                           ) {
        return new CakeByPopularityDto(cakeId, storeId, imageUrl, storeName, cakeLike, station, isLiked);
    }
}
