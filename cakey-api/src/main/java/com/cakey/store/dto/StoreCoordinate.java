package com.cakey.store.dto;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record StoreCoordinate(
        long storeId,
        double latitude,
        double longitude
) {
    public static StoreCoordinate of(final long storeId,
                                     final double latitude,
                                     final double longitude) {
        return StoreCoordinate.builder()
                .storeId(storeId)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
