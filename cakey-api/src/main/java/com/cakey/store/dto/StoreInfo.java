package com.cakey.store.dto;

import com.cakey.store.domain.Station;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record StoreInfo(
        long storeId,
        String storeName,
        Station station,
        String address,
        int storeLikesCount,
        boolean isLiked,
        List<StoreMainImage> images
) {
    public static StoreInfo of(final long storeId,
                               final String storeName,
                               final Station station,
                               final String address,
                               final int storeLikesCount,
                               final boolean isLiked,
                               final List<StoreMainImage> images) {
        return StoreInfo.builder()
                .storeId(storeId)
                .storeName(storeName)
                .address(address)
                .station(station)
                .storeLikesCount(storeLikesCount)
                .isLiked(isLiked)
                .images(images)
                .build();
    }

    public record StoreMainImage(
        long imageId,
        String imageUrl
    ) {
        public static StoreMainImage of(final long imageId, final String imageUrl) {
            return new StoreMainImage(imageId, imageUrl);
        }
    }
}
