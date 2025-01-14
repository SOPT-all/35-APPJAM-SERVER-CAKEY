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
        boolean isLiked,
        List<StoreMainImage> images
) {
    public static StoreInfo of(final long storeId,
                               final String storeName,
                               final Station station,
                               final String address,
                               final boolean isLiked) {
        return StoreInfo.builder()
                .storeId(storeId)
                .storeName(storeName)
                .address(address)
                .isLiked(isLiked)
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
